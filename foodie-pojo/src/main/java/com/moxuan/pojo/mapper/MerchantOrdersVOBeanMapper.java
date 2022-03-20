package com.moxuan.pojo.mapper;


import com.moxuan.vo.MerchantOrdersVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MerchantOrdersVOBeanMapper {

    MerchantOrdersVO combination(String merchantOrderId, String merchantUserId, int amount, Integer payMethod, String returnUrl);
}
