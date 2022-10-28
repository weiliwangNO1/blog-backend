package com.mengxuegu.blog.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * jwt令牌管理
 * @since  2022-1-26
 * @author wwl
 */
@Configuration
public class JwtTokenStoreConfig {

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 采用非对称加密jwt
        // 第1个参数就是密钥证书文件，第2个参数 密钥库口令, 私钥进行签名
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(
                new ClassPathResource("oauth2.jks"), "oauth2".toCharArray());
        converter.setKeyPair(factory.getKeyPair("oauth2"));
        return converter;
    }

    @Resource // 不要少了
    private RedisTemplate redisTemplate;

    /**
     * 使用redis存储jwt
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        // Jwt管理令牌
        return new JwtTokenStore(jwtAccessTokenConverter()) { // JwtTokenStore匿名类中重写方法
            // 存储到redis ++++++++++
            @Override
            public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication
                    authentication) {
                // 将 Jwt 的唯一标识 jti 为 Key 存入 redis 中，并保持与原Jwt有一致的时效性
                if (token.getAdditionalInformation().containsKey("jti")) {
                    String jti = token.getAdditionalInformation().get("jti").toString();
                    // （key，value，有效时间，时间单位秒）
                    redisTemplate.opsForValue().set(
                            jti, token.getValue(), token.getExpiresIn(), TimeUnit.SECONDS);
                }
                super.storeAccessToken(token, authentication);
            }
            // 删除redis的token ++++++++++
            @Override
            public void removeAccessToken(OAuth2AccessToken token) {
                if (token.getAdditionalInformation().containsKey("jti")) {
                    // 通过 Jwt 的唯一标识 jti 为 Key 删除 redis 中数据
                    String jti = token.getAdditionalInformation().get("jti").toString();
                    redisTemplate.delete(jti);
                }
                super.removeAccessToken(token);
            }
        }; // 不要少了分号
    }

}
