package com.onecap.datahub.service;

import com.onecap.datahub.model.*;
import com.onecap.datahub.repository.ConsentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsentService {
    private final ConsentRepository consentRepository;
    
    public ConsentService(ConsentRepository consentRepository) {
        this.consentRepository = consentRepository;
    }
    
    @Transactional
    public Consent createGSTConsent(String customerId, String gstin, 
                                  LocalDateTime expiresAt, String consentArtifact) {
        Consent consent = new Consent();
        consent.setCustomerId(customerId);
        consent.setConsentType(ConsentType.GSTN);
        consent.setAccountType(AccountType.GST_ACCOUNT);
        consent.setAccountIdentifier(gstin);
        consent.setStatus(ConsentStatus.ACTIVE);
        consent.setConsentGrantedAt(LocalDateTime.now());
        consent.setConsentExpiresAt(expiresAt);
        consent.setConsentArtifact(consentArtifact);
        
        return consentRepository.save(consent);
    }
    
    @Transactional
    public Consent createBankConsent(String customerId, String accountNumber, 
                                   AccountType accountType, String bankName, 
                                   String branch, LocalDateTime expiresAt, 
                                   String consentArtifact) {
        Consent consent = new Consent();
        consent.setCustomerId(customerId);
        consent.setConsentType(ConsentType.BANK_ACCOUNT);
        consent.setAccountType(accountType);
        consent.setAccountIdentifier(accountNumber);
        consent.setBankName(bankName);
        consent.setBranch(branch);
        consent.setStatus(ConsentStatus.ACTIVE);
        consent.setConsentGrantedAt(LocalDateTime.now());
        consent.setConsentExpiresAt(expiresAt);
        consent.setConsentArtifact(consentArtifact);
        
        return consentRepository.save(consent);
    }
    
    @Transactional(readOnly = true)
    public boolean hasValidConsent(String customerId, String accountIdentifier, ConsentType type) {
        return consentRepository.findByCustomerIdAndAccountIdentifierAndConsentType(
                customerId, accountIdentifier, type)
            .map(consent -> {
                LocalDateTime now = LocalDateTime.now();
                return consent.getStatus() == ConsentStatus.ACTIVE &&
                       consent.getConsentExpiresAt().isAfter(now);
            })
            .orElse(false);
    }
    
    @Transactional
    public void revokeConsent(String customerId, String accountIdentifier, ConsentType type) {
        consentRepository.findByCustomerIdAndAccountIdentifierAndConsentType(
                customerId, accountIdentifier, type)
            .ifPresent(consent -> {
                consent.setStatus(ConsentStatus.REVOKED);
                consentRepository.save(consent);
            });
    }
    
    @Transactional(readOnly = true)
    public List<Consent> getActiveConsents(String customerId) {
        return consentRepository.findByCustomerIdAndStatus(customerId, ConsentStatus.ACTIVE);
    }
    
    @Transactional(readOnly = true)
    public List<Consent> getActiveBankConsents(String customerId) {
        return consentRepository.findByCustomerIdAndConsentTypeAndStatus(
            customerId, ConsentType.BANK_ACCOUNT, ConsentStatus.ACTIVE);
    }
    
    @Transactional(readOnly = true)
    public List<Consent> getActiveGSTConsents(String customerId) {
        return consentRepository.findByCustomerIdAndConsentTypeAndStatus(
            customerId, ConsentType.GSTN, ConsentStatus.ACTIVE);
    }
} 