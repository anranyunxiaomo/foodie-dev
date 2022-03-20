package com.moxuan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payment")
@Data
public class PaymentProperties {

    /**
     * 支付成功回调支付中心接口地址
     */
    private String callback;

    /**
     * 创建订单通知支付中心的接口地址
     */
    private String notice;
}
