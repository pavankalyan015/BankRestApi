package com.bankapplication.BankRestApi.repository;

import com.bankapplication.BankRestApi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = "SELECT * FROM transaction " +
            "WHERE from_account_id = :accountId " +
            "   OR to_account_id = :accountId " +
            "ORDER BY time_stamp DESC",
            nativeQuery = true)
    List<Transaction> findTransactionsByAccount(@Param("accountId") Long accountId);
}
