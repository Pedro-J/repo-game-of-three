package com.challenge.gameofthreeplayer.config;


import com.challenge.gameofthreeplayer.client.GameApiClient;
import com.challenge.gameofthreeplayer.event.receiver.impl.PlayerReceiver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

@Configuration
public class RedisConfig {

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter,
                                            @Value("${game.player.number}") String playerNumber) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("player" + playerNumber));

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(PlayerReceiver receiver) {
        return new MessageListenerAdapter(receiver, "processMessage");
    }

    @Bean
    PlayerReceiver playerReceiver(GameApiClient GameApiClient,
                                  @Value("${game.player.number}") String playerNumber,
                                  @Value("${game.maxscore}") String maxScore
    ) {
        Integer maxScoreInt = Integer.valueOf(maxScore);
        Integer playerNumberInt = Integer.valueOf(playerNumber);

        return new PlayerReceiver(GameApiClient, playerNumberInt, maxScoreInt);
    }

    @Bean
    public RestTemplate getRestClient() {
/*        final HttpComponentsClientHttpRequestFactory ClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        ClientHttpRequestFactory.setReadTimeout(5000);
        ClientHttpRequestFactory.setConnectTimeout(5000);*/
        return new RestTemplate();
    }

    @Bean
    CountDownLatch latch() {
        return new CountDownLatch(1);
    }
}
