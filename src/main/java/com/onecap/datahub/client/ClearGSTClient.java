package com.onecap.datahub.client;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.onecap.datahub.model.AddCredentialsRequest;
import com.onecap.datahub.model.AddCredentialsResponse;
import com.onecap.datahub.model.OtpRequestResponse;
import com.onecap.datahub.model.SubmitOtpRequest;
import com.onecap.datahub.model.SubmitOtpResponse;
import com.onecap.datahub.model.TriggerIrnRequest;
import com.onecap.datahub.model.TriggerIrnResponse;
import com.onecap.datahub.model.ReturnPeriod;
import com.onecap.datahub.model.IrnPullStatusResponse;

import reactor.core.publisher.Mono;

@Component
public class ClearGSTClient {
    private final WebClient webClient;
    
    public ClearGSTClient(WebClient clearWebClient) {
        this.webClient = clearWebClient;
    }

    // Synchronous version
    public AddCredentialsResponse addCredentials(String gstin, String username) {
        return webClient.post()
                .uri("/clearIdentity/v1/gst-auth/add-credentials")
                .header("gstin", gstin)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AddCredentialsRequest(username))
                .retrieve()
                .bodyToMono(AddCredentialsResponse.class)
                .block(); // Makes it synchronous
    }

    // Asynchronous version
    public Mono<AddCredentialsResponse> addCredentialsAsync(String gstin, String username) {
        return webClient.post()
                .uri("/clearIdentity/v1/gst-auth/add-credentials")
                .header("gstin", gstin)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AddCredentialsRequest(username))
                .retrieve()
                .bodyToMono(AddCredentialsResponse.class);
    }

    // Synchronous version for requesting OTP
    public OtpRequestResponse requestOtp(String gstin) {
        return webClient.post()
                .uri("/clearIdentity/v1/gst-auth/otp/request")
                .header("gstin", gstin)
                .retrieve()
                .bodyToMono(OtpRequestResponse.class)
                .block(); // Makes it synchronous
    }

    // Asynchronous version for requesting OTP
    public Mono<OtpRequestResponse> requestOtpAsync(String gstin) {
        return webClient.post()
                .uri("/clearIdentity/v1/gst-auth/otp/request")
                .header("gstin", gstin)
                .retrieve()
                .bodyToMono(OtpRequestResponse.class);
    }

    // Synchronous version for submitting OTP
    public SubmitOtpResponse submitOtp(String gstin, String otp) {
        return webClient.post()
                .uri("/clearIdentity/v1/gst-auth/otp/submit")
                .header("gstin", gstin)
                .bodyValue(new SubmitOtpRequest(otp))
                .retrieve()
                .bodyToMono(SubmitOtpResponse.class)
                .block(); // Makes it synchronous
    }

    // Asynchronous version for submitting OTP
    public Mono<SubmitOtpResponse> submitOtpAsync(String gstin, String otp) {
        return webClient.post()
                .uri("/clearIdentity/v1/gst-auth/otp/submit")
                .header("gstin", gstin)
                .bodyValue(new SubmitOtpRequest(otp))
                .retrieve()
                .bodyToMono(SubmitOtpResponse.class);
    }

    // Synchronous version to trigger IRN list fetch
    public TriggerIrnResponse triggerIrnListFetch(String gstin, ReturnPeriod returnPeriod, TriggerIrnRequest.SupplierType supplierType, TriggerIrnRequest.InvoiceType invoiceType) {
        return webClient.post()
                .uri("/clearIdentity/v1/einvoices/create-pull-request")
                .bodyValue(new TriggerIrnRequest(gstin, returnPeriod, supplierType, invoiceType))
                .retrieve()
                .bodyToMono(TriggerIrnResponse.class)
                .block(); // Makes it synchronous
    }

    // Asynchronous version to trigger IRN list fetch
    public Mono<TriggerIrnResponse> triggerIrnListFetchAsync(String gstin, ReturnPeriod returnPeriod, TriggerIrnRequest.SupplierType supplierType, TriggerIrnRequest.InvoiceType invoiceType) {
        return webClient.post()
                .uri("/clearIdentity/v1/einvoices/create-pull-request")
                .bodyValue(new TriggerIrnRequest(gstin, returnPeriod, supplierType, invoiceType))
                .retrieve()
                .bodyToMono(TriggerIrnResponse.class);
    }

    public Mono<IrnPullStatusResponse> getIrnPullStatus(String gstin, String dataPullRequestId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/clearIdentity/v1/einvoices/fetch-pull-response/{gstin}/{dataPullRequestId}")
                    .build(gstin, dataPullRequestId))
                .retrieve()
                .bodyToMono(IrnPullStatusResponse.class);
    }
}