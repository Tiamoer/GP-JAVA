package com.yangxy.gpjava.authentication.shiro;

import com.yangxy.gpjava.authentication.jwt.JwtUtil;
import com.yangxy.gpjava.authentication.jwt.Token;
import com.yangxy.gpjava.user.service.UserService;
import com.yangxy.gpjava.user.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

	@Value("${token.expire_time}")
	String EXPIRE_TIME;

	@Resource
	RedisUtil redisUtil;

	@Resource
	UserService userService;

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
			if (redisUtil.hasKey(phone+token)) {
				// 说明只是请求头里面的token过期了，此时续签redis中的token
				String newToken = JwtUtil.createToken(userService.getUserByPhone(phone));
				redisUtil.set(phone+token, newToken, Long.parseLong(EXPIRE_TIME)*2);
			} else {
				log.info("token 已过期！");
				throw new AuthenticationException("token 已过期！");
			}
		}
		if (phone == null || !JwtUtil.verifyToken(token)) {
			log.info("token 校验失败！");
			throw new UnauthenticatedException("Token 校验失败!");
		}

		return new SimpleAuthenticationInfo(token, token, "ShiroRealm");
	}
}
