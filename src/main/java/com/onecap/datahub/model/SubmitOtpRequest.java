package com.onecap.datahub.model;

public class SubmitOtpRequest {
    private String otp;

    public SubmitOtpRequest(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
} 