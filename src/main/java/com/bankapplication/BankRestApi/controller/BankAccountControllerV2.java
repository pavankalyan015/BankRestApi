package com.bankapplication.BankRestApi.controller;

import com.bankapplication.BankRestApi.entity.AccountType;
import com.bankapplication.BankRestApi.entity.BankAccount;
import com.bankapplication.BankRestApi.exception.BankAccountNotFoundException;
import com.bankapplication.BankRestApi.repository.BankAccountRepository;
import com.bankapplication.BankRestApi.service.BankAccountService;
import com.bankapplication.BankRestApi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/accounts")
@RequiredArgsConstructor
public class BankAccountControllerV2 {
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;
    private final BankAccountRepository bankAccountRepository;

    @GetMapping(path="/search/type/{accountType}")
    public List<BankAccount> getAccountsOfType(@PathVariable AccountType accountType){
        List<BankAccount> accounts= bankAccountService.getAccountOfType(accountType);
        return accounts;
    }

    @GetMapping("/{id}/interest")
    public Double getInterest(@PathVariable Long id,
                              @RequestParam(defaultValue = "12") int months) {

        BankAccount acc = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("Account -"+id+" not found"));

        double rate = acc.getInterestRate();

        return acc.getBalance() * (rate / 100) * (months / 12.0);
    }
}
