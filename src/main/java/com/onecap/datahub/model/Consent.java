package com.onecap.datahub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consents")
public class Consent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String customerId;
    
    @Enumerated(EnumType.STRING)
    private ConsentType consentType;
    
    private String accountIdentifier; // GSTIN or Bank Account Number
    
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    
    private String bankName;  // Only applicable for bank accounts
    
    private String branch;    // Only applicable for bank accounts
    
    @Enumerated(EnumType.STRING)
    private ConsentStatus status;
    
    private LocalDateTime consentGrantedAt;
    
    private LocalDateTime consentExpiresAt;
    
    private String consentArtifact; // Store any consent-related documents/tokens
    
    @Version
    private Long version;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ConsentType getConsentType() {
        return consentType;
    }

    public void setConsentType(ConsentType consentType) {
        this.consentType = consentType;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public ConsentStatus getStatus() {
        return status;
    }

    public void setStatus(ConsentStatus status) {
        this.status = status;
    }

    public LocalDateTime getConsentGrantedAt() {
        return consentGrantedAt;
    }

    public void setConsentGrantedAt(LocalDateTime consentGrantedAt) {
        this.consentGrantedAt = consentGrantedAt;
    }

    public LocalDateTime getConsentExpiresAt() {
        return consentExpiresAt;
    }

    public void setConsentExpiresAt(LocalDateTime consentExpiresAt) {
        this.consentExpiresAt = consentExpiresAt;
    }

    public String getConsentArtifact() {
        return consentArtifact;
    }

    public void setConsentArtifact(String consentArtifact) {
        this.consentArtifact = consentArtifact;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
} 