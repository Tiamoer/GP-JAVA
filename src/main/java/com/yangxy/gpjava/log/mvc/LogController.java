package com.yangxy.gpjava.log.mvc;

import ch.ethz.ssh2.Connection;
import com.yangxy.gpjava.log.dao.LogDao;
import com.yangxy.gpjava.log.dao.LogDetailDao;
import com.yangxy.gpjava.log.entity.LogEntity;
import com.yangxy.gpjava.response.bean.ResponseBean;
import com.yangxy.gpjava.response.code.ResponseCode;
import com.yangxy.gpjava.system.bean.ExecCmdResult;
import com.yangxy.gpjava.system.dao.SystemDao;
import com.yangxy.gpjava.system.entity.SystemEntity;
import com.yangxy.gpjava.system.utils.SSHUtils;
import com.yangxy.gpjava.user.entity.SlmUser;
import com.yangxy.gpjava.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志接口
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/4/19
 */

@Slf4j
@RestController
@RequestMapping("/log")
public class LogController {

	@Resource
	UserService userService;
	@Resource
	SystemDao systemDao;
	@Resource
	LogDao logDao;

	@Resource
	LogDetailDao logDetailDao;

	@GetMapping("/getLogList")
	ResponseBean<List<Map<String, Object>>> getLog(HttpServletRequest request) {
		List<Map<String, Object>> logEntities = null;
		List<Map<String, Object>> resMap = new ArrayList<>();
		try {
			SlmUser user = userService.getRequestUser(request);
			logEntities = logDao.findAllByCreateUser(user.getId());
			logEntities.forEach(map -> {
				resMap.add(new HashMap<>(){
					{
						put("name", map.get("LOG_NAME"));
						put("path", map.get("LOG_PATH"));
						put("id", map.get("ID"));
						put("system", systemDao.getById(Long.parseLong(map.get("LOG_SYSTEM").toString())).getSystemName());
					}
				});
			});
		} catch (Exception e) {
			log.error("获取日志列表异常, {}", e.getMessage());
			return ResponseBean.fail(ResponseCode.RC500, e.getMessage());
		}
		return ResponseBean.success("success!", resMap);
	}

	@PostMapping("/add")
	ResponseBean<Integer> addLog(@RequestBody Map<String, Object> res, HttpServletRequest request) {
		SlmUser user = userService.getRequestUser(request);
		String name, path;
		long system;
		try {
			name = res.get("name").toString();
			system = Long.parseLong(res.get("system").toString());
			path = res.get("path").toString();
		} catch (NumberFormatException e) {
			log.error("日志新增接口参数异常，{}", e.getMessage());
			return ResponseBean.fail(ResponseCode.RC500, e.getMessage());
		}
		try {
			LogEntity logEntity = new LogEntity();
			logEntity.setLogName(name);
			logEntity.setLogSystem(systemDao.getById(system));
			logEntity.setCreateUser(user);
			logEntity.setLogPath(path);
			logEntity.setLogRate("0 0 0 0 0");
			logDao.save(logEntity);
		} catch (Exception e) {
			log.error("日志保存时异常，{}", e.getMessage());
			return ResponseBean.fail(ResponseCode.RC500, e.getMessage());
		}
		return ResponseBean.success("success!", 1);
	}

	@GetMapping("/deleteLog")
	ResponseBean<Integer> delLog(@RequestParam("id") long id) {
		try {
			LogEntity logEntity = logDao.getById(id);
			logDao.delete(logEntity);
		} catch (Exception e) {
			log.error("日志删除失败，{}", e.getMessage());
			return ResponseBean.fail(ResponseCode.RC500, "日志删除失败，" + e.getMessage());
		}
		return ResponseBean.success("success!", 1);
	}

	@PostMapping("/editLog")
	ResponseBean<Map<String, Object>> editLog(@RequestBody Map<String, Object> res) {
		return null;
	}

	@GetMapping("/info")
	ResponseBean<Object> getLogInfo(@RequestParam("id") long logId, HttpServletRequest request) {
		//try {
		//	LogDetailEntity entity = logDetailDao.findByLogId(logId).get(0);
		//	LogEntity logEntity = entity.getLog();
		//	SystemEntity systemEntity = entity.getSystem();
		//	Map<String, Object> res = new HashMap<>();
		//	res.put("name", logEntity.getLogName());
		//	res.put("system", systemEntity.getSystemName());
		//	res.put("path", logEntity.getLogPath());
		//	res.put("info", entity.getInfo());
		//	return ResponseBean.success("success!", res);
		//} catch (Exception e) {
		//	e.printStackTrace();
		//	log.error("获取日志详情异常，{}", e.getMessage());
		//	return ResponseBean.fail(ResponseCode.RC500, e.getMessage());
		//}

		LogEntity logEntity = logDao.getById(logId);
		SystemEntity system = logEntity.getLogSystem();
		Connection connection;
		try {
			connection = SSHUtils.getConnection(system.getSystemHost(), system.getSystemUsername(), system.getSystemPassword());
		} catch (IOException e) {
			log.error("服务器连接失败，{}", e.getMessage());
			return ResponseBean.fail(ResponseCode.RC500, "服务器[" + system.getSystemName() +"]连接失败！");
		}
		String cmd = "cat " + logEntity.getLogPath();
		ExecCmdResult execCmdResult = SSHUtils.execCommand(connection, cmd);
		if (execCmdResult.isSuccess()) {
			return ResponseBean.success("日志详情获取完成", new HashMap<String, Object>(){
				{
						put("name", logEntity.getLogName());
						put("system", system.getSystemName());
						put("path", logEntity.getLogPath());
						put("info", execCmdResult.getResult());
				}
			});
		} else {
			return ResponseBean.fail(ResponseCode.RC500, "日志获取失败");
		}
	}

}
