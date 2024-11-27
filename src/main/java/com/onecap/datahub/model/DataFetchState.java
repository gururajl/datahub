package com.onecap.datahub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "data_fetch_states")
public class DataFetchState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String customerId;
    
    private String accountIdentifier; // GSTIN or Bank Account Number
    
    @Enumerated(EnumType.STRING)
    private DataSourceType sourceType;
    
    private String lastFetchedId;  // IRN for GST, Transaction ID for Bank
    
    private LocalDateTime lastFetchedAt;
    
    private LocalDateTime lastRecordDate; // Invoice date for GST, Transaction date for Bank
    
    @Column(length = 1000)
    private String additionalInfo; // JSON string for any source-specific info
    
    @Version
    private Long version;

    // Default constructor required by JPA
    public DataFetchState() {
    }

    // Constructor with required fields
    public DataFetchState(String customerId, String accountIdentifier, DataSourceType sourceType) {
        this.customerId = customerId;
        this.accountIdentifier = accountIdentifier;
        this.sourceType = sourceType;
        this.lastFetchedAt = LocalDateTime.now();
    }

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

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public void setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
    }

    public DataSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(DataSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getLastFetchedId() {
        return lastFetchedId;
    }

    public void setLastFetchedId(String lastFetchedId) {
        this.lastFetchedId = lastFetchedId;
    }

    public LocalDateTime getLastFetchedAt() {
        return lastFetchedAt;
    }

    public void setLastFetchedAt(LocalDateTime lastFetchedAt) {
        this.lastFetchedAt = lastFetchedAt;
    }

    public LocalDateTime getLastRecordDate() {
        return lastRecordDate;
    }

    public void setLastRecordDate(LocalDateTime lastRecordDate) {
        this.lastRecordDate = lastRecordDate;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
} 