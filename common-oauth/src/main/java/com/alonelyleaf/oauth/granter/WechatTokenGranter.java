package com.alonelyleaf.oauth.granter;

import cn.hutool.core.util.StrUtil;
import com.alonelyleaf.oauth.exception.BusinessException;
import com.alonelyleaf.oauth.util.TokenUtil;
import com.alonelyleaf.oauth.util.WebUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * WxTokenGranter
 *
 * @author yanghaolei
 * @Date 2020/03/31 下午17:41
 */
public class WechatTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "wechat";

    private static final String USERNAME_FORMAT = "%s@%s";

    private final AuthenticationManager authenticationManager;


    public WechatTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                       ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        HttpServletRequest request = WebUtil.getRequest();

        // 1 校验手机验证码
        String mobile = request.getParameter(TokenUtil.MOBILEHEADER_KEY);
        String code = request.getParameter(TokenUtil.MOBILEHEADER_CODE);

        if (StrUtil.isNotEmpty(code)) {
            if (StrUtil.isEmpty(mobile)) {
                throw new BusinessException(401, "认证参数错误");
            }
            String realMobile = mobile.startsWith("86@") ?
                    mobile.split("@")[1] : "00" + mobile.split("@")[0] + mobile.split("@")[1];

            // todo 校验手机验证码
        }

        // 2 合成principal： wx + @ + unionId + @ + mobile
        String unionId = request.getParameter(TokenUtil.WECHAT_HEADER_UNIONID);
        String userName = StrUtil.isEmpty(mobile) ? unionId : String.format(USERNAME_FORMAT, unionId, mobile);

        // 3 合成userDetail
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());

        // 4 构建认证对象Authentication
        Authentication userAuth = new UsernamePasswordAuthenticationToken(userName, TokenUtil.DEFAULT_PASSWROD);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ase) {
            throw new BusinessException(-1, "用户认证失败");
        }

        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new BusinessException(-1, "用户认证失败");
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
