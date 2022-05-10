package com.yangxy.gpjava.authentication.jwt;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.json.JSONUtil;
import com.yangxy.gpjava.response.bean.ResponseBean;
import com.yangxy.gpjava.response.code.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.naming.AuthenticationException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 创建继承自AuthenticatingFilter的过滤器，这里需要做三件事
 * 1.从当前Ruequest获取token，从而创建AuthenticationToken对象
 * 2.在onAccessDenied方法，校验Token的有效性
 * 3.最后执行登录操作，这里的登录操作其实是用token换取用户信息，会执行AuthorizingRealm的doGetAuthenticationInfo方法
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/4/20
 */

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

	private boolean allowOrigin = true;

	public JwtFilter(){}
	public JwtFilter(boolean allowOrigin) {
		this.allowOrigin = allowOrigin;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		log.info("进入 JwtFilter -> isAccessAllowed()");
		try {
			executeLogin(request, response);
		} catch (Exception e) {
			try {
				responseError(response);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		return true;
	}

	@Override
	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
		HttpServletRequest req = (HttpServletRequest) request;
		String token = req.getHeader("slm-token");
		return token != null;
	}

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) {
		log.info("进入 JwtFilter -> executeLogin()");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader("slm-token");
		Token myToken = new Token(token);
		// 提交给realm进行登陆操作
		getSubject(request, response).login(myToken);
		return true;
	}

	/**
	 * 提供跨域支持
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		//前后端分离，shiro过滤器配置引起的跨域问题
		// 是否允许发送Cookie，默认Cookie不包括在CORS请求之中。设为true时，表示服务器允许Cookie包含在请求中。
		httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
		//前后端分离，shiro过滤器配置引起的跨域问题
		// 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(HttpStatus.OK.value());
			return false;
		}
		return super.preHandle(request, response);
	}

	private void responseError(ServletResponse response) throws IOException {
		HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json;charset=UTF-8");
		ResponseBean<Object> responseBean = ResponseBean.fail(ResponseCode.RC401, "dddd！");
		//OutputStream outputStream = httpServletResponse.getOutputStream();
		//outputStream.write(JSONUtil.toJsonStr(responseBean).getBytes(StandardCharsets.UTF_8));
		httpServletResponse.getOutputStream().print(JSONUtil.toJsonStr(responseBean));
	}
}
