package com.mengxuegu.blog.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 加载中文的认证提示信息配置类
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@Configuration
public class ReloadMessageConfig {

    @Bean // 加载中文的认证提示信息
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new
                ReloadableResourceBundleMessageSource();
        //.properties 不要加到后面
        // messageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");
        messageSource.setBasename("classpath:messages_zh_CN");//不要.properties
        return messageSource;
    }

}
