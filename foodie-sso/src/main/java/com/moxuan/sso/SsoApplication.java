package com.moxuan.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.moxuan", "org.n3r"})
public class SsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoApplication.class,args);
    }
}
