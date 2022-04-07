package com.yangxy.gpjava.log.service.impl;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.yangxy.gpjava.log.service.SSHConnectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service("SSHConnectService")
public class SSHConnectServiceImpl implements SSHConnectService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 执行命令
	 * @param channelExec SSH管道
	 * @param command 执行的命令
	 * @return 执行结果
	 */
	@Override
	public List<String> execCommand(ChannelExec channelExec, String command) throws JSchException {
		InputStream inputStream = null;
		// 输出结果到字符串数组
		List<String> resultLines = new ArrayList<>();
		// 传入命令
		channelExec.setCommand("command");
		// 开始执行
		channelExec.connect();
		// 获取执行结果的输入流
		try {
			inputStream = channelExec.getInputStream();
			String result = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			while ((result = in.readLine()) != null) {
				resultLines.add(result);
				logger.info("命令行返回信息：{}", result);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return resultLines;

	}

	/**
	 * 获取SSH连接
	 *
	 * @param host     服务器地址
	 * @param port     端口
	 * @param userName 用户名称
	 * @param password 用户密码
	 * @return 返回SSH管道
	 */
	@Override
	public ChannelExec getSSHChannel(String host, int port, String userName, String password) throws JSchException {
		// 创建jsch对象
		JSch jSch = new JSch();
		ChannelExec channelExec = null;
		Session session = null;
		InputStream inputStream = null;
		// 输出结果到字符串数组
		List<String> resultLines = new ArrayList<>();
		// 创建Session会话
		session = jSch.getSession(userName, host, port);
		// 设置密码
		session.setPassword(password);
		// 创建一个session配置类
		Properties sshConfig = new Properties();
		// 跳过公钥检测
		sshConfig.put("StrictHostKeyChecking", "no");
		session.setConfig(sshConfig);
		// 建立连接
		session.connect();
		// 创建通道
		return (ChannelExec) session.openChannel("exec");
	}
}
