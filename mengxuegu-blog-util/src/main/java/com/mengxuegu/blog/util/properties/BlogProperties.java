package com.mengxuegu.blog.util.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @since  2022-1-22
 * @author wwl
 * 读取aliyun配置
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "weili.blog")
public class BlogProperties implements Serializable {
    // 会将 mengxuegu.blog.aliyun 绑定到 AliyunProperties 对象上。
    private AliyunProperties aliyun;
}
