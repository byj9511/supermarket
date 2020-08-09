package com.byy.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {
    @Bean("mainThreadPoolExecutor")
    public ThreadPoolExecutor mainThreadPoolExecutor(PoolProperties poolProperties){
        return new ThreadPoolExecutor(poolProperties.getCorePoolSize(), poolProperties.getMaximumPoolSize()
                , 1, TimeUnit.SECONDS,new ArrayBlockingQueue<>(poolProperties.getWorkQueue()));

    }
}
