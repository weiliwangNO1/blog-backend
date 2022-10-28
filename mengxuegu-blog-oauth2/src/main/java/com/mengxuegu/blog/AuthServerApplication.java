package com.mengxuegu.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证服务器启动类
 * @since  2022-1-26
 * @author wwl
 */
@EnableFeignClients // 扫描 @Feign 接口
@EnableDiscoveryClient // 标识为 Nacos 客户端
@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
