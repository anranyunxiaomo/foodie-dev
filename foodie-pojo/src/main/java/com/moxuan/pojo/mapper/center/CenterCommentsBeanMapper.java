package com.moxuan.pojo.mapper.center;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxuan.bo.center.OrderItemsCommentBO;
import com.moxuan.pojo.ItemsComments;
import com.moxuan.utils.PagedGridResult;
import com.moxuan.vo.center.MyCommentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface CenterCommentsBeanMapper {


    @Mappings({
            @Mapping(target = "createdTime", expression = "java(new java.util.Date())"),
            @Mapping(target = "updatedTime", expression = "java(new java.util.Date())"),
            @Mapping(target = "sepcName", source = "comment.itemSpecName"),
    })
    ItemsComments combination(String userId, String id, OrderItemsCommentBO comment);


    @Mappings({
            @Mapping(target = "page", source = "page.current"),
            @Mapping(target = "rows", source = "page.records"),
            @Mapping(target = "total", source = "page.pages"),
            @Mapping(target = "records", source = "page.total")
    })
    PagedGridResult<MyCommentVO> toMyCommentVO(Page<MyCommentVO> page);

}
