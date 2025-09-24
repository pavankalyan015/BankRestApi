package com.bankapplication.BankRestApi.controller;

import com.bankapplication.BankRestApi.entity.BankAccount;
import com.bankapplication.BankRestApi.entity.Customer;
import com.bankapplication.BankRestApi.entity.Transaction;
import com.bankapplication.BankRestApi.exception.NotACustomerException;
import com.bankapplication.BankRestApi.dto.BankAccountDto;
import com.bankapplication.BankRestApi.service.BankAccountService;
import com.bankapplication.BankRestApi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;


    @PostMapping(path = "/create")
    public void createAccount(@RequestBody BankAccountDto accountDto) {

        Customer customer = customerService.getCustomerById(accountDto.getCustomerId());

        if (customer==null)
            throw new NotACustomerException(accountDto.getCustomerId() + " is not a customer");
        else {

            //Customer customer = cus.get();
            System.out.println("--------Interest rate - "+accountDto.getInterestRate());

            BankAccount bankAccount = new BankAccount();
            bankAccount.setCustomer(customer);
            bankAccount.setAccountHolderName(accountDto.getAccountHolderName());
            bankAccount.setBalance(accountDto.getBalance());
            bankAccount.setAccountType(accountDto.getAccountType());
            bankAccount.setInterestRate(accountDto.getInterestRate());
            // if(accountDto.getAccountType()== AccountType.SAVINGS && )
            bankAccountService.addAccount(bankAccount);
            //customer.setAccounts(customer.getAccounts().add(bankAccount));


        }
    }

    @GetMapping(path="/all")
    public List<BankAccount> getAllAccounts(){
        return bankAccountService.getAllAccountsDetails();
    }


    @GetMapping(path="/{id}")
    public BankAccount getAccountById(@PathVariable Long id){
        BankAccount account = bankAccountService.getAccountById(id);
        return account;
    }


    @DeleteMapping(path="/{id}")
    public void deleteAccount(@PathVariable Long id){
            bankAccountService.deleteAccountById(id);
    }
    @PutMapping(path = "/{id}/update")
    public void  upDateAccount(@PathVariable Long id, @RequestBody  BankAccount account){
        bankAccountService.updateAccount(id, account);


    }

    @GetMapping(path="/{id}/balance")
    public Double checkBalance(@PathVariable Long id){
        Optional<BankAccount> ac= bankAccountService.checkBalance(id);

        return ac.get().getBalance();
    }


    @PutMapping(path = "/{id}/withdraw")
    public BankAccount withDraw(@PathVariable Long id, @RequestParam double amount){
        return bankAccountService.withdraw(id,amount);

    }

    @PutMapping(path="/{id}/deposit")
    public BankAccount deposit(@PathVariable Long id, @RequestParam double amount){
        return bankAccountService.deposit(id,amount);
    }

    @GetMapping(path = "/{id}/transactions")
    public List<Transaction> transactionListOfAccount(@PathVariable Long id){
        return bankAccountService.getAllTransactions(id);
    }

    @GetMapping(path = "/report/topaccounts")
    public List<BankAccount> top5Account(){
         return bankAccountService.getTopAccountsByBalance();
    }





}
