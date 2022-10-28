package com.mengxuegu.blog.oauth2;

import com.mengxuegu.blog.util.base.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 成功退出处理器
 * @since  2022-1-26
 * @author wwl
 */
/**
 * 退出成功处理器，清除 redis 中的缓存
 */
@Component("customLogoutSuccessHandler")
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        // 获取 access_token
        String accessToken = request.getParameter("accessToken");
        if (StringUtils.isNotBlank(accessToken)) {
            // 转换token对象
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
            if (oAuth2AccessToken != null) {
                // 删除redis的访问令牌
                tokenStore.removeAccessToken(oAuth2AccessToken);
            }
        }
        // 退出成功, 响应结果
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write( Result.ok().toJsonString() );
    }
}
