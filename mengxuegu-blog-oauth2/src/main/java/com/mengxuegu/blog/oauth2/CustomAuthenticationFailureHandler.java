package com.mengxuegu.blog.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mengxuegu.blog.util.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 * @since  2022-1-26
 * @author wwl
 */
@Component("customAuthenticationFailureHandler")
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        // 响应错误信息：json格式
        response.setContentType("application/json;charset=UTF-8");
        String result = objectMapper.writeValueAsString(Result.error(e.getMessage()));
        response.getWriter().write(result);
    }
}
