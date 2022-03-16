package com.moxuan.mapper;

import com.moxuan.bo.UserAddBO;
import com.moxuan.pojo.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", expression = "java(com.moxuan.utils.MD5Utils.getMD5Str(userAddBO.getPassword()))")
    Users toUsers(UserAddBO userAddBO);

}
