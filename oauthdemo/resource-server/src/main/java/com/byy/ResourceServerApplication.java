package com.byy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableGlobalMethodSecurity(prePostEnabled =true,securedEnabled = true)
@SpringBootApplication
public class ResourceServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class,args);
    }
}
