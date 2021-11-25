package com.alonelyleaf.mybatis.multi.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 多数据源配置
 *
 * @author bijianlei
 * @date 2021/11/24
 */

@Data
@ConfigurationProperties("alonelyleaf.datasource")
public class MultiProperties {

    private boolean refresh = true;
}

