package com.yangxy.gpjava.system.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.yangxy.gpjava.system.bean.ExecCmdResult;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * SSH2工具类
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/5/10
 */

@Slf4j
public class SSHUtils {

	/**
	 * 获取服务器连接
	 * @param hostname 服务器地址
	 * @param username 服务器用户名称
	 * @param password 服务器密码
	 * @return 服务器连接Connection
	 */
	public static Connection getConnection(String hostname, String username, String password) {
		Connection connection = new Connection(hostname);
		try {
			connection.connect();
			boolean login = connection.authenticateWithPassword(username, password);
			if (login) {
				return connection;
			} else {
				throw new RuntimeException("服务器用户名称及密码错误！");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 执行一条命令
	 * @param connection ssh2 连接对象
	 * @param command 执行的命令
	 * @return ExecCmdResult 执行结果
	 */
	public static ExecCmdResult execCommand(Connection connection, String command) {
		ExecCmdResult execCmdResult = new ExecCmdResult();
		Session session = null;
		try {
			session = connection.openSession();
			// 执行命令
			session.execCommand(command);
			// 解析结果
			String result = parseResult(session.getStdout());
			// 如果解析结果为空，则表示执行命令发生了错误，尝试解析错误输出
			if (result.isEmpty()) {
				result = parseResult(session.getStderr());
				execCmdResult.setSuccess(false);
			} else {
				execCmdResult.setSuccess(true);
			}
			execCmdResult.setResult(result);
			return execCmdResult;
		} catch (IOException e) {
			log.error("EXEC命令执行出错,{}", e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
		return null;

	}

	/**
	 * 解析执行结果
	 * @param inputStream 执行结果的流
	 * @return 执行结果String
	 */
	private static String parseResult(InputStream inputStream) throws IOException {
		// 读取输出流内容
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		StringBuilder resultSb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			resultSb.append(line).append("\n");
		}
		return resultSb.toString();
	}

}
