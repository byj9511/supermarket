package com.byy.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.byy.member.dao")
@EnableFeignClients(basePackages = "com.byy.member.feign")

public class MemberMainApplication {
    public static void main(String[] args) {
        new SpringApplication().run(MemberMainApplication.class,args);
    }
}
