package com.onecap.datahub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient clearWebClient(ClearConfig clearConfig) {
        return WebClient.builder()
                .baseUrl(clearConfig.getBaseUrl())
                .defaultHeader("accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("x-cleartax-auth-token", clearConfig.getAuthToken())
                .build();
    }
} 