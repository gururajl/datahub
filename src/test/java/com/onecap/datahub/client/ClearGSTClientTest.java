package com.onecap.datahub.client;

import com.onecap.datahub.model.AddCredentialsRequest;
import com.onecap.datahub.model.AddCredentialsResponse;
import com.onecap.datahub.model.OtpRequestResponse;
import com.onecap.datahub.model.SubmitOtpRequest;
import com.onecap.datahub.model.SubmitOtpResponse;
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
                .setBody("{\"requestId\":\"12345\",\"status\":\"PROCESSED\"}")
        );

        String gstin = "TEST_GSTIN";
        String username = "TEST_USER";

        // Act
        AddCredentialsResponse response = clearGSTClient.addCredentials(gstin, username);

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
        assertEquals("12345", response.getRequestId());
        assertEquals("PROCESSED", response.getStatus());
    }

    @Test
    void addCredentialsAsync_shouldSendCorrectRequest() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"requestId\":\"12345\",\"status\":\"PROCESSED\"}")
        );

        String gstin = "TEST_GSTIN";
        String username = "TEST_USER";

        // Act & Assert
        clearGSTClient.addCredentialsAsync(gstin, username)
            .as(StepVerifier::create)
            .expectNextMatches(response -> {
                assertEquals("12345", response.getRequestId());
                assertEquals("PROCESSED", response.getStatus());
                return true;
            })
            .verifyComplete();

        // Verify the request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/gst-auth/add-credentials", recordedRequest.getPath());
        assertEquals(gstin, recordedRequest.getHeader("gstin"));
        assertEquals(MediaType.APPLICATION_JSON_VALUE, recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    void requestOtp_shouldSendCorrectRequest() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"requestId\":\"a81c49ef-ca50-46aa-ac42-79be1784c752\",\"status\":\"SUCCESS\"}")
        );

        String gstin = "TEST_GSTIN";

        // Act
        OtpRequestResponse response = clearGSTClient.requestOtp(gstin);

        // Assert
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        
        // Verify the request
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/gst-auth/otp/request", recordedRequest.getPath());
        assertEquals(gstin, recordedRequest.getHeader("gstin"));
        
        // Verify the response
        assertNotNull(response);
        assertEquals("a81c49ef-ca50-46aa-ac42-79be1784c752", response.getRequestId());
        assertEquals("SUCCESS", response.getStatus());
    }

    @Test
    void requestOtpAsync_shouldSendCorrectRequest() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"requestId\":\"a81c49ef-ca50-46aa-ac42-79be1784c752\",\"status\":\"SUCCESS\"}")
        );

        String gstin = "TEST_GSTIN";

        // Act & Assert
        clearGSTClient.requestOtpAsync(gstin)
            .as(StepVerifier::create)
            .expectNextMatches(response -> {
                assertEquals("a81c49ef-ca50-46aa-ac42-79be1784c752", response.getRequestId());
                assertEquals("SUCCESS", response.getStatus());
                return true;
            })
            .verifyComplete();

        // Verify the request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/gst-auth/otp/request", recordedRequest.getPath());
        assertEquals(gstin, recordedRequest.getHeader("gstin"));
    }

    @Test
    void submitOtp_shouldSendCorrectRequest() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"requestId\":\"3a4ce4c2-ec6b-40b3-9bbc-64f2d7f87b0e\",\"status\":\"SUCCESS\"}")
        );

        String gstin = "TEST_GSTIN";
        String otp = "488673";
        
        // Act
        SubmitOtpResponse response = clearGSTClient.submitOtp(gstin, otp);

        // Assert
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        
        // Verify the request
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/gst-auth/otp/submit", recordedRequest.getPath());
        assertEquals(gstin, recordedRequest.getHeader("gstin"));
        // assertEquals(authToken, recordedRequest.getHeader("x-cleartax-auth-token"));
        assertEquals(MediaType.APPLICATION_JSON_VALUE, recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE));
        
        // Verify request body contains otp
        String expectedRequestBody = "{\"otp\":\"" + otp + "\"}";
        assertEquals(expectedRequestBody, recordedRequest.getBody().readUtf8());
        
        // Verify the response
        assertNotNull(response);
        assertEquals("3a4ce4c2-ec6b-40b3-9bbc-64f2d7f87b0e", response.getRequestId());
        assertEquals("SUCCESS", response.getStatus());
    }

    @Test
    void submitOtpAsync_shouldSendCorrectRequest() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"requestId\":\"3a4ce4c2-ec6b-40b3-9bbc-64f2d7f87b0e\",\"status\":\"SUCCESS\"}")
        );

        String gstin = "TEST_GSTIN";
        String otp = "488673";
        // String authToken = "TEST_AUTH_TOKEN";

        // Act & Assert
        clearGSTClient.submitOtpAsync(gstin, otp)
            .as(StepVerifier::create)
            .expectNextMatches(response -> {
                assertEquals("3a4ce4c2-ec6b-40b3-9bbc-64f2d7f87b0e", response.getRequestId());
                assertEquals("SUCCESS", response.getStatus());
                return true;
            })
            .verifyComplete();

        // Verify the request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/gst-auth/otp/submit", recordedRequest.getPath());
        assertEquals(gstin, recordedRequest.getHeader("gstin"));
        // assertEquals(authToken, recordedRequest.getHeader("x-cleartax-auth-token"));
    }
}