package com.onecap.datahub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClearConfig {
    @Value("${clear.api.base-url}")
    private String baseUrl;

    @Value("${clear.api.auth-token}")
    private String authToken;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAuthToken() {
        return authToken;
    }
}