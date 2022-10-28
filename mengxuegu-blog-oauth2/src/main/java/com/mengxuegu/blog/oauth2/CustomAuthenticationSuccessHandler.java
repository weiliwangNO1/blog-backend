package com.mengxuegu.blog.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mengxuegu.blog.oauth2.service.AuthService;
import com.mengxuegu.blog.util.base.Result;
import com.mengxuegu.blog.util.enums.ResultEnum;
import com.mengxuegu.blog.util.tools.RequestUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 认证成功处理器
 * @since 2022-1-26
 * @author wwl
 */
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ClientDetailsService clientDetailsService;
    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    private static final String HEADER_TYPE = "Basic ";

    @Autowired
    private AuthService authService;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功 {}", authentication.getPrincipal());
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("header {}", header);
        // 封装响应结果
        Result result = null;
        try {
            if (header == null || !header.startsWith(HEADER_TYPE)) {
                throw new UnapprovedClientAuthenticationException("请求头无客户端信息");
            }
            // 解析请求头
            String[] tokens = RequestUtil.extractAndDecodeHeader(header);
            assert tokens.length == 2;
            String clientId = tokens[0];
            String clientSecret = tokens[1];
            ClientDetails clientDetails =
                    clientDetailsService.loadClientByClientId(clientId);
            if (clientDetails == null) {
                throw new UnapprovedClientAuthenticationException("无效 clientId");
            }
            // 客户端密码是否正确
            if (!passwordEncoder.matches(clientSecret,
                    clientDetails.getClientSecret())) {
                throw new UnapprovedClientAuthenticationException("无效clientSecret");
            }
            // 构建 tokenRequest 和 oAuth2Request 组合成 oAuth2Authentication 去获取 accessToken
            TokenRequest tokenRequest =
                    new TokenRequest(MapUtils.EMPTY_MAP, clientId,
                            clientDetails.getScope(), "custom");
            OAuth2Request oAuth2Request =
                    tokenRequest.createOAuth2Request(clientDetails);
            OAuth2Authentication oAuth2Authentication =
                    new OAuth2Authentication(oAuth2Request, authentication);
            // 获取 accessToken
            OAuth2AccessToken token =
                    authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            result = Result.ok(token);

        }catch (Exception e) {
            logger.error("refreshToken={}", e.getMessage(), e);
            // 认证失败
            result = Result.build(ResultEnum.AUTH_FAIL.getCode(), e.getMessage());
        }
        // 响应结果
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write( objectMapper.writeValueAsString( result ) );
    }

}
