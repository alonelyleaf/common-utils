package com.alonelyleaf.spring.common.i18n;

import com.alonelyleaf.util.MapUtil;

import java.util.Map;

/**
 * @author bijl
 * @date 2019/6/19
 */
public interface Message {

    class Common {

        /**
         * common business error
         */
        public static final String UNAUTHORIZED = "service.common.token.unauthorized";

        public static final String PERMISSION_DENIED = "service.common.permission.denied";

        public static final String INTERNAL_ERROR = "service.common.internal.error";

        public static final String RESOURCES_NOT_FOUND = "service.common.resources.not.found";

        public static final String SERVICE_NOT_FOUND = "service.common.not.found";

        public static final String GATEWAY_ERROR = "service.common.gateway.error";

        public static final String PROTOCOL_NOT_MATCH = "service.common.protocol.not.match";


        private static final Map description = MapUtil.asLinkedHashMap(
                UNAUTHORIZED, "用户未登录或登录已失效，请重新登录",
                PERMISSION_DENIED, "没有访问权限",
                INTERNAL_ERROR, "500 服务器暂时不可用，请稍候重试",
                RESOURCES_NOT_FOUND, "资源未找到",
                SERVICE_NOT_FOUND, "未找到该服务",
                GATEWAY_ERROR, "服务暂不可用，请稍候重试",
                PROTOCOL_NOT_MATCH, "参数错误，请对照接口参数文档"

        );
    }
}
