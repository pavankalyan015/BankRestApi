package com.bankapplication.BankRestApi.repository;

import com.bankapplication.BankRestApi.entity.AccountType;
import com.bankapplication.BankRestApi.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
    @Query(value = "SELECT * FROM bank_account " +
            "ORDER BY balance DESC " +
            "LIMIT 5",
            nativeQuery = true)
    List<BankAccount> findTop5AccountsByBalance();

    @Query("SELECT a FROM BankAccount a WHERE a.accountType = :accountType")
    List<BankAccount> findAccountsByType(@Param("accountType") AccountType accountType);

}
