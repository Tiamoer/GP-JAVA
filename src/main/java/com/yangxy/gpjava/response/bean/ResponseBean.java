/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.response.bean;

import com.yangxy.gpjava.response.code.ResponseCode;

/**
 * <p>
 * 统一接口返回格式
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/12
 */

public class ResponseBean<T> {

	private int code;
	private String message;
	private T data;

	public ResponseBean(int code, String msg, T data) {
		this.code = code;
		message = msg;
		this.data = data;
	}

	public static <T> ResponseBean<T> success(String message, T data) {
		return new ResponseBean<T>(ResponseCode.RC200.getCode(), message, data);
	}

	public static <T> ResponseBean<T> fail(ResponseCode code, String msg) {
		return new ResponseBean<T>(code.getCode(), msg, null);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
