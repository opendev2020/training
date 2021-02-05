package edu.cust;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.cust.login.MyFormAuthenticationFilter;

/**
 * Created by qh on 2017/4/9.
 */
@Configuration
public class FilterConfig {
	@Bean
	public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
		FilterRegistrationBean<XssFilter> fb = new FilterRegistrationBean<>();
		fb.setFilter(new XssFilter());
		fb.setOrder(1);
		fb.setEnabled(true);
		fb.addUrlPatterns("/*");
		HashMap<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("excludes", "/favicon.ico,/y/.*");
		initParameters.put("isIncludeRichText", "true");
		fb.setInitParameters(initParameters);
		return fb;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		Map<String, Filter> filters = new LinkedHashMap<>();
		filters.put("authc", authc());
		filters.put("authcAdm", authcAdm());
		shiroFilterFactoryBean.setFilters(filters);
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		filterChainDefinitionMap.put("/usr/**", "authc");
		filterChainDefinitionMap.put("/adm/**", "authcAdm");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public Filter authc() {
		MyFormAuthenticationFilter filter = new MyFormAuthenticationFilter();
		//filter.setTableName("c_user");
		filter.setLoginUrl("/usr/login");
		return filter;
	}

	@Bean
	public Filter authcAdm() {
		MyFormAuthenticationFilter filter = new MyFormAuthenticationFilter();
		//filter.setTableName("c_adm");
		filter.setLoginUrl("/adm/login");
		return filter;
	}

}
