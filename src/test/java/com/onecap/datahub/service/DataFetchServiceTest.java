package com.onecap.datahub.service;

import com.onecap.datahub.client.ClearGSTClient;
import com.onecap.datahub.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DataFetchServiceTest {

    @Mock
    private ConsentService consentService;

    @Mock
    private ClearGSTClient clearGSTClient;

    private DataFetchService dataFetchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataFetchService = new DataFetchService(consentService, clearGSTClient);
    }

    @Test
    void testFetchGSTInvoicesWithValidConsent() {
        String customerId = "CUST001";
        String gstin = "29ABCDE1234F1Z5";
        ReturnPeriod returnPeriod = new ReturnPeriod("042023");

        // Mock consent validation
        when(consentService.hasValidConsent(customerId, gstin, ConsentType.GSTN))
            .thenReturn(true);

        // Mock GST API responses
        TriggerIrnResponse triggerResponse = new TriggerIrnResponse();
        triggerResponse.setDataPullRequestId("REQ123");

        IrnPullStatusResponse statusResponse = new IrnPullStatusResponse();
        statusResponse.setStatus("COMPLETED");

        FetchIrnListResponse listResponse = new FetchIrnListResponse();
        List<Invoice> mockInvoices = Arrays.asList(new Invoice(), new Invoice());
        listResponse.setIrnList(mockInvoices);

        when(clearGSTClient.triggerIrnListFetchAsync(any(), any(), any(), any()))
            .thenReturn(Mono.just(triggerResponse));
        when(clearGSTClient.getIrnPullStatus(any(), any()))
            .thenReturn(Mono.just(statusResponse));
        when(clearGSTClient.fetchIrnList(any()))
            .thenReturn(Mono.just(listResponse));

        // Test
        StepVerifier.create(dataFetchService.fetchGSTInvoices(customerId, gstin, returnPeriod))
            .expectNext(mockInvoices)
            .verifyComplete();
    }

    @Test
    void testFetchGSTInvoicesWithoutValidConsent() {
        String customerId = "CUST001";
        String gstin = "29ABCDE1234F1Z5";
        ReturnPeriod returnPeriod = new ReturnPeriod("042023");

        // Mock invalid consent
        when(consentService.hasValidConsent(customerId, gstin, ConsentType.GSTN))
            .thenReturn(false);

        // Test
        StepVerifier.create(dataFetchService.fetchGSTInvoices(customerId, gstin, returnPeriod))
            .expectError(IllegalStateException.class)
            .verify();
    }
} 