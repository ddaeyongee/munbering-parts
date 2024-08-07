package com.assignment.numbering_parts.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLockUtil {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisLockUtil(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean tryLock(String lockKey, long timeoutInMillis) {
        return redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "locked", timeoutInMillis, TimeUnit.MILLISECONDS);
    }

    public void unlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }
}