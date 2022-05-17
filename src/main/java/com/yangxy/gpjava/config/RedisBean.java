package com.yangxy.gpjava.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <p>
 *
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/16
 */

@Slf4j
@Component
public class RedisBean {
	@Autowired
	private RedisTemplate redisTemplate;

	public static RedisTemplate redis;

	@PostConstruct
	public void getRedisTemplate() {
		redis = this.redisTemplate;
	}

}
