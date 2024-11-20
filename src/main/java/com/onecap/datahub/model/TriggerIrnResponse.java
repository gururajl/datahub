package com.onecap.datahub.model;

import java.util.UUID;

public class TriggerIrnResponse {
    private UUID requestId;
    private String dataPullRequestId;

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public String getDataPullRequestId() {
        return dataPullRequestId;
    }

    public void setDataPullRequestId(String dataPullRequestId) {
        this.dataPullRequestId = dataPullRequestId;
    }
} 