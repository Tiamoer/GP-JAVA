package com.yangxy.gpjava.authentication.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * <p>
 * AuthenticationToken的实现类，存放我们的Token信息
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/4/20
 */

public class Token implements AuthenticationToken {

	private String token;

	public Token(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}
