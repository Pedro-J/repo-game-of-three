package com.challenge.gameofthree.event.publisher;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.Serializable;

public abstract class RedisEventPublisher<T extends Serializable> extends JSONEventPublisher<T> {

    private StringRedisTemplate redisTemplate;

    public RedisEventPublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void publish(String channel, String event) {
        this.redisTemplate.convertAndSend(channel, event);
    }

}
