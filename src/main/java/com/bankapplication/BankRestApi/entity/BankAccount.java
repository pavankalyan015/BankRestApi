package com.bankapplication.BankRestApi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @TableGenerator(
            name = "account_gen",
            table = "id_generator",
            pkColumnName = "gen_name",
            valueColumnName = "gen_value",
            pkColumnValue = "account_id",
            initialValue = 10000000,
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "account_gen")
    private Long accountId;


    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true) // Foreign key in BankAccount table
    @JsonBackReference
    @NotNull(message = "Customer is required")
    private Customer customer;


    @Column(name = "account_holder_name", nullable = false, length = 50)
    @NotBlank(message = "Account holder name is required")
    @Size(min = 5, max = 50, message = "Account holder name must be between 5 and 50 characters")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must not contain numbers or special characters")
    private String accountHolderName;


    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    @NotNull(message = "Account type is required")
    private AccountType accountType;


    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="last_transaction")
    private LocalDateTime lastTransaction;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    @Transient
    @JsonProperty("customerId")
    public Long getCustomerId() {
        return (customer != null) ? customer.getCustomerId() : null;
    }
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();

        if (this.accountType == AccountType.SAVINGS && this.interestRate == null) {
            this.interestRate = 3.5;
        } else if (this.accountType == AccountType.CURRENT) {
            this.interestRate = 0.0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }




}
