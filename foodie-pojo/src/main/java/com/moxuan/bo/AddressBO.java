package com.moxuan.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户新增或修改地址的BO
 */
@Data
public class AddressBO {


    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotBlank(message = "收件人姓名不能为空")
    private String receiver;

    @NotBlank(message = "收件人手机号不能为空")
    private String mobile;

    @NotBlank(message = "省份不能为空")
    private String province;

    @NotBlank(message = "城市不能为空")
    private String city;

    @NotBlank(message = "区县不能为空")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    private String detail;

}
