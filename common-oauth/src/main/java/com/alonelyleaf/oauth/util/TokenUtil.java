package com.alonelyleaf.oauth.util;

import com.alonelyleaf.oauth.constant.StringPool;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 认证工具类
 *
 * @author bijl
 * @date 2020/11/25 下午7:19
 */
public class TokenUtil {


    public final static String MOBILEHEADER_KEY = "mobile";
    public final static String MOBILEHEADER_CODE = "code";
    public final static String WECHAT_HEADER_UNIONID = "unionId";

    public final static String WECHAT_HEADER_OPENID = "openId";
    public final static String WECHAT_HEADER_APPID = "appId";

    public final static String BYTEDANCE_HEADER_OPENID = "openId";

    public final static String HEADER_KEY = "Authorization";
    public final static String HEADER_PREFIX = "Basic ";

    public final static String DEFAULT_PASSWROD = "alonelyleaf";

    /**
     * 解码
     */
    @SneakyThrows
    public static String[] extractAndDecodeHeader() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        String header = ((ServletRequestAttributes)requestAttributes).getRequest().getHeader(TokenUtil.HEADER_KEY);
        if (header == null || !header.startsWith(TokenUtil.HEADER_PREFIX)) {
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }

        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);
        int index = token.indexOf(StringPool.COLON);
        if (index == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, index), token.substring(index + 1)};
        }
    }

    public static String getAuthorization(String clientId, String clientSecret) {
        return Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
    }

    public static void main(String[] args) {

        System.out.println(getAuthorization("student_ma", ""));
    }
}
