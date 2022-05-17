package com.yangxy.gpjava.authentication.shiro;

import com.yangxy.gpjava.authentication.jwt.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Shiro配置类
 * @author <a href="mailto:tiamoer@outlook.com">yangxy</a>
 * @version 1.0, 2022/4/20
 */

@Slf4j
@Configuration
public class ShiroConfig {

	/**
	 * 先通过token过滤器，如果请求头存在token，则用token去login，接着realm
	 */
	@Bean(name = "shiroFilterFactoryBean")
	public ShiroFilterFactoryBean factory(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
		log.info("进入 ShiroConfig -> factoryBean()");
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager);
		factoryBean.setUnauthorizedUrl("/");
		// 配置过滤器
		Map<String, Filter> filterMap = new HashMap<>();
		// 添加自己的过滤器并取名为jwt
		filterMap.put("jwt", new JwtFilter());
		factoryBean.setFilters(filterMap);
		// 拦截规则
		Map<String, String> filterRuleMap = new HashMap<>();
		// 添加不需要拦截的url
		filterRuleMap.put("/user/loginByPwd", "anon");
		filterRuleMap.put("/user/loginByCode", "anon");
		filterRuleMap.put("/user/register", "anon");
		filterRuleMap.put("/user/getSmsCode", "anon");
		filterRuleMap.put("/user/getCode", "anon");
		filterRuleMap.put("/user/idLogin", "anon");
		//filterRuleMap.put("/user/getUserInfo", "anon");
		// 所有请求通过JWT Filter
		filterRuleMap.put("/**", "jwt");
		factoryBean.setFilterChainDefinitionMap(filterRuleMap);
		return factoryBean;
	}


	@Bean(name = "securityManager")
	public DefaultWebSecurityManager securityManager(ShiroRealm realm) {
		log.info("进入 ShiroConfig -> securityManager()");
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置自定义的realm
		securityManager.setRealm(realm);
		// 关闭session
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);
		return securityManager;
	}
	/**
	 * 添加注解支持
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		// 强制使用cglib，防止重复代理和可能引起代理出错的问题
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
}
