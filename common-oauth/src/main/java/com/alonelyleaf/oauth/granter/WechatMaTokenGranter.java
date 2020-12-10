package com.alonelyleaf.oauth.granter;

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
 * 微信小程序 TokenGranter
 *
 * @author bijl
 * @date 2020/11/16 上午10:16
 */
public class WechatMaTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "wechat_ma";

    private final AuthenticationManager authenticationManager;

    public WechatMaTokenGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                                ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

        HttpServletRequest request = WebUtil.getRequest();

        // 1 userName(mobile)
        String userName = request.getParameter(TokenUtil.MOBILEHEADER_KEY);


        // 2 合成userDetail
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());

        // 3 构建认证对象Authentication
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
