package com.moxuan.mapper;

import com.moxuan.bo.UserAddBO;
import com.moxuan.pojo.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Value;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // 密码加密
    @Mappings({
    @Mapping(target = "password", expression = "java(com.moxuan.utils.MD5Utils.getMD5Str(userAddBO.getPassword()))"),
    @Mapping(target = "nickname", source = "username")})
    Users toUsers(UserAddBO userAddBO);

}
