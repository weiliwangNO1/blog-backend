package com.mengxuegu.blog.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign 拦截器让请求头携带 token
 * @since  2021-1-27
 * @author wwl
 */
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 使用 RequestContextHolder 工具获取 request 相关变量
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null) {
            // 获取 request
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(StringUtils.isNotEmpty(token)) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, token);
            }
        }
    }

}
