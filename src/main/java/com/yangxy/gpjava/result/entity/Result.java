/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.result.entity;

import com.yangxy.gpjava.result.code.ReturnCode;

/**
 * <p>
 * 统一接口返回格式
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/12
 */

public class Result<T> {

	private int status;
	private String message;
	private T data;
	private long timestamp;

	public Result (){
		this.timestamp = System.currentTimeMillis();
	}

	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<>();
		result.setStatus(ReturnCode.RC100.getCode());
		result.setMessage(ReturnCode.RC100.getMessage());
		result.setData(data);
		return result;
	}

	public static <T> Result<T> fail(int code, String message) {
		Result<T> result = new Result<>();
		result.setStatus(code);
		result.setMessage(message);
		return result;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
