package com.alonelyleaf.spring.common.i18n;

import com.alonelyleaf.util.MapUtil;

import java.util.Map;

/**
 * @author bijl
 * @date 2019/6/19
 */
public interface Message {

    class ServiceCommon {

        public static final String unauthorized = "service.common.token.unauthorized";

        public static final String permissionDenied = "service.common.permission.denied";

        public static final String internalError = "service.common.internal.error";

        public static final String resourcesNotFound = "service.common.resources.not.found";

        public static final String serviceNotFound = "service.common.not.found";

        public static final String gateWayError = "service.common.gateway.error";

        public static final String idNotNull = "service.common.id.not.null";

        public static final String typeInvalid = "service.common.type.invalid";

        public static final String emailInvalid = "service.common.email.invalid";

        public static final String concurrentEdit = "service.common.concurrent.edit";

        public static final String protocolNotMatch = "service.common.protocol.not.match";

        public static final String waitTimeout = "service.common.wait.timeout";

        public static final String unsupportedOperation = "service.common.unsupported.operation";

        private static final Map description = MapUtil.asLinkedHashMap(
                unauthorized, "用户未登录或登录已失效，请重新登录",
                permissionDenied, " 没有访问权限",
                internalError, "500 服务器暂时不可用，请稍候重试",
                resourcesNotFound, "资源未找到",
                serviceNotFound, "未找到该服务",
                gateWayError, "服务暂不可用，请稍候重试",
                idNotNull, "ID不能为空",
                typeInvalid, "类型错误",
                concurrentEdit, "有人正在进行操作，请稍候再试",
                emailInvalid, "邮箱格式错误",
                protocolNotMatch, "参数错误，请对照接口参数文档",
                waitTimeout, "服务器繁忙，请重试",
                unsupportedOperation, "不支持的操作类型"
        );
    }
}
