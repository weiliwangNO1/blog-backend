package com.mengxuegu.blog;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统启动类
 * @since  2022-1-25
 * @author wwl
 */
@EnableFeignClients
@EnableDiscoveryClient // 标识为 Nacos 客户端
@EnableSwagger2Doc //启动Swagger
@SpringBootApplication
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }

}
