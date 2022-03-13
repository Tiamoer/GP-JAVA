/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.result.error;

import com.yangxy.gpjava.result.code.ReturnCode;
import com.yangxy.gpjava.result.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 全局异常处理器
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/12
 */

@RestControllerAdvice
public class RestExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<String> exception(Exception e) {
		logger.error("全局异常信息 ex={}", e.getMessage(), e);
		return Result.fail(ReturnCode.RC500.getCode(), e.getMessage());
	}

}
