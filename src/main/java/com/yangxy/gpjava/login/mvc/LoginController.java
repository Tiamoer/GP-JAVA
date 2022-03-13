/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.login.mvc;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 鉴权接口
 *
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/12
 */

@RestController
@RequestMapping("/user")
public class LoginController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/login")
	protected String login(@RequestBody Map<String, Object> map) throws Exception {

		Subject subject = SecurityUtils.getSubject();

		// 获取用户信息
		String username = (String) map.get("username");
		String password = (String) map.get("password");

		// 封装前端传送的用户信息
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);

		try {
			subject.login(usernamePasswordToken);
			return "登陆成功！";
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			logger.info("账号【" + username + "】不存在！");
			throw new Exception("错误");
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			logger.info("账号【" + username + "】密码错误！");
			throw new Exception("错误");
		}

	}

}
