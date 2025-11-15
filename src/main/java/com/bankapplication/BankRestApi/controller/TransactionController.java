package com.bankapplication.BankRestApi.controller;

import com.bankapplication.BankRestApi.entity.Transaction;
import com.bankapplication.BankRestApi.dto.TransferRequestDto;
//import com.bankapplication.BankRestApi.model.TransferRequestModel;
import com.bankapplication.BankRestApi.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class TransactionController {

   // @Autowired
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping(path="accounts/transfer")
    public void transferAmount(@RequestBody TransferRequestDto transferRequest){
        transactionService.transfer(transferRequest.getFromAccountId(),transferRequest.getToAccountId(),transferRequest.getAmount());

    }

    @GetMapping(path="transactions")
    public List<Transaction> getAllTransactions(){
        return transactionService.getAllTransactions();

    }

    @GetMapping(path="transactions/{id}")
    public Transaction getTransactionById(@PathVariable UUID id){
        return transactionService.getTransactionById(id);

    }

}
