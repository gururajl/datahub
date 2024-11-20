package com.onecap.datahub.model;

import java.util.List;

public class FetchIrnListResponse {
    private String requestId;
    private List<Invoice> irnList;

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<Invoice> getIrnList() {
        return irnList;
    }

    public void setIrnList(List<Invoice> irnList) {
        this.irnList = irnList;
    }
} 