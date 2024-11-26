package com.onecap.datahub.service;

import com.onecap.datahub.client.ClearGSTClient;
import com.onecap.datahub.model.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class DataFetchService {
    private final ConsentService consentService;
    private final ClearGSTClient clearGSTClient;
    
    public DataFetchService(ConsentService consentService, ClearGSTClient clearGSTClient) {
        this.consentService = consentService;
        this.clearGSTClient = clearGSTClient;
    }
    
    public Mono<List<Invoice>> fetchGSTInvoices(String customerId, String gstin, ReturnPeriod returnPeriod) {
        if (!consentService.hasValidConsent(customerId, gstin, ConsentType.GSTN)) {
            return Mono.error(new IllegalStateException("No valid consent found for GSTN access"));
        }
        
        return clearGSTClient.triggerIrnListFetchAsync(gstin, returnPeriod, 
                TriggerIrnRequest.SupplierType.B2B, TriggerIrnRequest.InvoiceType.SALES)
            .flatMap(response -> clearGSTClient.getIrnPullStatus(gstin, response.getDataPullRequestId()))
            .flatMap(status -> {
                FetchIrnListRequest request = new FetchIrnListRequest();
                request.setGstin(gstin);
                request.setReturnPeriod(returnPeriod.toString());
                return clearGSTClient.fetchIrnList(request);
            })
            .map(FetchIrnListResponse::getIrnList);
    }
} 