package com.moxuan.payment.pojo.bo;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class MerchantOrdersBO {

    /**
     * 商户订单号
     */
    @NotBlank(message = "商户订单号不能为空")
    private String merchantOrderId;

    /**
     * 商户方的发起用户的用户主键id
     */
    @NotBlank(message = "用户唯一标识不能为空")
    private String merchantUserId;

    /**
     * 实际支付总金额（包含商户所支付的订单费邮费总额）
     */
    @NotNull(message = "实际支付总金额不能为空")
    private Integer amount;

    /**
     * 支付方式 1:微信   2:支付宝
     */
    private Integer payMethod;

    /**
     * 支付成功后的回调地址
     */
    @NotBlank(message = "支付成功后的回调地址不能为空")
    private String returnUrl;

}
