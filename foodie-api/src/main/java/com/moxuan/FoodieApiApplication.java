package com.moxuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 开启定时任务
@SpringBootApplication
@ComponentScan(basePackages = {"com.moxuan", "org.n3r"})
public class FoodieApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodieApiApplication.class,args);
    }

}
