package com.yangxy.gpjava.system.bean;

/**
 * <p>
 * 命令执行结果返回信息封装
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/12
 */

public class ExecCmdResult {

	private boolean success;

	private String result;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
