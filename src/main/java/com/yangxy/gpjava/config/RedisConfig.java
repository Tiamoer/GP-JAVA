package com.yangxy.gpjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 * Redis配置文件
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/16
 */

@Configuration
public class RedisConfig {
	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
		redisTemplate.setConnectionFactory(factory);

		// key序列化
		redisTemplate.setKeySerializer(redisSerializer);
		// value序列化
		redisTemplate.setValueSerializer(redisSerializer);
		// key hashmap序列化
		redisTemplate.setHashKeySerializer(redisSerializer);
		// value hashmap序列化
		redisTemplate.setHashValueSerializer(redisSerializer);
		return redisTemplate;
	}
}
