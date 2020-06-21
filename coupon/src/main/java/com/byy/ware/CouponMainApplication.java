package com.byy.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class CouponMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponMainApplication.class, args);
    }
}
