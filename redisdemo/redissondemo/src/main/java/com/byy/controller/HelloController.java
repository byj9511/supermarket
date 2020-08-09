package com.byy.controller;

import com.byy.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
public class HelloController {
    private Logger logger=LoggerFactory.getLogger(HelloController.class);

    @Autowired
    @Qualifier("mainThreadPoolExecutor")
    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    RedisService redisService;

    @Autowired
    RedisTemplate redisTemplate ;

//    直接使用redis进行数据自增，存在线程（不同应用的线程）安全问题
    @RequestMapping("/incre1")
    public String inredemo1(){
        redisTemplate.opsForValue().increment("incre1");
        return "OK";
    }
//    利用redisson操作，官方推荐的方式
    @RequestMapping("/incre2")
    public String inredemo2(){
        redisService.incre2();
        return "OK";
    }
//    异步执行任务，并使用定义好的线程池
    @RequestMapping("/async")
    public String ansyc() throws ExecutionException, InterruptedException {
        long s = System.currentTimeMillis();
        System.out.println("ansyc运行");
        CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("taskA:运行线程：{}",Thread.currentThread().getName());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "1";
        },threadPoolExecutor).thenApply((res)->{
            logger.info("taskA:thenApply:运行线程：{},结果{}",Thread.currentThread().getName(),res);
            return res;
        });
        CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("taskB:运行线程：{}",Thread.currentThread().getName());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "2";
        },threadPoolExecutor).thenApplyAsync((res)->{
            logger.info("taskB:thenApplyAsync运行线程：{},结果{}",Thread.currentThread().getName(),res);
            return res;
        },threadPoolExecutor);
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        CompletableFuture<Void> taskC = CompletableFuture.supplyAsync(() -> {
            logger.info("taskC:运行线程：{}",Thread.currentThread().getName());
            throw new RuntimeException("抛一个异常");
        },threadPoolExecutor);
//        等待三个任务执行完成
        CompletableFuture<Void> all = CompletableFuture.allOf(taskA, taskB, taskC);
        all.join();
//        为了处理taskC的运行时异常，必须get(),get会阻塞主线程
/*        try {
            taskC.get();
        } catch (Exception e) {
            throw new RuntimeException("抛一个异常");
        }*/

        long duration = System.currentTimeMillis()-s;

        return String.valueOf(duration)+"ms";
    }
//    测试单实例是否会造成线程阻塞
    @RequestMapping("sleep")
    public String singletonSleep(){
       redisTemplate.opsForValue().increment("incre1");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
//    结论：线程阻塞是由于请求的资源被占用导致的，用单实例可以处理并发，并不会造成堵塞
}
