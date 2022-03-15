package com.moxuan.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moxuan.mapper.UsersMapper;
import com.moxuan.pojo.Users;

@Service
public class UsersService extends ServiceImpl<UsersMapper, Users> {

}

