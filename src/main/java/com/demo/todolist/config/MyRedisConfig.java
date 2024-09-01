package com.demo.todolist.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configurable
public class MyRedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        var config = new RedisStandaloneConfiguration();
        config.setHostName("your-redis-server-host");
        config.setPort(6600);
        config.setPassword("1234");
        return new LettuceConnectionFactory(config);
    }
}
