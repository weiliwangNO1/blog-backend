package com.mengxuegu.blog.web.controller;

import com.google.common.base.Preconditions;
import com.mengxuegu.blog.oauth2.service.AuthService;
import com.mengxuegu.blog.util.base.Result;
import com.mengxuegu.blog.util.tools.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import
        org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;


@RestController
public class AuthController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsService clientDetailsService;

    private static final String HEADER_TYPE = "Basic ";

    @GetMapping("/user/refreshToken")
    public Result refreshToken(HttpServletRequest request) {
        try {
            // 获取请求中的刷新令牌
            String refreshToken = (String) request.getParameter("refreshToken");
            Preconditions.checkArgument(StringUtils.isNotEmpty(refreshToken), "刷新令牌不能为空");
            // 请求头
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith(HEADER_TYPE)) {
                throw new UnapprovedClientAuthenticationException("请求头中无client信息");
            }
            String[] tokens = RequestUtil.extractAndDecodeHeader(header);
            assert tokens.length == 2;
            String clientId = tokens[0];
            String clientSecret = tokens[1];
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
            if (clientDetails == null) {
                throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:"
                        + clientId);
            }
// 客户端密码是否正确
            if(!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())){
                throw new UnapprovedClientAuthenticationException("无效clientSecret");
            }
// 获取刷新令牌
            return authService.refreshToken(header, refreshToken);
        } catch (Exception e) {
            logger.error("refreshToken={}", e.getMessage(), e);
            return Result.error("新令牌获取失败："+ e.getMessage());
        }
    }


}
