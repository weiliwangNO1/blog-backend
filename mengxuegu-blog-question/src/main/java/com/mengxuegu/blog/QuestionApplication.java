package com.mengxuegu.blog;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 问答模块启动器
 * @since 2022-1-23
 * @author wwl
 */
@EnableFeignClients   //feign远程调用
@EnableDiscoveryClient   //nacos服务注册发现
@EnableSwagger2Doc
@SpringBootApplication
public class QuestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionApplication.class, args);
    }

}
