package com.mengxuegu.blog.filter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关限流过滤器
 * @since  2021-1-27
 * @author wwl
 */
@Component
public class UriKeyResolver implements KeyResolver {


    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        // 针对微服务的每个请求地址进行限流
        return Mono.just(exchange.getRequest().getURI().getPath());
    }
}
