package com.alonelyleaf.spring.handler.banner;

import java.util.List;

/**
 * 广告图 handler
 *
 * @author bijl
 * @date 2021/2/24 上午9:44
 */
public interface IBannerHandler {

    /**
     * 支持的banner类型
     *
     * @param
     * @return
     * @author bijl
     * @date 2021/2/24 上午11:31
     */
    default List<Integer> supportTypeList() {
        return null;
    }

    /**
     * 差异性方法
     *
     * @param
     * @return
     * @author bijl
     * @date 2021/2/24 上午11:32
     */
    default void myMethod(String param) {}
}
