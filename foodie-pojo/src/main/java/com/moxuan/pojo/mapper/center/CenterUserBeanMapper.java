package com.moxuan.pojo.mapper.center;

import com.moxuan.pojo.Users;
import com.moxuan.pojo.center.CenterUserBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "Spring")
public interface CenterUserBeanMapper {

    @Mappings({
            @Mapping(target = "id", source = "userId"),
            @Mapping(target = "updatedTime", expression = "java(new java.util.Date())")
    })
    Users combination(CenterUserBO centerUserBO, String userId);
}
