/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.result.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yangxy.gpjava.result.entity.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import javax.annotation.Resource;

/**
 * <p>
 * 返回数据统一拦截封装
 * ResponseBodyAdvice的作用：拦截Controller方法的返回值，统一处理返回值/响应体，一般用来统一返回格式，加解密，签名等等。
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/12
 */

@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

	@Resource
	private ObjectMapper objectMapper;

	/**
	 * 注意 此处的返回值必须修改为true,否则的话并不会调用beforeBodyWrite()方法对返回信息进行封装
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		// 如果接口返回的是String，我们需要将其封装成json格式的再返回
		if (body instanceof String) {
			try {
				return objectMapper.writeValueAsString(Result.success(body));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		if (body instanceof Result) {
			return body;
		}
		return Result.success(body);
	}
}
