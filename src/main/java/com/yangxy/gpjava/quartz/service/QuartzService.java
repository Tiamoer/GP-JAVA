package com.yangxy.gpjava.quartz.service;

import com.yangxy.gpjava.log.entity.LogEntity;

public interface QuartzService {

	/**
	 * 创建日志对应的定时任务
	 * @param log 日志实体
	 */
	void createQuartzJobByLog(LogEntity log);

}
