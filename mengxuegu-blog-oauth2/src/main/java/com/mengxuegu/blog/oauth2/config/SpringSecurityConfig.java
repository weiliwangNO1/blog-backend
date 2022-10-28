package com.mengxuegu.blog.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 安全配置类,
 * @since  2022-1-26
 * @author wwl
 */
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService; //

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 读取用户信息进行认证
        auth.userDetailsService(userDetailsService);
    }

    @Bean // password 模式需要此 bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired // 1. 退出成功处理器 +++++++
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 关闭csrf
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf攻击
        http.formLogin() // ++
                // 成功处理器
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout() // 1. 退出成功处理器 +++++++
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .csrf().disable();
    }

}
