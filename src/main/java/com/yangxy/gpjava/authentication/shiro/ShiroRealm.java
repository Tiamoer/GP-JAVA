package com.yangxy.gpjava.authentication.shiro;

import com.yangxy.gpjava.authentication.jwt.JwtUtil;
import com.yangxy.gpjava.authentication.jwt.Token;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 *
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/4/20
 */

@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof Token;
	}

	/**
	 * 鉴权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		return null;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		log.info("进入 ShiroRealm -> doGetAuthenticationInfo() 认证");
		String token = (String) authenticationToken.getCredentials();
		String phone = null;
		try {
			phone = Objects.requireNonNull(JwtUtil.getInfo(token)).get("phone");
		} catch (Exception e) {
			log.info("token 非法！");
			throw new UnauthenticatedException("Token 非法！");
		}

		if (JwtUtil.isExpire(token)) {
			log.info("token 已过期！");
			throw new UnknownAccountException("Token 已过期！");
		}

		if (phone == null || !JwtUtil.verifyToken(token)) {
			log.info("token 校验失败！");
			throw new UnauthenticatedException("Token 校验失败!");
		}

		return new SimpleAuthenticationInfo(token, token, "ShiroRealm");
	}
}
