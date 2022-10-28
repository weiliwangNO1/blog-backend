package com.mengxuegu.blog.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务器配置类
 * @since  2022-1-26
 * @Auther: wwl
 */
@Configuration
@EnableAuthorizationServer // 开启了认证服务器
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired // 1. 数据源
    private DataSource dataSource;

    @Resource // 注入扩展器
    private TokenEnhancer jwtTokenEnhancer;

    @Bean // 1. 客户端使用 jdbc 管理
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 1. 配置被允许访问此认证服务器的客户端信息: 数据库方式
     * 如：门户客户端，后台客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
// jdbc 管理客户端
        clients.withClientDetails(jdbcClientDetailsService());
    }
    // 2. 在 SpringSecurityConfig 中添加到容器了， 密码模式需要
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Resource // token管理方式，引用 JwtTokenStoreConfig 配置的
    private TokenStore tokenStore;

    @Resource // jwt 转换器
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 关于认证服务器端点配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // password 要这个 AuthenticationManager 实例
        endpoints.authenticationManager(authenticationManager);
        // 刷新令牌时需要使用
        endpoints.userDetailsService(userDetailsService);
        // 令牌的管理方式
        endpoints.tokenStore(tokenStore).accessTokenConverter(jwtAccessTokenConverter);
        // 添加扩展器 +++++++ ++++++++++++++++++++++++++++
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancerList = new ArrayList<>();
        enhancerList.add(jwtTokenEnhancer);
        enhancerList.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancerList);
        endpoints.tokenEnhancer(enhancerChain).
                accessTokenConverter(jwtAccessTokenConverter);
    }
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // /oauth/check_token 解析令牌，默认情况 下拒绝访问
        security.checkTokenAccess("permitAll()");
    }





}
