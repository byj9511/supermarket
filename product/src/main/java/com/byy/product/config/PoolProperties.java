package com.byy.product.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "product.threadpool")
public class PoolProperties {
    int corePoolSize;
    int maximumPoolSize;
    int workQueue;
}
