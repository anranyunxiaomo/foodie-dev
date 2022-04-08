package com.moxuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 开启定时任务
@SpringBootApplication  (
        //  在启动类中使用 exclude 排除 security 的自动配置就可以了
    //    exclude = {SecurityAutoConfiguration.class}
)
@ComponentScan(basePackages = {"com.moxuan", "org.n3r"})
public class FoodieApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodieApiApplication.class,args);
    }

}
