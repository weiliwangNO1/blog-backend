package com.mengxuegu.blog;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 文章模块
 * @since  2022-1-21
 * @author wwl
 */
@EnableFeignClients   //feign接口调用
@EnableDiscoveryClient   //nacos服务注册与发现
@EnableSwagger2Doc
@SpringBootApplication
public class ArticleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }

}
