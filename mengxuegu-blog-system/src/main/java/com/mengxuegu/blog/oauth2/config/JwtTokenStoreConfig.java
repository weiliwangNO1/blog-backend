package com.mengxuegu.blog.oauth2.config;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.util.Map;

/**
 * JWT 管理令牌，指定加密的公钥
 * @Auther: wwl
 * @since  2022-1-27
 */
@Configuration
public class JwtTokenStoreConfig {

    /**
     * 定制accessToken转换器，为额外扩展的用户信息在资源服务器中获取
     */
    class CustomAccessTokenConverter extends DefaultAccessTokenConverter {
        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
            OAuth2Authentication authentication
                    = super.extractAuthentication(claims);
            authentication.setDetails(claims);
            return authentication;
        }
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 非对称加密，资源服务器使用公钥解密 public.txt
        ClassPathResource resource = new ClassPathResource("public.txt");
        String publicKey = null;
        try {
            publicKey = IOUtils.toString(resource.getInputStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        converter.setVerifierKey(publicKey);

        // 定制 AccessToken 转换器添加扩展内容到JWT的转换器中 ++++++++++++++++
        converter.setAccessTokenConverter(new CustomAccessTokenConverter());

        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        // Jwt管理令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

}
