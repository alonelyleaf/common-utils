package com.alonelyleaf.spring.handler.banner;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 广告图 handler 组装器
 *
 * @author bijl
 * @date 2021/2/24 上午11:34
 */
@Component
public class CompositeBannerHandler implements ApplicationContextAware, InitializingBean {

    private ApplicationContext context;
    private ConcurrentHashMap<Integer, IBannerHandler> bannerTypeHandlerMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Map<String, IBannerHandler> bannerHandlerMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.context,
            IBannerHandler.class, true, true);
        for (IBannerHandler handler : bannerHandlerMap.values()) {

            Optional.ofNullable(handler.supportTypeList()).ifPresent(list -> {

                list.forEach(type -> {
                    bannerTypeHandlerMap.put(type, handler);
                });
            });
        }
    }

    /**
     * 根据类型获取处理器
     *
     * @param
     * @return
     * @author bijl
     * @date 2021/2/24 上午11:37
     */
    public IBannerHandler getHandlerByType(Integer type) {

        return bannerTypeHandlerMap.get(type);
    }

}
