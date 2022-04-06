/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.user.mvc;

import com.yangxy.gpjava.exception.UnauthorizedException;
import com.yangxy.gpjava.response.bean.ResponseBean;
import com.yangxy.gpjava.response.code.ResponseCode;
import com.yangxy.gpjava.token.utils.JWTUtil;
import com.yangxy.gpjava.user.dao.UserDao;
import com.yangxy.gpjava.user.entity.UserEntity;
import com.yangxy.gpjava.user.service.UserService;
import com.yangxy.gpjava.user.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * 鉴权接口
 *
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/12
 */

@RestController
@RequestMapping("/user")
public class UserController {

	static String code;

	@Resource
	private UserService userService;

	@Resource
	private UserDao userDao;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 使用密码登录接口
	 * @param req post参数
	 * @return 登陆结果
	 */
	@PostMapping("/loginByPwd")
	public ResponseBean<Map<String, String>> loginByPwd(@RequestBody Map<String, Object> req) {

		logger.info("interface:loginByPwd => {}", req);
		String phone = (String) req.get("phone");
		String password = (String) req.get("pwd");
		UserEntity user = userService.getUserByPhone(phone);

		if (user == null) {
			return ResponseBean.fail(ResponseCode.RC401, "用户 " + phone + " 不存在！");
		}

		if (user.getUserPwd().equals(UserUtils.pwdEncode(password, phone))) {
			logger.info("用户[{}]登陆成功", phone);
			return ResponseBean.success("Login successful!", new HashMap<String, String>(){
				{
					String token = JWTUtil.sign(phone);
					put("token", token);
					put("refreshToken", token);
				}
			});
		} else {
			logger.info("用户[{}]登陆失败", phone);
			return ResponseBean.fail(ResponseCode.RC401, "用户 "+phone+" 密码错误！");
		}
	}

	/**
	 * 验证码登录方式
	 * @param req 请求参数
	 * @return 登陆结果
	 */
	@PostMapping("/loginByCode")
	public ResponseBean<Map<String, String>> loginByCode(@RequestBody Map<String, Object> req) {
		logger.info("interface:loginByPwd => {}", req);
		String phone = (String) req.get("phone");
		String code = (String) req.get("code");
		UserEntity user = userService.getUserByPhone(phone);
		if (user != null && code.equals(this.code)) {
			String token = JWTUtil.sign(phone);
			return ResponseBean.success("Login successful!", new HashMap<>(){
				{
					put("token", token);
					put("refreshToken", token);
				}
			});
		} else {
			return ResponseBean.fail(ResponseCode.RC401, "用户 " + phone + " 不存在！");
		}
	}

	/**
	 * 获取用户信息
	 * @param token token
	 * @return 用户信息
	 */
	@GetMapping("/getUserInfo")
	public ResponseBean<Map<String, Object>> getUserInfo(@RequestParam("token") String token ) {

		logger.info("interface:getUserInfo => {}", token);

		String phone = JWTUtil.getUserPhone(token);
		// 校验token
		if (JWTUtil.verify(token, phone)) {
			UserEntity user = userService.getUserByPhone(phone);
			return ResponseBean.success("ok", new HashMap<String, Object>(){
				{
					put("userId", user.getId());
					put("userName", user.getUserName());
					put("userPhone", user.getUserPhone());
					put("userRole", user.getUserRole());
				}
			});
		}
		throw new UnauthorizedException();
	}

	/**
	 * 注册
	 * @param res res
	 * @return 注册是否成功
	 */
	@PostMapping("/register")
	public ResponseBean<Map<String, Object>> register(@RequestBody Map<String, Object> res) {

		String userName = res.get("name").toString();
		String phone = res.get("phone").toString();
		String pwd = res.get("pwd").toString();

		// 检查是否已存在该用户
		if (userService.getUserByPhone(phone) != null) {
			return ResponseBean.fail(ResponseCode.RC500, "用户已存在");
		}

		// 注册用户
		try {
			UserEntity user = new UserEntity();
			user.setUserName(userName);
			user.setUserPhone(phone);
			user.setUserPwd(UserUtils.pwdEncode(pwd, phone));
			// 设置默认用户角色为admin
			user.setUserRole("admin");
			userDao.saveAndFlush(user);
			return ResponseBean.success("用户"+userName+"注册成功", new HashMap<String, Object>(){
				{
					put("userName", userName);
					put("userPhone", phone);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户注册失败!");
			return ResponseBean.fail(ResponseCode.RC500, e.getMessage());
		}
	}

	/**
	 * 获取验证码
	 * @return 验证码
	 */
	@GetMapping("/getCode")
	public ResponseBean<String> getCode() {
		// 生成随机数
		int random = new Random().nextInt(10000, 99999);
		code = String.valueOf(random);
		return ResponseBean.success("Generation Complete!", String.valueOf(random));
	}

}
