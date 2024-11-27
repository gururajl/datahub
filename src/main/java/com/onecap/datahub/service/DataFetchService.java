package com.onecap.datahub.service;

import com.onecap.datahub.client.ClearGSTClient;
import com.onecap.datahub.model.*;
import com.onecap.datahub.repository.DataFetchStateRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataFetchService {
    private final ConsentService consentService;
    private final ClearGSTClient clearGSTClient;
    private final DataFetchStateRepository fetchStateRepository;
    private static final int PAGE_SIZE = 10;
    
    public DataFetchService(ConsentService consentService, 
                          ClearGSTClient clearGSTClient,
                          DataFetchStateRepository fetchStateRepository) {
        this.consentService = consentService;
        this.clearGSTClient = clearGSTClient;
        this.fetchStateRepository = fetchStateRepository;
    }
    
    public Mono<List<Invoice>> fetchIncrementalInvoices(String customerId, String gstin) {
        if (!consentService.hasValidConsent(customerId, gstin, ConsentType.GSTN)) {
            return Mono.error(new IllegalStateException("No valid consent found for GSTN access"));
        }

        DataFetchState fetchState = fetchStateRepository
            .findByCustomerIdAndAccountIdentifierAndSourceType(customerId, gstin, DataSourceType.GST_INVOICE)
            .orElseGet(() -> createNewFetchState(customerId, gstin, DataSourceType.GST_INVOICE));

        ReturnPeriod currentPeriod = ReturnPeriod.fromYearMonth(YearMonth.now());
        
        return fetchInvoicesRecursively(gstin, currentPeriod, fetchState, 1, new ArrayList<>())
            .flatMap(invoices -> {
                if (!invoices.isEmpty()) {
                    // Update fetch state with latest invoice details
                    Invoice latestInvoice = invoices.get(0);
                    fetchState.setLastFetchedId(latestInvoice.getIrn());
                    fetchState.setLastFetchedAt(LocalDateTime.now());
                    fetchState.setLastRecordDate(parseInvoiceDate(latestInvoice));
                    fetchStateRepository.save(fetchState);
                }
                return Mono.just(invoices);
            });
    }

    private Mono<List<Invoice>> fetchInvoicesRecursively(String gstin, 
                                                        ReturnPeriod returnPeriod,
                                                        DataFetchState fetchState, 
                                                        int pageNo,
                                                        List<Invoice> accumulatedInvoices) {
        return clearGSTClient.triggerIrnListFetchAsync(gstin, returnPeriod, 
                TriggerIrnRequest.SupplierType.B2B, TriggerIrnRequest.InvoiceType.SALES)
            .flatMap(response -> clearGSTClient.getIrnPullStatus(gstin, response.getDataPullRequestId()))
            .flatMap(status -> {
                FetchIrnListRequest request = new FetchIrnListRequest();
                request.setGstin(gstin);
                request.setReturnPeriod(returnPeriod.toString());
                request.setPage(pageNo);
                request.setSize(PAGE_SIZE);
                return clearGSTClient.fetchIrnList(request);
            })
            .flatMap(response -> {
                List<Invoice> currentPageInvoices = response.getIrnList();
                accumulatedInvoices.addAll(currentPageInvoices);

                // Check if we've reached the last fetched IRN or end of list
                boolean reachedLastFetch = currentPageInvoices.stream()
                    .anyMatch(inv -> inv.getIrn().equals(fetchState.getLastFetchedId()));
                boolean hasMorePages = currentPageInvoices.size() == PAGE_SIZE;

                if (!reachedLastFetch && hasMorePages) {
                    // Recursively fetch next page
                    return fetchInvoicesRecursively(gstin, returnPeriod, fetchState, 
                                                  pageNo + 1, accumulatedInvoices);
                }

                return Mono.just(accumulatedInvoices);
            });
    }

    private DataFetchState createNewFetchState(String customerId, String accountIdentifier, DataSourceType sourceType) {
        DataFetchState newState = new DataFetchState();
        newState.setCustomerId(customerId);
        newState.setAccountIdentifier(accountIdentifier);
        newState.setSourceType(sourceType);
        newState.setLastFetchedAt(LocalDateTime.now());
        return newState;
    }

    private LocalDateTime parseInvoiceDate(Invoice invoice) {
        return LocalDateTime.parse(invoice.getDocumentDetails().getDocumentDate());
    }
} 