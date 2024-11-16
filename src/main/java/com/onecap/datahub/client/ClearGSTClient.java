package com.onecap.datahub.client;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.onecap.datahub.model.AddCredentialsRequest;

import reactor.core.publisher.Mono;

@Component
public class ClearGSTClient {
    private final WebClient webClient;
    
    public ClearGSTClient(WebClient clearWebClient) {
        this.webClient = clearWebClient;
    }

    // Synchronous version
    public String addCredentials(String gstin, String username) {
        return webClient.post()
                .uri("/clearIdentity/v1/gst-auth/add-credentials")
                .header("gstin", gstin)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AddCredentialsRequest(username))
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Makes it synchronous
    }

    // Asynchronous version
    public Mono<String> addCredentialsAsync(String gstin, String username) {
        return webClient.post()
                .uri("/clearIdentity/v1/gst-auth/add-credentials")
                .header("gstin", gstin)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AddCredentialsRequest(username))
                .retrieve()
                .bodyToMono(String.class);
    }
}