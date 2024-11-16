package com.onecap.datahub.model;

public class AddCredentialsRequest {
    private String username;

    public AddCredentialsRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
} 