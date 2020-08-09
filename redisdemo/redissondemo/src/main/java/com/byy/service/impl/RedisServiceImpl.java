package com.byy.service.impl;

import com.byy.service.RedisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    RedissonClient redisson;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void incre2() {
        RLock lock = redisson.getLock("lock");
        lock.lock();
        redisTemplate.opsForValue().increment("incre2");
        lock.unlock();
    }
}
