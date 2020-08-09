package com.byy.product.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@RestController("thread")
public class ThreadController {
    @Qualifier("mainThreadPoolExecutor")
    @Autowired
    ThreadPoolExecutor mainThreadPoolExecutor;


    @RequestMapping
    public Map<String, Object> getStatus(){
        final HashMap<String, Object> map = new HashMap<>();
        mainThreadPoolExecutor.getCorePoolSize();
        mainThreadPoolExecutor.getActiveCount();
        return map;
    }
}
