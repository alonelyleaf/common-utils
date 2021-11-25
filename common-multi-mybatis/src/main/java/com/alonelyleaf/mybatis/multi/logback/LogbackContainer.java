package com.alonelyleaf.mybatis.multi.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;

/**
 * LogbackContainer
 *
 * @author bijianlei
 * @date 2021/11/24
 */
public class LogbackContainer {

    private RollingFileAppender<ILoggingEvent> fileAppender;
    private ConsoleAppender<ILoggingEvent> consoleAppender;
    private String applicationName;

    public LogbackContainer() {
    }

    public void start(LogProperties logProperties) {
        String level = !StringUtils.isEmpty(logProperties.getLevel()) ? logProperties.getLevel() : "info";
        String file = !StringUtils.isEmpty(logProperties.getFile()) ? logProperties.getFile() : "logs/";
        this.doInitializer(file, level, 0, "slow-sql", logProperties.isConsole());
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void stop() {
    }

    private void doInitializer(String file, String level, int maxHistory, String name, boolean console) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger(name.toUpperCase());
        this.fileAppender = new RollingFileAppender();
        this.fileAppender.setContext(loggerContext);
        this.fileAppender.setName(name.toUpperCase());
        if (!file.endsWith("/")) {
            file = file + "/";
        }

        this.fileAppender.setFile(file + name + ".log");
        this.fileAppender.setAppend(true);
        if (console) {
            this.consoleAppender = new ConsoleAppender();
            this.consoleAppender.setName(name.toUpperCase() + "CONSOLE");
            PatternLayoutEncoder conLayout = new PatternLayoutEncoder();
            conLayout.setContext(loggerContext);
            conLayout.setPattern(this.applicationName + "|%date{yyyy-MM-dd HH:mm:ss.SSS}|%X{log-id}|%X{X-B3-TraceId}|%X{x-uid}|%X{platform}|%X{localIp}|%-5level|[%thread]|%logger{56}.%method:%L|%msg%n");
            conLayout.setCharset(Charset.forName("UTF-8"));
            conLayout.start();
            this.consoleAppender.setEncoder(conLayout);
            this.consoleAppender.start();
            rootLogger.addAppender(this.consoleAppender);
        }

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> policy = new SizeAndTimeBasedRollingPolicy();
        policy.setContext(loggerContext);
        policy.setFileNamePattern(file + "%d{yyyy-MM-dd,aux}/" + name + ".%d{yyyy-MM-dd-HH}.%i.log");
        policy.setMaxHistory(168);
        policy.setCleanHistoryOnStart(true);
        policy.setMaxFileSize(new FileSize(2147483648L));
        policy.setTotalSizeCap(new FileSize(107374182400L));
        policy.setParent(this.fileAppender);
        policy.start();
        this.fileAppender.setRollingPolicy(policy);
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(this.applicationName + "|%date{yyyy-MM-dd HH:mm:ss.SSS}|%X{log-id}|%X{X-B3-TraceId}|%X{x-uid}|%X{platform}|%X{localIp}|%-5level|[%thread]|%logger{56}.%method:%L|%msg%n");
        encoder.setCharset(Charset.forName("UTF-8"));
        encoder.start();
        this.fileAppender.setEncoder(encoder);
        this.fileAppender.start();
        rootLogger.addAppender(this.fileAppender);
        rootLogger.setLevel(Level.toLevel(level));
        rootLogger.setAdditive(false);
        SlowSqlLogger.SLOW_SQL = rootLogger;
    }

    public RollingFileAppender<ILoggingEvent> getFileAppender() {
        return this.fileAppender;
    }

    public void setFileAppender(RollingFileAppender<ILoggingEvent> fileAppender) {
        this.fileAppender = fileAppender;
    }

    public ConsoleAppender<ILoggingEvent> getConsoleAppender() {
        return this.consoleAppender;
    }

    public void setConsoleAppender(ConsoleAppender<ILoggingEvent> consoleAppender) {
        this.consoleAppender = consoleAppender;
    }
}

