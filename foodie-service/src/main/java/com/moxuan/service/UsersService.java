package com.moxuan.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.moxuan.utils.BaseResp;
import com.moxuan.utils.ResultUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.UsersMapper;
import com.moxuan.pojo.Users;

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
        if (ObjectUtil.isEmpty(users)){
            return ResultUtil.error("用户名称已存在");
        }
        // 可以注册
        return ResultUtil.ok();
    }

}

