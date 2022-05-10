package com.yangxy.gpjava.quartz.service.impl;

import com.yangxy.gpjava.log.entity.LogEntity;
import com.yangxy.gpjava.quartz.service.QuartzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务Service实现类
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/4/18
 */

@Service("quartzService")
public class QuartzServiceImpl implements QuartzService {

	Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);

	/**
	 * 创建日志对应的定时任务
	 * @param log 日志实体
	 */
	@Override
	public void createQuartzJobByLog(LogEntity log) {
		logger.info("DO NOT ANYTHING!");
	}
}
