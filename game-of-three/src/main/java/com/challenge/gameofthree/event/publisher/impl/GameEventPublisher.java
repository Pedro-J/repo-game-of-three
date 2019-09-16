package com.challenge.gameofthree.event.publisher.impl;

import com.challenge.gameofthree.event.GameEvent;
import com.challenge.gameofthree.event.publisher.RedisEventPublisher;
import com.challenge.gameofthree.exception.EventPublishExpcetion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameEventPublisher extends RedisEventPublisher<GameEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameEventPublisher.class);

    @Autowired
    public GameEventPublisher(StringRedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void publish(GameEvent event) throws EventPublishExpcetion {
        LOGGER.info(String.format("Seding message to channel %s in game %s", getChannel(event), event.getGameId() ));
        publish(getChannel(event), event);
    }

    private String getChannel(GameEvent event) {
        return "player" + event.getPlayer();
    }

}
