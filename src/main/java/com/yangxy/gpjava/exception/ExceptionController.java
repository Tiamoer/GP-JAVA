/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * Rest响应拦截器
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/19
 */

@RestControllerAdvice
public class ExceptionController {

	///**
	// * Shiro异常拦截封装
	// * @param e shiro异常
	// * @return ResponseBean.fail
	// */
	//@ResponseStatus(HttpStatus.UNAUTHORIZED)
	//@ExceptionHandler(ShiroException.class)
	//public ResponseBean<Object> handle401(ShiroException e) {
	//	e.printStackTrace();
	//	return ResponseBean.fail(ResponseCode.RC401, e.getMessage());
	//}
	//
	///**
	// * 授权异常
	// * @return ResponseBean.fail
	// */
	//@ResponseStatus(HttpStatus.UNAUTHORIZED)
	//@ExceptionHandler(UnauthorizedException.class)
	//public ResponseBean<Object> handle401() {
	//	return ResponseBean.fail(ResponseCode.RC401, "Unauthorized");
	//}
	//
	///**
	// * 控制器全局异常处理
	// * @return ResponseBean.fail
	// */
	//@ResponseStatus(HttpStatus.BAD_REQUEST)
	//@ExceptionHandler(Exception.class)
	//public ResponseBean<Object> handleException(HttpServletRequest request, Throwable throwable) {
	//	throwable.printStackTrace();
	//	return new ResponseBean<>(getStatus(request).value(),throwable.getMessage(), null);
	//}
	//
	//@ExceptionHandler(UnauthenticatedException.class)
	//public ResponseBean<Object> handleUnauthenticated(UnauthorizedException e) {
	//	return ResponseBean.fail(ResponseCode.RC500, e.getMessage());
	//}
	//
	//private HttpStatus getStatus(HttpServletRequest request) {
	//	Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
	//	if (statusCode == null) {
	//		return HttpStatus.INTERNAL_SERVER_ERROR;
	//	}
	//	return HttpStatus.valueOf(statusCode);
	//}

}
