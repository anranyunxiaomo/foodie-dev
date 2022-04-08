package com.moxuan.pojo.mapper;

import com.moxuan.bo.UserAddBO;
import com.moxuan.pojo.Users;
import com.moxuan.vo.UsersVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserBeanMapper {

    // 密码加密
    @Mappings({
            @Mapping(target = "password", expression = "java(com.moxuan.utils.MD5Utils.getMD5Str(userAddBO.getPassword()))"),
            @Mapping(target = "nickname", source = "username")})
    Users toUsers(UserAddBO userAddBO);

    UsersVO toUsersVO(Users users,String userUniqueToken);
}
