package com.yangxy.gpjava.system.service.impl;

import ch.ethz.ssh2.Connection;
import com.yangxy.gpjava.system.bean.ExecCmdResult;
import com.yangxy.gpjava.system.service.SystemConnectService;
import com.yangxy.gpjava.system.utils.SSHUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/10
 */

@Slf4j
@Service("systemConnectService")
public class SystemConnectServiceImpl implements SystemConnectService {

	@Override
	public ExecCmdResult execCommand(String host, String userName, String password, String cmd) {

		Connection connection = SSHUtils.getConnection(host, userName, password);
		return SSHUtils.execCommand(connection, "cat cat /run/cloud-init/cloud-init-generator.log");

	}
}
