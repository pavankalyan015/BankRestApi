package com.bankapplication.BankRestApi.service;

import com.bankapplication.BankRestApi.entity.*;
import com.bankapplication.BankRestApi.exception.BankAccountNotFoundException;
import com.bankapplication.BankRestApi.exception.HighValueTransactionException;
import com.bankapplication.BankRestApi.exception.InsufficientFundsException;
import com.bankapplication.BankRestApi.exception.TransactionNotFoundException;
import com.bankapplication.BankRestApi.repository.BankAccountRepository;
import com.bankapplication.BankRestApi.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    private final TransactionRepository transactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;


    public TransactionService(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository=bankAccountRepository;
    }

    public void createTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    public void transfer(Long fromAccountId, Long toAccountId, double amount){
        BankAccount sourceBank = bankAccountRepository.findById(fromAccountId).orElseThrow(()->new BankAccountNotFoundException("Source Account :"+fromAccountId+" not found"));
        BankAccount destinationBank = bankAccountRepository.findById(toAccountId).orElseThrow(()->new BankAccountNotFoundException("Destination Account :"+toAccountId+" not found"));

        if(amount>100000)
            throw new HighValueTransactionException("Cannot be transfered more than 100000");

        Transaction transaction = new Transaction();
        transaction.setFromAccountId(fromAccountId);
        transaction.setToAccountId(toAccountId);
        transaction.setAmount(amount);
        transaction.setInitialBalance(sourceBank.getBalance());
        transaction.setRemainingBalance(sourceBank.getBalance()-amount);
        transaction.setTransactionType(TransactionType.TRANSFER);
        if(sourceBank.getAccountType()== AccountType.SAVINGS&&sourceBank.getBalance()<amount)
            transaction.setStatus(Status.FAILED);
        else if(sourceBank.getAccountType()==AccountType.CURRENT && (sourceBank.getBalance()+5000)>amount){
            transaction.setStatus(Status.FAILED);
        }
        else{
            transaction.setStatus(Status.SUCCESS);
        }
        transactionRepository.save(transaction);

        if(transaction.getStatus()==Status.FAILED)
            throw new InsufficientFundsException("Insufficient Funds in Source Account :"+sourceBank.getBalance());
        else {
            sourceBank.setBalance(sourceBank.getBalance() - amount);
            sourceBank.setLastTransaction(LocalDateTime.now());

            if(sourceBank.getAccountType()==AccountType.SAVINGS && (sourceBank.getBalance()==amount))
                sourceBank.setStatus(AccountStatus.CLOSED);
            if(sourceBank.getAccountType()==AccountType.CURRENT && ((sourceBank.getBalance()+5000)<amount))
                sourceBank.setStatus(AccountStatus.CLOSED);
            bankAccountRepository.save(sourceBank);

            destinationBank.setLastTransaction(LocalDateTime.now());
            destinationBank.setBalance(destinationBank.getBalance() + amount);
            bankAccountRepository.save(destinationBank);
        }

    }


    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }


    public Transaction getTransactionById(UUID transactionId){
        Optional<Transaction> transaction=transactionRepository.findById(transactionId);
        if(transaction.isPresent()){
            return transaction.get();
        }
        else{
            throw new TransactionNotFoundException(transactionId+"- is not found");
        }
    }


}
