package com.onecap.datahub.client;

import com.onecap.datahub.model.AddCredentialsRequest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import reactor.test.StepVerifier;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClearGSTClientTest {

    private static MockWebServer mockWebServer;
    private ClearGSTClient clearGSTClient;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
        clearGSTClient = new ClearGSTClient(webClient);
    }

    @Test
    void addCredentials_shouldSendCorrectRequest() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"status\":\"success\"}")
        );

        String gstin = "TEST_GSTIN";
        String username = "TEST_USER";

        // Act
        String response = clearGSTClient.addCredentials(gstin, username);

        // Assert
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        
        // Verify the request
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/gst-auth/add-credentials", recordedRequest.getPath());
        assertEquals(gstin, recordedRequest.getHeader("gstin"));
        assertEquals(MediaType.APPLICATION_JSON_VALUE, recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE));
        
        // Verify request body contains username
        String expectedRequestBody = "{\"username\":\"" + username + "\"}";
        assertEquals(expectedRequestBody, recordedRequest.getBody().readUtf8());
        
        // Verify the response
        assertNotNull(response);
        assertEquals("{\"status\":\"success\"}", response);
    }

    @Test
    void addCredentialsAsync_shouldSendCorrectRequest() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"status\":\"success\"}")
        );

        String gstin = "TEST_GSTIN";
        String username = "TEST_USER";

        // Act & Assert
        clearGSTClient.addCredentialsAsync(gstin, username)
            .as(StepVerifier::create)
            .expectNext("{\"status\":\"success\"}")
            .verifyComplete();

        // Verify the request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/gst-auth/add-credentials", recordedRequest.getPath());
        assertEquals(gstin, recordedRequest.getHeader("gstin"));
        assertEquals(MediaType.APPLICATION_JSON_VALUE, recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE));
    }
}