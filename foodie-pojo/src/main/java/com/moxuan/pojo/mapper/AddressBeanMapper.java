package com.moxuan.pojo.mapper;

import com.moxuan.bo.AddressBO;
import com.moxuan.bo.AddressUpdateBO;
import com.moxuan.pojo.UserAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressBeanMapper {


    UserAddress addToAddress(AddressBO addressBO);


    @Mapping(target = "id", source = "addressUpdateBO.addressId")
    UserAddress updateToAddress(AddressUpdateBO addressUpdateBO);
}
