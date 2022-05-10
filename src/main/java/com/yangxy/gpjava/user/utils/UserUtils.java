package com.yangxy.gpjava.user.utils;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 用户模块工具类
 */

@Component
public class UserUtils {

	public static String SECRET;

	/**
	 * 密码加密
	 * @param pwd 加密前的密码
	 * @param phone 用户手机号码 以手机号码为盐
	 * @return 加密后的密码
	 */
	public static String pwdEncode(String pwd, String phone) {
		if (!StringUtils.hasLength(pwd)) return "";
		byte[] salt = (phone + SECRET).getBytes();
		// 使用AES对称加密算法加密密码
		SymmetricCrypto crypto = new SymmetricCrypto(SymmetricAlgorithm.AES, salt);
		return crypto.encryptHex(pwd);
	}

	/**
	 * 密码解密
	 * @param pwd 已加密的密码
	 * @param phone 用户电话号
	 * @return 解密完成的密码
	 */
	public static String pwdDecode(String pwd, String phone) {
		byte[] salt = (phone + SECRET).getBytes();
		SymmetricCrypto crypto = new SymmetricCrypto(SymmetricAlgorithm.AES, salt);
		return crypto.encryptHex(pwd);
	}

	public String getSECRET() {
		return SECRET;
	}

	@Value("${token.secret_key}")
	public void setSECRET(String SECRET) {
		UserUtils.SECRET = SECRET;
	}
}
