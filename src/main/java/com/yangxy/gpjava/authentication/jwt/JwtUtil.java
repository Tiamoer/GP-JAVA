package com.yangxy.gpjava.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yangxy.gpjava.user.entity.SlmUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Jwt工具类
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/4/20
 */


@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

	private static String EXPIRE_TIME;

	//private static final long EXPIRE_TIME = 120 * 60 * 1000; //20min
	private static final String SECRET = "password";

	public static String createToken(SlmUser user) {
		Date date = new Date(System.currentTimeMillis() + Long.parseLong(EXPIRE_TIME));
		Algorithm algorithm = Algorithm.HMAC256(SECRET);
		return JWT.create().withClaim("phone", user.getUserPhone())
				.withClaim("username", user.getUserName())
				.withExpiresAt(date).withIssuedAt(new Date()).sign(algorithm);
	}

	public static boolean verifyToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET);
			Map<String, String> info = getInfo(token);
			assert info != null;
			JWTVerifier verifier = JWT.require(algorithm).withClaim("phone", info.get("phone")).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			return true;
		} catch (IllegalArgumentException | JWTVerificationException e) {
			return false;
		}
	}

	public static Map<String, String> getInfo(String token) {
		try {
			DecodedJWT decodeJWT = JWT.decode(token);
			String phone = decodeJWT.getClaim("phone").asString();
			String username = decodeJWT.getClaim("username").asString();
			return new HashMap<String, String>(){
				{
					put("phone", phone);
					put("username", username);
				}
			};
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	public static boolean isExpire(String token) {
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getExpiresAt().getTime() < System.currentTimeMillis();
	}

	@Value("${token.expire_time}")
	public void setExpireTime(String expireTime) {
		EXPIRE_TIME = expireTime;
	}
}
