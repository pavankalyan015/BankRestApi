package com.bankapplication.BankRestApi.dto;

import com.bankapplication.BankRestApi.entity.AccountType;
//import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto {

    private Long customerId;

    private String accountHolderName;

    private Double balance;

    private AccountType accountType;

    private Double interestRate;

/*    public BankAccountDto(String accountHolderName, double balance, AccountType accountType, double intrestRate) {
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.accountType = accountType;
        this.intrestRate=intrestRate;
    }*/

}
