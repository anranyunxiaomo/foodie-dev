package com.moxuan.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "user")
@Data
public class UserFaceProperties {

    /**
     * 用户默认头像
     */
    private String face;

}
