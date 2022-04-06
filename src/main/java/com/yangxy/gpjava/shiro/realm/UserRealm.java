/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.shiro.realm;

import com.yangxy.gpjava.token.ShiroToken;
import com.yangxy.gpjava.token.utils.JWTUtil;
import com.yangxy.gpjava.user.entity.UserEntity;
import com.yangxy.gpjava.user.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/19
 */

@Component
public class UserRealm extends AuthorizingRealm {

	@Resource
	UserService userService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof ShiroToken;
	}

	/**
	 * 鉴权
	 * @param principals 身份信息集合
	 * @return simpleAuthorizationInfo
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String phone = JWTUtil.getUserPhone(principals.toString());
		UserEntity user = userService.getUserByPhone(phone);
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addRole(user.getUserRole());
		Set<String> permission = new HashSet<>(Arrays.asList(user.getUserRole().split(",")));
		simpleAuthorizationInfo.addStringPermissions(permission);
		return simpleAuthorizationInfo;
	}

	/**
	 * 认证
	 * @param authenticationToken token
	 * @return 认证信息
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		String token = authenticationToken.getCredentials().toString();
		String phone = JWTUtil.getUserPhone(token);
		if (phone == null) {
			throw new AuthenticationException("token invalid");
		}
		UserEntity user = userService.getUserByPhone(phone);
		if (user == null) {
			throw new AuthenticationException("User didn't existed!");
		}
		if (! JWTUtil.verify(token, phone)) {
			throw new AuthenticationException("Username or password error");
		}
		return new SimpleAuthenticationInfo(token, token, "user_realm");
	}
}
