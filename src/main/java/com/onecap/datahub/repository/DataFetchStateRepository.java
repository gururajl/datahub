package com.onecap.datahub.repository;

import com.onecap.datahub.model.DataFetchState;
import com.onecap.datahub.model.DataSourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataFetchStateRepository extends JpaRepository<DataFetchState, Long> {
    Optional<DataFetchState> findByCustomerIdAndAccountIdentifierAndSourceType(
        String customerId, 
        String accountIdentifier, 
        DataSourceType sourceType
    );
} 