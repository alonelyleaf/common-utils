package com.alonelyleaf.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CommonOauthApplication {

    private static final Logger logger = LoggerFactory.getLogger(CommonOauthApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CommonOauthApplication.class, args);

        logger.info("application is started...");
    }

}
