package com.byy;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemoTest {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedissonClient redisson;

    @Test
    public void demo1(){
        RLock lock = redisson.getLock("lock1");
        lock.lock();
        redisTemplate.opsForValue().set("user","user");
    }
}
