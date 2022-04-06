package com.yangxy.gpjava.response.code;

public enum ResponseCode {

	RC200(200, "操作成功"),
	RC500(500, "服务器内部异常"),
	RC401(401, "匿名用户访问无权限资源时的异常");


	/**
	 * 自定义状态码
	 **/
	private final int code;
	/**
	 * 自定义描述
	 **/
	private final String message;

	ResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	}
