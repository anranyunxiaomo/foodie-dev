package com.moxuan.pojo.mapper.center;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxuan.pojo.OrderStatus;
import com.moxuan.utils.PagedGridResult;
import com.moxuan.vo.center.MyOrdersVO;
import com.moxuan.vo.center.OrderStatusCountsVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CenterOrdersBeanMapper {


    @Mappings({
            @Mapping(target = "page", source = "page.current"),
            @Mapping(target = "rows", source = "page.records"),
            @Mapping(target = "total", source = "page.pages"),
            @Mapping(target = "records", source = "page.total")
    })
    PagedGridResult<MyOrdersVO> toMyOrdersVO(Page<MyOrdersVO> page);

    @Mappings({
            @Mapping(target = "page", source = "page.current"),
            @Mapping(target = "rows", source = "page.records"),
            @Mapping(target = "total", source = "page.pages"),
            @Mapping(target = "records", source = "page.total")
    })
    PagedGridResult<OrderStatus> toOrderStatus(Page<OrderStatus> page);

    OrderStatusCountsVO combination(Integer waitPayCounts,
                                    Integer waitDeliverCounts,
                                    Integer waitReceiveCounts,
                                    Integer waitCommentCounts);

}
