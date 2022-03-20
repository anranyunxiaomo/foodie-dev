package com.moxuan.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressUpdateBO extends AddressBO {

    @NotBlank(message = "地址ID不能为空")
    private String addressId;

}
