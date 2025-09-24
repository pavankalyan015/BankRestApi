package com.bankapplication.BankRestApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDto {
    private Long fromAccountId;
    private Long toAccountId;
    private double amount;
}
