package com.yangxy.gpjava.system.service;

import com.yangxy.gpjava.system.bean.ExecCmdResult;

/**
 * <p>
 *
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/10
 */

public interface SystemConnectService {


	ExecCmdResult execCommand(String host, String userName, String password, String cmd);

	

}
