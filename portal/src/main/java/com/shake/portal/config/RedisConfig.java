package com.shake.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author: Shake
 * @Description RedisConfig Redis 序列化的工具配置類 配置redisTemplate Bean 取代默認導入的Bean 實現自定義配置
 * @Date 2023/11/30
 */
@Configuration
public class RedisConfig {

    /**
     *  this.redisTemplate.opsForValue(); 提供操作String類型的所有方法
     *  this.redisTemplate.opsForList(); 提供操作list類型的所有方法
     *  this.redisTemplate.opsForSet(); 提供操作set的所有方法
     *  this.redisTemplate.opsForHash(); 提供操作hash表的所有方法
     *  this.redisTemplate.opsForZSet(); 提供操做zset的所有方法
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 設置key序列化方式String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 設置value序列化方式json
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}
