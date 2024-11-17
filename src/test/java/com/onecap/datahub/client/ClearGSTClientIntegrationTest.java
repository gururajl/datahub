package com.onecap.datahub.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Disabled;

import com.onecap.datahub.model.AddCredentialsResponse;

import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@Disabled("Integration tests that require real server connection")
class ClearGSTClientIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ClearGSTClientIntegrationTest.class);

    @Autowired
    private ClearGSTClient clearGSTClient;

    @Test
    @Disabled("Requires real server connection")
    void addCredentials_withRealServer() {
        // Arrange
        String gstin = "27AAECM7914L1Z2";    // Replace with valid GSTIN
        String username = "magoxyinfra";   // Replace with valid username

        // Act
        AddCredentialsResponse response = clearGSTClient.addCredentials(gstin, username);
        
        // Assert
        assertNotNull(response);
        log.info("Response received: requestId={}, status={}", response.getRequestId(), response.getStatus());
    }

    @Test
    @Disabled("Requires real server connection")
    void addCredentialsAsync_withRealServer() {
        // Arrange
        String gstin = "27AAECM7914L1Z2";    // Replace with valid GSTIN
        String username = "magoxyinfra";   // Replace with valid username

        // Act & Assert
        clearGSTClient.addCredentialsAsync(gstin, username)
            .as(StepVerifier::create)
            .expectNextMatches(response -> {
                log.info("Async response received: requestId={}, status={}", response.getRequestId(), response.getStatus());
                return response != null;
            })
            .verifyComplete();
    }
} 