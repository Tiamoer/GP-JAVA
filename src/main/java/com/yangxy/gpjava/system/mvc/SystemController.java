package com.yangxy.gpjava.system.mvc;

import com.yangxy.gpjava.response.bean.ResponseBean;
import com.yangxy.gpjava.response.code.ResponseCode;
import com.yangxy.gpjava.system.dao.SystemDao;
import com.yangxy.gpjava.system.entity.SystemEntity;
import com.yangxy.gpjava.user.entity.SlmUser;
import com.yangxy.gpjava.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统模块Controller
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/13
 */

@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController {

	@Resource
	SystemDao systemDao;
	@Resource
	UserService userService;

	@CrossOrigin
	@PostMapping("/add")
	public ResponseBean<Object> addSystem(@RequestBody Map<String, Object> res, HttpServletRequest request) {

		if (res.isEmpty()) {
			return ResponseBean.fail(ResponseCode.RC500, "前端传递参数为空！");
		}

		// 获取当前登录用户
		SlmUser loginUser = userService.getRequestUser(request);

		String host = res.get("systemHost").toString();
		long port = Long.parseLong(res.get("systemPort").toString());
		String name = res.get("systemName").toString();
		String username = res.get("systemUsername").toString();
		String password = res.get("systemPassword").toString();

		// 判断是否已存在host
		List<SystemEntity> systemList = systemDao.findByHost(host, port, username, loginUser.getId());
		if (!systemList.isEmpty())
			return ResponseBean.fail(ResponseCode.RC500, "该服务器已存在！");
		try {
			// 保存
			SystemEntity systemEntity = new SystemEntity();
			systemEntity.setSystemHost(host);
			systemEntity.setSystemName(name);
			systemEntity.setSystemUsername(username);
			systemEntity.setSystemPassword(password);
			systemEntity.setSystemPort(port);
			systemEntity.setCreateUser(loginUser);
			systemDao.saveAndFlush(systemEntity);
		} catch (Exception e) {
			log.error("保存系统信息失败，{}", e.getMessage());
			e.printStackTrace();
			return ResponseBean.fail(ResponseCode.RC500, "系统信息保存失败，异常信息：" + e.getMessage());
		}
		return ResponseBean.success("系统信息保存完成", 1);
	}

	@CrossOrigin
	@GetMapping("/getSystemList")
	public ResponseBean<List<Map<String, Object>>> getSystemList(HttpServletRequest request) {
		// 获取当前登录用户
		SlmUser loginUser = userService.getRequestUser(request);
		log.info("获取用户{}的系统列表!", loginUser.getUserName());
		// 获取当前用户的System列表
		List<Map<String, Object>> list = systemDao.findAllByCreateUser(loginUser.getId());
		return ResponseBean.success("success!", list);
	}

	@CrossOrigin
	@GetMapping("/getSystemInfo")
	public ResponseBean<List<Map<String, Object>>> getSystemInfo(@RequestParam("sysId") long sysId) {
		log.info("获取SystemId为{}的系统信息！", sysId);
		List<Map<String, Object>> list = systemDao.findOneById(sysId);
		return ResponseBean.success("success!", list);
	}

	@CrossOrigin
	@PostMapping("/edit")
	public ResponseBean<Object> editSystemInfo(@RequestBody Map<String, Object> res) {
		long id;
		String sysName;
		String sysHost;
		long sysPort;
		String username;
		String password;
		try {
			id = Long.parseLong(res.get("ID").toString());
			sysName = (String) res.get("SYSTEM_NANE");
			sysHost = (String) res.get("SYSTEM_HOST");
			sysPort = Integer.parseInt(res.get("SYSTEM_PORT").toString());
			username = (String) res.get("SYSTEM_USERNAME");
			password = (String) res.get("SYSTEM_PASSWORD");
		} catch (Exception e) {
			log.error("解析前端POST数据失败！{}", e.getMessage());
			return ResponseBean.fail(ResponseCode.RC500, e.getMessage());
		}
		int code;
		try {
			code = systemDao.updateSystem(sysName, sysHost, sysPort, username, password, id);
		} catch (Exception e) {
			log.error("保存修改后的System信息失败！{}", e.getMessage());
			return ResponseBean.fail(ResponseCode.RC500, e.getMessage());
		}
		return ResponseBean.success("success!", code);
	}

	@CrossOrigin
	@GetMapping("/delete")
	public ResponseBean<Object> deleteSystem(@RequestParam("sysId") long sysId) {
		log.info("删除SystemId为{}的系统信息！", sysId);
		try {
			SystemEntity system = systemDao.getById(sysId);
			systemDao.delete(system);
		} catch (Exception e) {
			log.error("删除系统信息失败！{}", e.getMessage());
			return ResponseBean.fail(ResponseCode.RC500, e.getMessage());
		}
		return ResponseBean.success("success!", 1);
	}

}
