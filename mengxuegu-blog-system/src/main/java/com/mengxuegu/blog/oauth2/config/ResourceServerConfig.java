package com.mengxuegu.blog.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 配置资源放行条件
 * @since  2022-1-27
 * @author wwl
 */
@Configuration
@EnableResourceServer // 标识为资源服务器，请求服务中的资源，就要带着token过来，找不到token或token是无效访问不了资源
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别权限控制
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore); // JWT管理令牌
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                // 不使用也不会创建HttpSession实例，因为我们使用 token 方式
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // 授权规则配置
                // 放行 swagger-ui 相关请求
                .antMatchers("/v2/api-docs", "/v2/feign-docs",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources","/swagger-resources/configuration/security",
                        "/swagger-ui.html", "/webjars/**").permitAll()
                // 放行 /api 开头的请求
                .antMatchers("/api/**").permitAll()
                // 所有请求，都需要有all范围（scope）
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                // 其他所有请求都要先通过认证
                .anyRequest().authenticated();
    }

}
