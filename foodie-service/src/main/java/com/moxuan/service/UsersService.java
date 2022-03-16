package com.moxuan.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.bo.UserAddBO;
import com.moxuan.mapper.UserMapper;
import com.moxuan.mapper.UsersMapper;
import com.moxuan.pojo.Users;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.ResultUtil;
import org.springframework.stereotype.Service;

@Service
public class UsersService extends ServiceImpl<UsersMapper, Users> {


    /**
     * 注册名称校验
     */
    public BaseResp usernameIsExist(String username) {
        // 判断用户名称是否为空
        if (StrUtil.isBlank(username)){
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
    public BaseResp regist(UserAddBO userAddBO) {
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
        users = UserMapper.INSTANCE.toUsers(userAddBO);
        System.out.println(users);


        return null;

    }


}

