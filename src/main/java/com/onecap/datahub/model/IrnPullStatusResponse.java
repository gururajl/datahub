package com.onecap.datahub.model;

import java.util.List;

public class IrnPullStatusResponse {
    private String requestId;
    private String status;
    private List<String> generatedUrls;

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getGeneratedUrls() {
        return generatedUrls;
    }

    public void setGeneratedUrls(List<String> generatedUrls) {
        this.generatedUrls = generatedUrls;
    }
} 