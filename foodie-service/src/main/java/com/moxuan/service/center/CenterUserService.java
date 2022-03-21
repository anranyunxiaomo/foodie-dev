package com.moxuan.service.center;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.UsersMapper;
import com.moxuan.pojo.Users;
import com.moxuan.pojo.center.CenterUserBO;
import com.moxuan.pojo.mapper.center.CenterUserBeanMapper;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.BindingResultUtil;
import com.moxuan.utils.CookieUtils;
import com.moxuan.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
public class CenterUserService extends ServiceImpl<UsersMapper, Users> {

    @Autowired
    private BindingResultUtil bindingResultUtil;
    @Resource
    private CenterUserBeanMapper centerUserBeanMapper;

    /**
     * 用户中心-用户信息
     */
    public BaseResp userInfo(String userId) {
        Users users = this.lambdaQuery().setEntity(new Users())
                // 查询时，排除密码
                .select(c -> !Objects.equals(c.getProperty(), "password"))
                .eq(Users::getId, userId)
                .one();
        return ResultUtil.ok(users);
    }

    /**
     * 修改用户信息
     */
    public BaseResp updateUserInfo(String userId,
                                   CenterUserBO centerUserBO,
                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        // 校验字段是否错误
        if (result.hasErrors()) {
            return ResultUtil.error(JSONUtil.toJsonStr(bindingResultUtil.getErrors(result)));
        }
        // 更新用户信息
        Users users = centerUserBeanMapper.combination(centerUserBO, userId);
        this.updateById(users);
        users = this.lambdaQuery().eq(Users::getId, userId)
                .select(Users::getUsername, Users::getId, Users::getFace, Users::getSex)
                .one();
        // 更新cookie
        CookieUtils.setCookie(request, response, "user",
                JSONUtil.toJsonStr(users), true);
        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话
        return ResultUtil.ok();
    }


}
