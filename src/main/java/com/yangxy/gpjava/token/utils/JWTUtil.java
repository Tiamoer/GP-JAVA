/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.token.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * Token工具类
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/19
 */

@Component
public class JWTUtil {

	public static long EXPIRE_TIME;
	public static String SECRET;

	/**
	 * 校验Token准确性
	 * @param token token
	 * @param phone 用户名
	 * @return boolean
	 */
	public static boolean verify(String token, String phone) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET);
			JWTVerifier verifier = JWT.require(algorithm).withClaim("phone",phone).build();
			DecodedJWT jwt = verifier.verify(token);
			return true;
		} catch (IllegalArgumentException | JWTVerificationException e) {
			return false;
		}
	}

	/**
	 * 从token中获取用户电话号
	 * @param token token
	 * @return 用户电话号
	 */
	public static String getUserPhone(String token) {
		try {
			DecodedJWT decodeJWT = JWT.decode(token);
			return decodeJWT.getClaim("phone").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 签发token
	 * @param phone 用户电话号
	 * @return token
	 */
	public static String sign(String phone) {
		Date time = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(SECRET);
		return JWT.create().withClaim("phone", phone).withExpiresAt(time).sign(algorithm);
	}

	public long getExpireTime() {
		return EXPIRE_TIME;
	}

	@Value("${token.expire_time}")
	public void setExpireTime(String expireTime) {
		EXPIRE_TIME = Long.parseLong(expireTime);
	}

	public String getSECRET() {
		return SECRET;
	}

	@Value("${token.secret_key}")
	public void setSECRET(String SECRET) {
		JWTUtil.SECRET = SECRET;
	}
}
