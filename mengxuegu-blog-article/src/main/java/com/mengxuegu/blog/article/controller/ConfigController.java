package com.mengxuegu.blog.article.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope  //实时更新读取配置文件
@RestController
public class ConfigController {

    /*@Value("${user.name}")
    private String name;

    @Value("${user.age}")
    private String age;

    @GetMapping(value = "getConfig")
    public String config() {
        String content = "name:" + name + ", age:" + age;
        System.out.println(content);
        return content;
    }*/

}
