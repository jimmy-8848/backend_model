package com.example.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FlowUtils {
    @Autowired
    StringRedisTemplate redisTemplate;
    public boolean limitOnceCheck(String key,long blockTime){
        if(redisTemplate.hasKey(key)) return false;
        redisTemplate.opsForValue().set(key,"",blockTime, TimeUnit.SECONDS);
        return true;
    }
}
