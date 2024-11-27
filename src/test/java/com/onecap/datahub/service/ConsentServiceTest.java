package com.onecap.datahub.service;

import com.onecap.datahub.config.TestConfig;
import com.onecap.datahub.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
@Transactional
class ConsentServiceTest {

    @Autowired
    private ConsentService consentService;

    @Test
    void testCreateGSTConsent() {
        String customerId = "CUST001";
        String gstin = "29ABCDE1234F1Z5";
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(30);
        
        Consent consent = consentService.createGSTConsent(
            customerId, 
            gstin, 
            expiresAt,
            "consent-doc-123"
        );
        
        assertNotNull(consent.getId());
        assertEquals(ConsentType.GSTN, consent.getConsentType());
        assertEquals(AccountType.GST_ACCOUNT, consent.getAccountType());
        assertEquals(ConsentStatus.ACTIVE, consent.getStatus());
        assertEquals(gstin, consent.getAccountIdentifier());
    }

    @Test
    void testCreateBankConsent() {
        String customerId = "CUST001";
        String accountNumber = "1234567890";
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(90);
        
        Consent consent = consentService.createBankConsent(
            customerId,
            accountNumber,
            AccountType.SAVINGS_ACCOUNT,
            "HDFC Bank",
            "Mumbai Main",
            expiresAt,
            "consent-doc-456"
        );
        
        assertNotNull(consent.getId());
        assertEquals(ConsentType.BANK_ACCOUNT, consent.getConsentType());
        assertEquals(AccountType.SAVINGS_ACCOUNT, consent.getAccountType());
        assertEquals(ConsentStatus.ACTIVE, consent.getStatus());
        assertEquals("HDFC Bank", consent.getBankName());
    }

    @Test
    void testHasValidConsent() {
        // Create a consent first
        String customerId = "CUST001";
        String gstin = "29ABCDE1234F1Z5";
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(30);
        
        consentService.createGSTConsent(
            customerId, 
            gstin, 
            expiresAt,
            "consent-doc-123"
        );
        
        // Test valid consent
        boolean hasConsent = consentService.hasValidConsent(customerId, gstin, ConsentType.GSTN);
        assertTrue(hasConsent);
        
        // Test invalid consent
        boolean hasInvalidConsent = consentService.hasValidConsent(
            "INVALID_CUST", gstin, ConsentType.GSTN);
        assertFalse(hasInvalidConsent);
    }

    @Test
    void testRevokeConsent() {
        // Create a consent first
        String customerId = "CUST001";
        String gstin = "29ABCDE1234F1Z5";
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(30);
        
        consentService.createGSTConsent(
            customerId, 
            gstin, 
            expiresAt,
            "consent-doc-123"
        );
        
        // Revoke the consent
        consentService.revokeConsent(customerId, gstin, ConsentType.GSTN);
        
        // Verify consent is revoked
        boolean hasConsent = consentService.hasValidConsent(customerId, gstin, ConsentType.GSTN);
        assertFalse(hasConsent);
    }

    @Test
    void testGetActiveConsents() {
        String customerId = "CUST001";
        
        // Create multiple consents
        consentService.createGSTConsent(
            customerId,
            "29ABCDE1234F1Z5",
            LocalDateTime.now().plusDays(30),
            "consent-doc-123"
        );
        
        consentService.createBankConsent(
            customerId,
            "1234567890",
            AccountType.SAVINGS_ACCOUNT,
            "HDFC Bank",
            "Mumbai Main",
            LocalDateTime.now().plusDays(90),
            "consent-doc-456"
        );
        
        List<Consent> activeConsents = consentService.getActiveConsents(customerId);
        assertEquals(2, activeConsents.size());
    }
} 