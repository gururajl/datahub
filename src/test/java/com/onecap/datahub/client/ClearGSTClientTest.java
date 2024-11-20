package com.onecap.datahub.client;

import com.onecap.datahub.model.AddCredentialsRequest;
import com.onecap.datahub.model.AddCredentialsResponse;
import com.onecap.datahub.model.OtpRequestResponse;
import com.onecap.datahub.model.SubmitOtpRequest;
import com.onecap.datahub.model.SubmitOtpResponse;
import com.onecap.datahub.model.TriggerIrnRequest;
import com.onecap.datahub.model.TriggerIrnResponse;
import com.onecap.datahub.model.ReturnPeriod;
import com.onecap.datahub.model.IrnPullStatusResponse;
import com.onecap.datahub.model.FetchIrnListRequest;
import com.onecap.datahub.model.FetchIrnListResponse;
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

    @Test
    void triggerIrnListFetch_shouldSendCorrectRequest() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"requestId\":\"cabd934c-7c98-4b3d-9fb2-2fe2f9f15d52\",\"dataPullRequestId\":\"65cb681814e93b26da0953c0\"}")
        );

        String gstin = "03AAHCG7552R1Z1";
        String returnPeriod = "112023";
        String supplierType = "B2B";
        String invoiceType = "SALES";

        // Act
        TriggerIrnResponse response = clearGSTClient.triggerIrnListFetch(gstin, new ReturnPeriod(returnPeriod), TriggerIrnRequest.SupplierType.B2B, TriggerIrnRequest.InvoiceType.SALES );

        // Assert
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/einvoices/create-pull-request", recordedRequest.getPath());
        // assertEquals("your_auth_token_here", recordedRequest.getHeader("x-cleartax-auth-token"));
        assertEquals(MediaType.APPLICATION_JSON_VALUE, recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE));

        // Verify request body
        String expectedRequestBody = "{\"gstin\":\"" + gstin + "\",\"returnPeriod\":\"" + returnPeriod + "\",\"supplierType\":\"" + supplierType + "\",\"invoiceType\":\"" + invoiceType + "\"}";
        assertEquals(expectedRequestBody, recordedRequest.getBody().readUtf8());

        // Verify the response
        assertNotNull(response);
        assertEquals("cabd934c-7c98-4b3d-9fb2-2fe2f9f15d52", response.getRequestId().toString());
        assertEquals("65cb681814e93b26da0953c0", response.getDataPullRequestId());
    }

    @Test
    void triggerIrnListFetchAsync_shouldSendCorrectRequest() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"requestId\":\"cabd934c-7c98-4b3d-9fb2-2fe2f9f15d52\",\"dataPullRequestId\":\"65cb681814e93b26da0953c0\"}")
        );

        String gstin = "03AAHCG7552R1Z1";
        ReturnPeriod returnPeriod = new ReturnPeriod("112023");
        TriggerIrnRequest.SupplierType supplierType = TriggerIrnRequest.SupplierType.B2B;
        TriggerIrnRequest.InvoiceType invoiceType = TriggerIrnRequest.InvoiceType.SALES;

        // Act & Assert
        clearGSTClient.triggerIrnListFetchAsync(gstin, returnPeriod, supplierType, invoiceType)
            .as(StepVerifier::create)
            .expectNextMatches(response -> {
                assertEquals("cabd934c-7c98-4b3d-9fb2-2fe2f9f15d52", response.getRequestId().toString());
                assertEquals("65cb681814e93b26da0953c0", response.getDataPullRequestId());
                return true;
            })
            .verifyComplete();

        // Verify the request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/einvoices/create-pull-request", recordedRequest.getPath());
        // assertEquals("your_auth_token_here", recordedRequest.getHeader("x-cleartax-auth-token"));
    }

    @Test
    void getIrnPullStatus_shouldReturnCorrectResponse() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"requestId\":\"e2808655-1f6b-49c5-a97b-19e193946783\",\"status\":\"SUCCESS\",\"generatedUrls\":[]}")
        );

        String gstin = "03AAHCG7552R1Z1";
        String dataPullRequestId = "65a1116090a5ce7d5cb75e39";

        // Act & Assert
        clearGSTClient.getIrnPullStatus(gstin, dataPullRequestId)
            .as(StepVerifier::create)
            .expectNextMatches(response -> {
                assertEquals("e2808655-1f6b-49c5-a97b-19e193946783", response.getRequestId());
                assertEquals("SUCCESS", response.getStatus());
                return true;
            })
            .verifyComplete();

        // Verify the request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/einvoices/fetch-pull-response/03AAHCG7552R1Z1/65a1116090a5ce7d5cb75e39", recordedRequest.getPath());
    }

    @Test
    void fetchIrnList_shouldReturnCorrectResponse() throws InterruptedException {
        // Arrange
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"requestId\":\"test-request-id\",\"irnList\":[]}")
        );

        FetchIrnListRequest request = new FetchIrnListRequest();
        request.setGstin("03AAHCG7552R1Z1");
        request.setSupplierType("B2B");
        request.setInvoiceType("SALES");
        request.setPage(0);
        request.setSize(5);

        // Act & Assert
        clearGSTClient.fetchIrnList(request)
            .as(StepVerifier::create)
            .expectNextMatches(response -> {
                assertEquals("test-request-id", response.getRequestId());
                assertNotNull(response.getIrnList());
                return true;
            })
            .verifyComplete();

        // Verify the request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/clearIdentity/v1/einvoices/fetch-irn-list", recordedRequest.getPath());
    }
}