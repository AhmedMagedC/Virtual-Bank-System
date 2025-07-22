package com.microservice.transaction.dao;

import com.microservice.transaction.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.transaction.enums.TransactionStatus;

import java.util.List;
import java.util.UUID;

public interface TransactionDao extends JpaRepository<Transactions, UUID> {

    List<Transactions> findAllByFromAccountId(UUID fromAccountId);
    List<Transactions> findAllByToAccountId(UUID toAccountId);
    List<Transactions> findByFromAccountIdAndStatusIn(UUID fromAccountId,
                                                      List<TransactionStatus> statuses);

    List<Transactions> findByToAccountIdAndStatusIn(UUID accountId,
                                                    List<TransactionStatus> success);
}
