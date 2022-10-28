package com.mengxuegu.blog.filter;

import com.nimbusds.jose.JWSObject;
import net.minidev.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;


/**
 *
 * 用于验证请求头访问令牌是否有效，通过token中的jti查询redis中是否存在
 * @since  2021-1-27
 * @author wwl
 */
@Component // 一定不要少了
public class AccessTokenFilter implements GlobalFilter, Ordered {

    private static Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 验证传递的访问令牌是否有效。
     * Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6Ikp
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 请求对象
        ServerHttpRequest request = exchange.getRequest();
        // 响应对象
        ServerHttpResponse response = exchange.getResponse();
        // 获取请求头访问令牌
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = StringUtils.substringAfter(authorization, "Bearer ");
        // 如果 token 为 null 可能/api接口不要带token的请求，直接放行
        if (StringUtils.isEmpty(token)) {
            return chain.filter(exchange);
        }
        String message = null;
        try {
            // 解析token中的载荷部分（认证信息），
            // 注意：载荷部分可直接获取，签名部分才要公钥解密去验证是否有效，交给资源服务器验证
            JWSObject jwsObject = JWSObject.parse(token);
            JSONObject jsonObject = jwsObject.getPayload().toJSONObject();
            // 查询 redis 是否存在，不存在则过期。
            String jti = jsonObject.get("jti").toString();
            Object value = redisTemplate.opsForValue().get(jti);
            if (value == null) {
                logger.info("令牌已过期 {}", token);
                message = "您的身份已过期，请重新认证！";

            }
        } catch (ParseException e) {
            logger.error("解析令牌错误：{} ", token);
            message = "无效令牌！";
        }
        // 响应消息内容对象
        if (message == null) {
            // 如果令牌存在redis就通过
            return chain.filter(exchange);
        }
        // 响应提示响应提示
        JSONObject result = new JSONObject();
        // 响应状态
        result.put("code", 1401);
        // 响应内容
        result.put("message", message);
        // 转换响应消息内容对象为字节
        byte[] bits = result.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        // 设置响应对象状态码 401
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        // 设置响应对象内容并且指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        // 返回响应对象
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        // 过滤器执行优先级，越小越优先
        // 当前过滤器在AuthorizationFilter后执行
        return 10;
    }


}
