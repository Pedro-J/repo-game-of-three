package com.challenge.gameofthreeplayer.config;


import com.challenge.gameofthreeplayer.client.GameApiClient;
import com.challenge.gameofthreeplayer.event.receiver.impl.PlayerReceiver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisMessageListenerContainer redisContainer(MessageListenerAdapter messageListener, ChannelTopic topic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener, topic);
        return container;
    }

    @Bean
    MessageListenerAdapter messageListener(PlayerReceiver receiver) {
        return new MessageListenerAdapter(receiver, "processMessage");
    }

    @Bean
    PlayerReceiver playerReceiver(GameApiClient GameApiClient,
                                  @Value("${game.player.number}") String playerNumber,
                                  @Value("${game.play.auto}") boolean auto) {
        Integer playerNumberInt = Integer.valueOf(playerNumber);
        return new PlayerReceiver(GameApiClient, playerNumberInt, auto);
    }

    @Bean
    ChannelTopic topic(@Value("${game.player.number}") String playerNumber) {
        return new ChannelTopic("player" + playerNumber);
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
