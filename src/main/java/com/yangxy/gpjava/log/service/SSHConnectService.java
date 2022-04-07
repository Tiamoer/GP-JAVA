package com.yangxy.gpjava.log.service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;

import java.util.List;

public interface SSHConnectService {

	/**
	 * 获取SSH连接
	 * @param host 服务器地址
	 * @param port 端口
	 * @param userName 用户名称
	 * @param password 用户密码
	 * @return 返回SSH管道
	 */
	ChannelExec getSSHChannel(String host, int port, String userName, String password) throws JSchException;

	/**
	 * 执行命令
	 * @param channelExec SSH管道
	 * @param command 执行的命令
	 * @return 执行结果
	 */
	List<String> execCommand(ChannelExec channelExec, String command) throws JSchException;

}
