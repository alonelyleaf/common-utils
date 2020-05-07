package com.alonelyleaf.spring.common.constant;

/**
 * @author bijl
 * @date 2019/6/10
 */
public interface Constant {

    class Channel {

        public static final String CHANNEL_1 = "message_channel_1";

        public static final String CHANNEL_2 = "message_channel_2";
    }

    class Header {

        public static final String TOKEN_FIELD = "token";

        public static final String X_FROM = "X-FROM";
    }

    class Gateway {
        /**
         * 服务名称，
         */
        public static final String RS_MANAGER = "business-rss-manager";

        /**
         * 服务地址
         */
        public static final String RS_MANAGER_HOST = "${microservice.rss.manager.host}";

    }
}
