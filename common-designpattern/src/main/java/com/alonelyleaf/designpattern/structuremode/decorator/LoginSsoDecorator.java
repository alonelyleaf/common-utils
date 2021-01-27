package com.alonelyleaf.designpattern.structuremode.decorator;

import com.alonelyleaf.designpattern.structuremode.decorator.interceptor.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在具体的装饰类实现中，继承了装饰类 SsoDecorator ，那么现在就可以扩展方法preHandle;
 * 在 preHandle 的实现中可以看到，这里只关心扩展部分的功能，同时不会影响原有类的核⼼服务，也不会因为使⽤继承⽅式而导致的多余子类，增加了整体的灵活性。
 *
 * @param
 * @return
 */
public class LoginSsoDecorator extends SsoDecorator {

    private Logger logger = LoggerFactory.getLogger(LoginSsoDecorator.class);

    private static Map<String, String> authMap = new ConcurrentHashMap<String, String>();

    static {
        authMap.put("huahua", "queryUserInfo");
        authMap.put("doudou", "queryUserInfo");
    }

    public LoginSsoDecorator(HandlerInterceptor handlerInterceptor) {
        super(handlerInterceptor);
    }


    @Override
    public boolean preHandle(String request, String response, Object handler) {
        // 调用原有方法
        boolean success = super.preHandle(request, response, handler);
        // 增加额外处理
        if (!success) return false;
        String userId = request.substring(8);
        String method = authMap.get(userId);
        logger.info("模拟单点登录方法访问拦截校验：{} {}", userId, method);
        // 模拟方法校验
        return "queryUserInfo".equals(method);
    }
}
