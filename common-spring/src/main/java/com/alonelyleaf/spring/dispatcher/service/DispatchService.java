package com.alonelyleaf.spring.dispatcher.service;

import com.alonelyleaf.spring.dispatcher.support.DispatchRegistry;
import com.alonelyleaf.spring.dispatcher.vo.TransmitData;
import com.alonelyleaf.util.CommonUtil;
import com.alonelyleaf.util.JSONUtil;
import com.alonelyleaf.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Map;

@Service
@DependsOn()
public class DispatchService extends CommonUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DispatchRegistry registry;

    /**
     * 根据消息类型进行分发，适用于对于同一数据来源根据类型进行不同处理的情况
     *
     */
    public void dispatch(TransmitData transmitData) {

        Map<Integer, DispatchRegistry.OperationContext> methodMap = registry.getRegistry();
        DispatchRegistry.OperationContext operationContext = methodMap.get(transmitData.getType());
        if (isEmpty(operationContext)) {
            logger.info("[DispatchService] not allowed type :{}", transmitData.getType());
            return;
        }
        Method method = operationContext.getMethod();
        try {
            method.invoke(
                    SpringContextUtil.getBean(operationContext.getClazz()),
                    convertTo(
                            transmitData.getObj(),
                            transmitData.getData(),
                            operationContext.getArgTypes()[0]
                    )
            );
        } catch (Exception e) {
            logger.error("接口类型：[" + transmitData.getType() + "]," +
                    "数据:[" + new String(transmitData.getData()) + "]," +
                    "反射执行失败->" + e.getMessage(), e);
        }
    }


    /**
     * 将byte数组转成指定类型的对象
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T convertTo(byte[] data, Class<T> clazz) {

        String json = new String(data);
        return JSONUtil.deserialize(json, clazz);
    }

    /**
     * 将Object转成指定类型的对象
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T convertTo(Object obj, Class<T> clazz) {

        return copy(obj, clazz);
    }

    public <T> T convertTo(Object obj, byte[] data, Class<T> clazz) {

        if (isNotEmpty(obj)) {
            return convertTo(obj, clazz);
        }

        return convertTo(data, clazz);
    }

}
