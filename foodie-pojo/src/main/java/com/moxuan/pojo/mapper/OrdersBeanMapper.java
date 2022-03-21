package com.moxuan.pojo.mapper;

import com.moxuan.ao.MountAO;
import com.moxuan.bo.SubmitOrderBO;
import com.moxuan.pojo.Orders;
import com.moxuan.pojo.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrdersBeanMapper {


    @Mappings({
            @Mapping(target = "id", source = "orderId"),
            @Mapping(target = "userId", source = "submitOrderBO.userId"),
            @Mapping(target = "receiverName", source = "userAddress.receiver"),
            @Mapping(target = "receiverMobile", source = "userAddress.mobile"),
            @Mapping(target = "receiverAddress", expression = "java(com.moxuan.function.OrdersFunction.spliceAddress(userAddress))"),
            @Mapping(target = "postAmount", source = "postAmount"),
            @Mapping(target = "payMethod", source = "submitOrderBO.payMethod"),
            @Mapping(target = "leftMsg", source = "submitOrderBO.leftMsg"),
            @Mapping(target = "isComment", constant = "1"),
            @Mapping(target = "isDelete", constant = "1"),
            @Mapping(target = "createdTime", expression = "java(new java.util.Date())"),
            @Mapping(target = "updatedTime", expression = "java(new java.util.Date())"),
            @Mapping(target = "totalAmount", source = "mountAO.totalAmount"),
            @Mapping(target = "realPayAmount", source = "mountAO.realPayAmount"),
    })
    Orders combination(String orderId,
                       SubmitOrderBO submitOrderBO,
                       UserAddress userAddress,
                       Integer postAmount,
                       MountAO mountAO);
}
