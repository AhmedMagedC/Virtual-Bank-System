package com.microservice.transaction.dao;

import com.microservice.transaction.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionDao extends JpaRepository<Transactions, UUID> {

    List<Transactions> findAllByFromAccountId(UUID fromAccountId);
    List<Transactions> findAllByToAccountId(UUID toAccountId);

}
