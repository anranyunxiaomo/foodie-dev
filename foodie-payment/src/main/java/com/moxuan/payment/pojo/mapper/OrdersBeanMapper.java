package com.moxuan.payment.pojo.mapper;

import com.moxuan.payment.pojo.Orders;
import com.moxuan.payment.pojo.bo.MerchantOrdersBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "Spring")
public interface OrdersBeanMapper {

    @Mappings({
            @Mapping(target = "payStatus",constant = "20"),
            @Mapping(target = "id",source = "ordersId"),
            @Mapping(target = "comeFrom",constant = "天天吃货"),
            @Mapping(target = "isDelete",constant = "0"),
            @Mapping(target = "createdTime", expression = "java(new java.util.Date())")
    })
    Orders combination(MerchantOrdersBO merchantOrdersBO, String ordersId);

}
