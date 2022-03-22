package com.moxuan.config;

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

    /**
     * 文件存储位置的路径 除了 {driveLetter}
     */
    private String pathPrefix;

    /**
     * 文件盘符 Linux 加上/ 即可 win c:// 即可
     */
    private String driveLetter;

    private String imageServerUrl;
}
