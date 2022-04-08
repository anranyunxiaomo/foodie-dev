package com.moxuan.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.bo.UserAddBO;
import com.moxuan.bo.UserBO;
import com.moxuan.config.UserFaceProperties;
import static com.moxuan.constant.RedisKeyConstant.*;
import com.moxuan.enums.Sex;
import com.moxuan.mapper.UsersMapper;
import com.moxuan.pojo.Users;
import com.moxuan.pojo.mapper.UserBeanMapper;
import com.moxuan.utils.*;
import com.moxuan.vo.UsersVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.moxuan.constant.RedisKeyConstant.FOODIE_SHOPCART;

@Service
public class UsersService extends ServiceImpl<UsersMapper, Users> {

    @Resource
    private UserBeanMapper userMapper;
    @Resource
    private UserFaceProperties userFaceProperties;
    @Autowired
    private Sid sid;
    @Autowired
    private ShopcartService shopcartService;
    @Autowired
    private RedisOperator redisOperator;

    /**
     * 注册名称校验
     */
    public BaseResp usernameIsExist(String username) {
        // 判断用户名称是否为空
        if (StrUtil.isBlank(username)) {
            return ResultUtil.error("用户名称不能为空");
        }
        // 判断用户名称是否已经存在
        Users users = this.lambdaQuery().eq(Users::getUsername, username).one();
        if (!ObjectUtil.isEmpty(users)) {
            return ResultUtil.error("用户名称已存在");
        }
        // 可以注册
        return ResultUtil.ok();
    }


    /**
     * 注册
     */
    public BaseResp<Users> regist(UserAddBO userAddBO, HttpServletRequest request, HttpServletResponse response) {
        // 0 校验密码是否相同
        if (!userAddBO.getPassword().equals(userAddBO.getConfirmPassword())) {
            return ResultUtil.error("二次密码输入不相同");
        }
        // 1 密码长度不能低于6位
        if (userAddBO.getPassword().length() < 6) {
            return ResultUtil.error("密码长度小于6位");
        }
        // 2 查询用户名称是否存在
        Users users = this.lambdaQuery().eq(Users::getUsername, userAddBO.getUsername()).one();
        if (!ObjectUtil.isEmpty(users)) {
            return ResultUtil.error("用户名称已存在");
        }
        // 3 注册
        users = userMapper.toUsers(userAddBO);
        // 默认头像
        users.setFace(userFaceProperties.getFace());
        // 设置默认生日
        users.setBirthday(DateUtil.parseDate("1993-02-05"));
        // 默认性别
        users.setSex(Sex.secret.type);
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());
        users.setId(sid.nextShort());
        this.save(users);
        users = this.lambdaQuery()
                .eq(Users::getId, users.getId())
                .select(Users::getUsername, Users::getId, Users::getFace, Users::getSex)
                .one();
        // 设置redis 分布式会话token
        generateUserInfo(request, response, users);
        // 同步购物车的信息
        shopcartService.synchShopcartData(users.getId(), request, response);
        return ResultUtil.ok();
    }

    /**
     * 登陆
     */
    public BaseResp<Users> login(UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        Users users = this.lambdaQuery()
                .eq(Users::getUsername, userBO.getUsername())
                .eq(Users::getPassword, MD5Utils.getMD5Str(userBO.getPassword()))
                .select(Users::getUsername, Users::getId, Users::getFace, Users::getSex)
                .one();
        if (ObjectUtil.isNull(users)) {
            return ResultUtil.error("用户名或密码不正确");
        }
        // 设置redis 分布式会话token
        generateUserInfo(request, response, users);
        // 同步购物车的信息
        shopcartService.synchShopcartData(users.getId(), request, response);
        return ResultUtil.ok(users);
    }

    public void generateUserInfo(HttpServletRequest request, HttpServletResponse response, Users users) {
        // 获取一个唯一token
        String userUniqueToken = UUID.randomUUID().toString().trim();
        UsersVO usersVO = userMapper.toUsersVO(users, userUniqueToken);
        // 存储到token
        redisOperator.set(REDIS_USER_TOKEN.getName()+":"+ users.getId(),userUniqueToken);
       // 更新 cookie
        CookieUtils.setCookie(request, response, "user", JSONUtil.toJsonStr(usersVO), true);
    }

    /**
     * 退出
     */
    public BaseResp logout(String userId, HttpServletRequest request, HttpServletResponse response) {
        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");
        // 用户退出登录，清除redis中user的会话信息
        redisOperator.del(REDIS_USER_TOKEN.getName() + ":" + userId);
        // 分布式会话中需要清除用户数据
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART.getName());
        return ResultUtil.ok();
    }

}

