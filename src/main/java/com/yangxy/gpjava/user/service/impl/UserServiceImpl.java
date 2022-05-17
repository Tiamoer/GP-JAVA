/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.user.service.impl;

import com.yangxy.gpjava.authentication.jwt.JwtUtil;
import com.yangxy.gpjava.user.dao.UserDao;
import com.yangxy.gpjava.user.entity.SlmUser;
import com.yangxy.gpjava.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/13
 */

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	UserDao userDao;

	@Override
	public SlmUser getUserByName(String username) {
		return userDao.getUserByName(username);
	}

	@Override
	public SlmUser getUserByPhone(String phone) {
		return userDao.getUserByPhone(phone);
	}

	@Override
	public SlmUser getRequestUser(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		// 根据token获取用户手机号码
		Map<String, String> userInfo = JwtUtil.getInfo(token);
		return userDao.getUserByPhone(userInfo.get("phone"));
	}
}
