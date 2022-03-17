package com.moxuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.moxuan","org.n3r"})
public class FoodieApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodieApiApplication.class,args);
    }

}
