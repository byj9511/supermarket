package com.byy.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.byy.member.dao")
public class MemberMainApplication {
    public static void main(String[] args) {
        new SpringApplication().run(MemberMainApplication.class,args);
    }
}
