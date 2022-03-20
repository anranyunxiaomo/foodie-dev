package com.moxuan.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SubmitOrderBO {

    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    /**
     * 商品规格IDs [,]
     */
    @NotBlank(message = "商品规格ID不能为空")
    private String itemSpecIds;

    /**
     * 地址ID
     */
    @NotBlank(message = "地址ID不能为空")
    private String addressId;

    /**
     * 支付方法 1。微信 2 支付宝
     */
    // @Size(min = 1, max = 2, message = "支付方法不支持")
    private Integer payMethod;


    /**
     * 订单备注
     */
    private String leftMsg;


}
