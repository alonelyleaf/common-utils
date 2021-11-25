package com.alonelyleaf.mybatis.multi.logback;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志配置
 *
 * @author bijianlei
 * @date 2021/11/24
 */
@Data
@ConfigurationProperties("alonelyleaf.slow.sql.log")
public class LogProperties {

    private String file;

    private String level;

    private boolean console;

    private long time = 500L;
}
