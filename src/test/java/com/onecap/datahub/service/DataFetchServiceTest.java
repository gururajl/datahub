package com.onecap.datahub.service;

import com.onecap.datahub.client.ClearGSTClient;
import com.onecap.datahub.model.*;
import com.onecap.datahub.repository.DataFetchStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataFetchServiceTest {

    @Mock
    private ConsentService consentService;

    @Mock
    private ClearGSTClient clearGSTClient;

    @Mock
    private DataFetchStateRepository fetchStateRepository;

    private DataFetchService dataFetchService;

    private static final String CUSTOMER_ID = "CUST001";
    private static final String GSTIN = "29ABCDE1234F1Z5";

    @BeforeEach
    void setUp() {
        dataFetchService = new DataFetchService(consentService, clearGSTClient, fetchStateRepository);
    }

    @Test
    void whenNoValidConsent_thenReturnError() {
        when(consentService.hasValidConsent(CUSTOMER_ID, GSTIN, ConsentType.GSTN))
            .thenReturn(false);

        StepVerifier.create(dataFetchService.fetchIncrementalInvoices(CUSTOMER_ID, GSTIN))
            .expectError(IllegalStateException.class)
            .verify();

        verify(clearGSTClient, never()).triggerIrnListFetchAsync(any(), any(), any(), any());
    }

    @Test
    void whenFirstTimeFetch_thenCreateNewFetchState() {
        // Setup
        when(consentService.hasValidConsent(CUSTOMER_ID, GSTIN, ConsentType.GSTN))
            .thenReturn(true);
        
        when(fetchStateRepository.findByCustomerIdAndAccountIdentifierAndSourceType(
            CUSTOMER_ID, GSTIN, DataSourceType.GST_INVOICE))
            .thenReturn(Optional.empty());

        DataFetchState newState = new DataFetchState(CUSTOMER_ID, GSTIN, DataSourceType.GST_INVOICE);
        when(fetchStateRepository.save(any(DataFetchState.class)))
            .thenReturn(newState);

        mockSuccessfulGSTResponse(Arrays.asList(createMockInvoice("IRN001")));

        // Test
        StepVerifier.create(dataFetchService.fetchIncrementalInvoices(CUSTOMER_ID, GSTIN))
            .expectNextMatches(invoices -> invoices.size() == 1)
            .verifyComplete();

        // Verify
        verify(fetchStateRepository).save(argThat(state -> 
            state.getCustomerId().equals(CUSTOMER_ID) &&
            state.getAccountIdentifier().equals(GSTIN) &&
            state.getSourceType() == DataSourceType.GST_INVOICE
        ));
    }

    @Test
    void whenExistingFetchState_thenFetchIncrementalInvoices() {
        // Setup
        when(consentService.hasValidConsent(CUSTOMER_ID, GSTIN, ConsentType.GSTN))
            .thenReturn(true);

        DataFetchState existingState = new DataFetchState();
        existingState.setLastFetchedId("IRN001");
        when(fetchStateRepository.findByCustomerIdAndAccountIdentifierAndSourceType(
            CUSTOMER_ID, GSTIN, DataSourceType.GST_INVOICE))
            .thenReturn(Optional.of(existingState));

        List<Invoice> mockInvoices = Arrays.asList(
            createMockInvoice("IRN003"),
            createMockInvoice("IRN002"),
            createMockInvoice("IRN001")
        );
        mockSuccessfulGSTResponse(mockInvoices);

        // Test
        StepVerifier.create(dataFetchService.fetchIncrementalInvoices(CUSTOMER_ID, GSTIN))
            .expectNextMatches(invoices -> 
                invoices.size() == 3 && 
                invoices.get(0).getIrn().equals("IRN003"))
            .verifyComplete();

        // Verify state update
        verify(fetchStateRepository).save(argThat(state -> 
            state.getLastFetchedId().equals("IRN003")
        ));
    }

    @Test
    void whenMultiplePages_thenFetchAllUntilLastKnownInvoice() {
        // Setup
        when(consentService.hasValidConsent(CUSTOMER_ID, GSTIN, ConsentType.GSTN))
            .thenReturn(true);

        DataFetchState existingState = new DataFetchState();
        existingState.setLastFetchedId("IRN011");
        when(fetchStateRepository.findByCustomerIdAndAccountIdentifierAndSourceType(
            CUSTOMER_ID, GSTIN, DataSourceType.GST_INVOICE))
            .thenReturn(Optional.of(existingState));

        // Mock first page response
        List<Invoice> page1 = Arrays.asList(
            createMockInvoice("IRN002"),
            createMockInvoice("IRN001")
        );
        
        // Mock second page response
        List<Invoice> page2 = Arrays.asList(
            createMockInvoice("IRN012"),
            createMockInvoice("IRN011")
        );

        mockSuccessfulGSTResponseWithPagination(Arrays.asList(page1, page2));

        // Test
        StepVerifier.create(dataFetchService.fetchIncrementalInvoices(CUSTOMER_ID, GSTIN))
            .expectNextMatches(invoices -> 
                invoices.size() == 4 && 
                invoices.get(0).getIrn().equals("IRN002"))
            .verifyComplete();
    }

    private Invoice createMockInvoice(String irn) {
        Invoice invoice = new Invoice();
        invoice.setIrn(irn);
        Invoice.DocumentDetails docDetails = new Invoice.DocumentDetails();
        docDetails.setDocumentDate(LocalDateTime.now().toString());
        invoice.setDocumentDetails(docDetails);
        return invoice;
    }

    private void mockSuccessfulGSTResponse(List<Invoice> invoices) {
        TriggerIrnResponse triggerResponse = new TriggerIrnResponse();
        triggerResponse.setDataPullRequestId("REQ123");

        IrnPullStatusResponse statusResponse = new IrnPullStatusResponse();
        statusResponse.setStatus("COMPLETED");

        FetchIrnListResponse listResponse = new FetchIrnListResponse();
        listResponse.setIrnList(invoices);

        when(clearGSTClient.triggerIrnListFetchAsync(any(), any(), any(), any()))
            .thenReturn(Mono.just(triggerResponse));
        when(clearGSTClient.getIrnPullStatus(any(), any()))
            .thenReturn(Mono.just(statusResponse));
        when(clearGSTClient.fetchIrnList(any()))
            .thenReturn(Mono.just(listResponse));
    }

    private void mockSuccessfulGSTResponseWithPagination(List<List<Invoice>> pages) {
        TriggerIrnResponse triggerResponse = new TriggerIrnResponse();
        triggerResponse.setDataPullRequestId("REQ123");

        IrnPullStatusResponse statusResponse = new IrnPullStatusResponse();
        statusResponse.setStatus("COMPLETED");

        when(clearGSTClient.triggerIrnListFetchAsync(any(), any(), any(), any()))
            .thenReturn(Mono.just(triggerResponse));
        when(clearGSTClient.getIrnPullStatus(any(), any()))
            .thenReturn(Mono.just(statusResponse));

        // Mock paginated responses
        for (int i = 0; i < pages.size(); i++) {
            final int pageNumber = i + 1;
            FetchIrnListResponse listResponse = new FetchIrnListResponse();
            listResponse.setIrnList(pages.get(i));
            
            when(clearGSTClient.fetchIrnList(argThat(request -> 
                request != null && 
                request.getPage() == pageNumber)))
                .thenReturn(Mono.just(listResponse));
        }
    }
} 