package com.bankapplication.BankRestApi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID transactionId;

    @Column(name = "from_account_id")
    @Positive(message = "fromAccountId must be positive")
    private Long fromAccountId;


    @Column(name = "to_account_id")
    @Positive(message = "toAccountId must be positive")
    private Long toAccountId;


    @Column(name = "amount",nullable = false)
    @Positive(message = "amount must be positive")
    private double amount;

    @Column(name = "initial_balance",nullable = false)
    private double initialBalance;

    @Column(name = "remaining_balance", nullable = false)
    private double remainingBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type",nullable = false)
    private TransactionType transactionType;

    @Column(name = "time_stamp",nullable = false)
    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @PrePersist
    protected void onCreate() {
        this.timeStamp = LocalDateTime.now();
    }

}
