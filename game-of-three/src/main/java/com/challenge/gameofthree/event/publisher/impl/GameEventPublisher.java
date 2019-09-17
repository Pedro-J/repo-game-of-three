package com.challenge.gameofthree.event.publisher.impl;

import com.challenge.gameofthree.event.GameEvent;
import com.challenge.gameofthree.event.publisher.RedisEventPublisher;
import com.challenge.gameofthree.exception.EventPublishExpcetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameEventPublisher extends RedisEventPublisher<GameEvent> {

    @Autowired
    public GameEventPublisher(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void publish(GameEvent event) throws EventPublishExpcetion {
        publish(getChannel(event), event);
    }

    private String getChannel(GameEvent event) {
        return "player" + event.getPlayer();
    }

}
