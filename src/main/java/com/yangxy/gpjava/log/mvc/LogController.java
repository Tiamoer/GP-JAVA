package com.yangxy.gpjava.log.mvc;

import com.yangxy.gpjava.response.bean.ResponseBean;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 日志接口
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/4/19
 */

@RestController
@RequestMapping("/log")
public class LogController {

	@GetMapping("/getLog")
	ResponseBean<Map<String, Object>> getLog(int logId) {
		return null;
	}

	@PostMapping("/addLog")
	ResponseBean<Map<String, Object>> addLog(@RequestBody Map<String, Object> res) {
		return null;
	}

	@GetMapping("/delLog")
	ResponseBean<Map<String, Object>> delLog(int logId) {
		return null;
	}

	@PostMapping("/editLog")
	ResponseBean<Map<String, Object>> editLog(@RequestBody Map<String, Object> res) {
		return null;
	}

}
