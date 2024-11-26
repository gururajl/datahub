package com.onecap.datahub.repository;

import com.onecap.datahub.model.Consent;
import com.onecap.datahub.model.ConsentType;
import com.onecap.datahub.model.ConsentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, Long> {
    List<Consent> findByCustomerIdAndStatus(String customerId, ConsentStatus status);
    
    List<Consent> findByCustomerIdAndConsentTypeAndStatus(
        String customerId, 
        ConsentType consentType, 
        ConsentStatus status
    );
    
    Optional<Consent> findByCustomerIdAndAccountIdentifierAndConsentType(
        String customerId, 
        String accountIdentifier, 
        ConsentType consentType
    );
    
    List<Consent> findByConsentType(ConsentType consentType);
} 