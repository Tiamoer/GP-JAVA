/*
 *	Copyright 2013-2022 Smartdot Technologies Co., Ltd. All rights reserved.
 *	SMARTDOT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.yangxy.gpjava.user.mvc;

import com.yangxy.gpjava.authentication.jwt.JwtUtil;
import com.yangxy.gpjava.exception.UnauthorizedException;
import com.yangxy.gpjava.response.bean.ResponseBean;
import com.yangxy.gpjava.response.code.ResponseCode;
import com.yangxy.gpjava.user.dao.UserDao;
import com.yangxy.gpjava.user.entity.SlmUser;
import com.yangxy.gpjava.user.service.UserService;
import com.yangxy.gpjava.user.utils.RedisUtil;
import com.yangxy.gpjava.user.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * <p>
 * 鉴权接口
 *
 * @author <a href="mailto:yangxy@smartdot.com.cn">yangxy</a>
 * @version 1.0, 2022/3/12
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

	static String code;

	@Resource
	private UserService userService;

	@Resource
	private UserDao userDao;

	@Resource
	private RedisUtil redisUtil;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${token.expire_time}")
	String EXPIRE_TIME;

	/**
	 * 使用密码登录接口
	 * @param req post参数
	 * @return 登陆结果
	 */
	@CrossOrigin
	@PostMapping("/loginByPwd")
	public ResponseBean<Map<String, String>> loginByPwd(@RequestBody Map<String, Object> req) {

		logger.info("interface:loginByPwd => {}", req);
		String phone = (String) req.get("phone");
		String password = (String) req.get("pwd");
		Assert.notNull(phone, "用户手机号码不能为空！");
		Assert.notNull(password, "用户密码不能为空！");
		SlmUser user = userService.getUserByPhone(phone);

		if (user == null) {
			return ResponseBean.fail(ResponseCode.RC401, "用户 " + phone + " 不存在！");
		}

		if (user.getUserPwd().equals(UserUtils.pwdEncode(password, phone))) {
			logger.info("用户[{}]登陆成功", phone);
			String token = JwtUtil.createToken(user);
			// 设置redis中的token
			redisUtil.set(user.getUserPhone()+token, token, Long.parseLong(EXPIRE_TIME) * 2);
			return ResponseBean.success("Login successful!", new HashMap<String, String>(){
				{
					put("token", token);
					put("refreshToken", token);
				}
			});
		} else {
			logger.info("用户[{}]登陆失败", phone);
			return ResponseBean.fail(ResponseCode.RC500, "用户 "+phone+" 密码错误！");
		}
	}

	/**
	 * 验证码登录方式
	 * @param req 请求参数
	 * @return 登陆结果
	 */
	@CrossOrigin
	@PostMapping("/loginByCode")
	public ResponseBean<Map<String, String>> loginByCode(@RequestBody Map<String, Object> req) {
		logger.info("interface:loginByPwd => {}", req);
		String phone = (String) req.get("phone");
		String code = (String) req.get("code");
		SlmUser user = userService.getUserByPhone(phone);
		if (user != null && code.equals(UserController.code)) {
			String token = JwtUtil.createToken(user);
			return ResponseBean.success("Login successful!", new HashMap<>(){
				{
					put("token", token);
					put("refreshToken", token);
				}
			});
		} else if (user == null) {
			return ResponseBean.fail(ResponseCode.RC500, "用户 " + phone + " 不存在！");
		} else {
			return ResponseBean.fail(ResponseCode.RC500, "验证码 " + code + " 错误！");
		}
	}

	/**
	 * 获取用户信息
	 * @param token token
	 * @return 用户信息
	 */
	@CrossOrigin
	@GetMapping("/getUserInfo")
	public ResponseBean<Map<String, Object>> getUserInfo(@RequestParam String token) {
		logger.info("interface:getUserInfo => {}", token);
		String phone = Objects.requireNonNull(JwtUtil.getInfo(token)).get("phone");
		// 校验token
		if (JwtUtil.verifyToken(token)) {
			SlmUser user = userService.getUserByPhone(phone);
			return ResponseBean.success("ok", new HashMap<String, Object>(){
				{
					put("userId", user.getId());
					put("userName", user.getUserName());
					put("userPhone", user.getUserPhone());
					put("userRole", user.getUserRole());
				}
			});
		}
		log.info("Token 校验未通过");
		throw new UnauthorizedException();
	}

	/**
	 * 注册
	 * @param res res
	 * @return 注册是否成功
	 */
	@CrossOrigin
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
			SlmUser user = new SlmUser();
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
	 * 修改用户信息
	 * @param res 参数
	 * @return 是否成功修改用户信息
	 */
	@CrossOrigin
	@PostMapping("/changeUserInfo")
	public ResponseBean<Map<String, Object>> changeUserInfo(@RequestBody Map<String, Object> res) {

		String username = res.get("name").toString();
		String phone = res.get("phone").toString();
		String oldPwd = res.get("oldPwd").toString();
		String newPwd = res.get("newPwd").toString();

		// 通过电话号获取用户
		SlmUser user = userService.getUserByPhone(phone);
		// 虽然这种情况基本不可能存在，但是为了以防万一还是加上好
		if (user == null) {
			return ResponseBean.fail(ResponseCode.RC500, "用户不存在！");
		}
		if (Objects.equals(UserUtils.pwdEncode(oldPwd, phone), user.getUserPwd())) {
			user.setUserName(username);
			user.setUserPwd(UserUtils.pwdEncode(newPwd, phone));
			userDao.saveAndFlush(user);
			return ResponseBean.success("ok", new HashMap<String, Object>(){
				{
					put("userId", user.getId());
					put("userName", user.getUserName());
					put("userPhone", user.getUserPhone());
					put("userRole", user.getUserRole());
				}
			});
		} else {
			return ResponseBean.fail(ResponseCode.RC401, "旧密码错误！");
		}
	}

	/**
	 * 生成验证码
	 * @return 验证码
	 */
	@CrossOrigin
	@PostMapping("/getSmsCode")
	public ResponseBean<String> getSmsCode() {
		// 生成随机数
		int random = new Random().nextInt(100000, 999999);
		code = String.valueOf(random);
		logger.info("验证码 = {}", code);
		return ResponseBean.success("Generation Complete!", "code");
	}

	/**
	 * 获取验证码
	 * @return 验证码
	 */
	@CrossOrigin
	@GetMapping("/getCode")
	public ResponseBean<String> getCode() {
		return ResponseBean.success("Get Code!", code);
	}

	/**
	 * 用户是否登录 1：已登陆， -1：Token过期， 0：token校验失败
	 * @param request 请求
	 * @return Integer
	 */
	@CrossOrigin
	@GetMapping("/isLogin")
	public ResponseBean<Integer> isLogin(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		// 校验Token
		if(JwtUtil.verifyToken(token)) {
			// 获取用户
			SlmUser loginUser = userService.getRequestUser(request);
			if (JwtUtil.isExpire(token)) {
				// 从redis里取token，如果有，就续签token，如果没有则用户Token过期，重新登录
				if (redisUtil.hasKey(loginUser.getUserPhone() + token)) {
					String newToken = JwtUtil.createToken(loginUser);
					redisUtil.set(loginUser.getUserPhone()+token, newToken, Long.parseLong(EXPIRE_TIME)*2);
					return ResponseBean.success("success!", 1);
				} else {
					return ResponseBean.success("fail", -1);
				}
			} else {
				return ResponseBean.success("success!", 1);
			}
		} else {
			return ResponseBean.success("fail", 0);
		}
	}

}
