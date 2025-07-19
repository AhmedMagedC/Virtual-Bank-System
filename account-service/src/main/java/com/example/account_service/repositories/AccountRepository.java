package com.example.account_service.repositories;

import com.example.account_service.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query(value = "SELECT nextval('account_number_seq')", nativeQuery = true)
    String getNextAccountNumberValue();

    List<Account> findByUserId(UUID userId);
}
