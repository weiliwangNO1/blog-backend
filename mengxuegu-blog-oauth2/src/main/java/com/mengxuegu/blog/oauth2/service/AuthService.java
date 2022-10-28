package com.mengxuegu.blog.oauth2.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.mengxuegu.blog.util.base.Result;
import com.mengxuegu.blog.util.enums.ResultEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service // 不要少了
public class AuthService {

    @Autowired // 负载均衡client
    LoadBalancerClient loadBalancerClient;

    public Result refreshToken(String header, String refreshToken) throws HttpProcessException {
        // 采用客户端负载均衡，从 Nacos 获取认证服务器的 ip 和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose("auth-server");
        if (serviceInstance == null) {
            return Result.error("未找到有效认证服务器");
        }
        // 请求刷新令牌 url
        String refreshTokenUrl = serviceInstance.getUri().toString() + "/auth/oauth/token";
        // 封装刷新令牌请求参数
        Map<String, Object> map = new HashMap<>(2);
        map.put("grant_type", "refresh_token");
        map.put("refresh_token", refreshToken);
        // 构建配置请求参数（网址、请求参数、编码、client）
        Header[] headers = HttpHeader.custom() // 自定义请求
                .contentType(HttpHeader.Headers.APP_FORM_URLENCODED) // 数据类型
                .authorization(header) // 认证请求头
                .build();
        HttpConfig config = HttpConfig.custom().headers(headers)
                .url(refreshTokenUrl)
                .map(map);
        // 发送请求, 响应令牌
        String token = HttpClientUtil.post(config);
        JSONObject jsonToken = JSON.parseObject(token);
        if(StringUtils.isNotEmpty(jsonToken.getString("error")) ) {
            return Result.build(ResultEnum.TOKEN_PAST);
        }
        // 响应新令牌对象
        return Result.ok( jsonToken );
    }

}
