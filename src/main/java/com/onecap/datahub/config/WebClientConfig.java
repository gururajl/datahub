package com.onecap.datahub.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);

    @Value("${clear.api.base-url}")
    private String baseUrl;

    @Value("${CLEAR_AUTH_TOKEN}")  
    private String authToken;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("accept", "application/json")
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("x-cleartax-auth-token", authToken)
            .filter(logRequestResponse())
            .build();
    }

    private ExchangeFilterFunction logRequestResponse() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.debug("============================ Request Begin ============================");
            log.debug("Request: {} {}", request.method(), request.url());
            request.headers().forEach((name, values) -> 
                log.debug("Request Header: {}={}", name, values));
            log.debug("============================ Request End ==============================");
            return Mono.just(request);
        }).andThen(ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.debug("============================ Response Begin ===========================");
            log.debug("Response Status: {}", response.statusCode());
            response.headers().asHttpHeaders()
                .forEach((name, values) -> log.debug("Response Header: {}={}", name, values));
            
            return response.bodyToMono(String.class)
                .map(body -> {
                    log.debug("Response Body: {}", body);
                    log.debug("============================ Response End =============================");
                    return response.mutate().body(body).build();
                })
                .defaultIfEmpty(response.mutate().build());
        }));
    }
} 