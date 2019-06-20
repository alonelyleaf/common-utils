package com.alonelyleaf.spring.gateway;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(FeignClientsConfiguration.class)
@Configuration
public class RecordingClientConfiguration {

    @Bean
    public RecordingClient recordingClient(Decoder decoder,
                                           Encoder encoder,
                                           Client client,
                                           Contract contract,
                                           AccessTokenFinder accessTokenFinder) {

        return Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .errorDecoder(new ErrorDecoder())
                .contract(contract)
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger(RecordingClient.class))
                .requestInterceptor(new AccessTokenInterceptor(accessTokenFinder))
                .target(RecordingClient.class, "http://" + "Constant.Gateway.RS_MANAGER");
    }
}


class AccessTokenInterceptor implements RequestInterceptor {

    private static final String ACCESS_KEY = "accessToken";

    private AccessTokenFinder accessTokenFinder;

    public AccessTokenInterceptor(AccessTokenFinder accessTokenFinder) {

        this.accessTokenFinder = accessTokenFinder;
    }

    @Override
    public void apply(RequestTemplate template) {

        template.query(true, ACCESS_KEY, accessTokenFinder.getAccessToken());
    }
}