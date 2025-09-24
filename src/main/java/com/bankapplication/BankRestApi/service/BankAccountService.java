package com.bankapplication.BankRestApi.service;

import com.bankapplication.BankRestApi.entity.*;
import com.bankapplication.BankRestApi.exception.*;
//import com.bankapplication.BankRestApi.model.BankAccountModel;
import com.bankapplication.BankRestApi.repository.BankAccountRepository;
import com.bankapplication.BankRestApi.repository.CustomerRepository;
import com.bankapplication.BankRestApi.repository.TransactionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bankapplication.BankRestApi.exception.BankAccountNotFoundException;

//import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;


    public void addAccount(BankAccount bankAccount){
        Long cId = bankAccount.getCustomer().getCustomerId();
        Optional<Customer> cs= customerRepository.findById(cId);
        if(cs.isEmpty()){
            throw new NotACustomerException(cId+" - is a not a customer");
        }
        else {
            if(bankAccount.getBalance()<1000)
                throw new MinimumBalanceExpection("Minimum Balance Should be 1000");
            else {
                bankAccount.setLastTransaction(LocalDateTime.now());
                bankAccountRepository.save(bankAccount);
                Customer customer = cs.get();
                List<BankAccount> accounts = customer.getAccounts();
                accounts.add(bankAccount);
                customer.setAccounts(accounts);
                customerRepository.save(customer);
                //customerService.upDateCustomer(customer.getCustomerId(),customer);
            }
        }
    }

    public List<BankAccount> getAllAccountsDetails(){
        return bankAccountRepository.findAll();
    }


    public BankAccount getAccountById(Long id) throws BankAccountNotFoundException {
        Optional<BankAccount> account= bankAccountRepository.findById(id);
        //BankAccount bankAccount=account.get();
        if(account.isEmpty()){
            throw new BankAccountNotFoundException(id+"-is not found");
        }
        else {
            return account.get();
        }
    }



    public void deleteAccountById(Long id) throws  BankAccountNotFoundException{
        Optional<BankAccount> account= bankAccountRepository.findById(id);
        if(account.isEmpty()){
            throw new BankAccountNotFoundException(id+"-is not found");
        }
        else {
            bankAccountRepository.deleteById(id);
        }
    }

    public void updateAccount(Long id, @Valid BankAccount account){
        Optional<BankAccount> acc = bankAccountRepository.findById(id);
        if(acc.isPresent()){
            BankAccount bankAccount = acc.get();
            bankAccount.setAccountHolderName(account.getAccountHolderName());
            bankAccount.setAccountType(account.getAccountType());
            //bankAccount.setCustomer(account.getCustomer());
            bankAccount.setBalance(account.getBalance());
            bankAccount.setInterestRate(account.getInterestRate());
            bankAccount.setUpdatedAt(LocalDateTime.now());
            bankAccountRepository.save(bankAccount);
        }
        else{
            throw new BankAccountNotFoundException("Bank account with id -"+id+"is not found");
        }
    }

    public Optional<BankAccount> checkBalance(Long id){
        Double res;
        Optional<BankAccount> acc = bankAccountRepository.findById(id);
        if(acc.isPresent()){
            return acc;
        }
        else
            throw new BankAccountNotFoundException("Account with id -"+id+" is not found");
    }

    public BankAccount deposit(Long id,double newAmount) throws BankAccountNotFoundException, HighValueTransactionException {
        if(newAmount>100000)
            throw new HighValueTransactionException("Cannot be deposited more than 100000");

        Optional<BankAccount> account= bankAccountRepository.findById(id);
        if(account.isPresent()){

            //Transaction for deposit
            Transaction transaction = new Transaction();
            transaction.setToAccountId(account.get().getAccountId());
            transaction.setAmount(newAmount);
            transaction.setInitialBalance(account.get().getBalance());
            transaction.setRemainingBalance(account.get().getBalance()+newAmount);
            transaction.setTransactionType(TransactionType.DEPOSIT);
            transaction.setStatus(Status.SUCCESS);
            transactionRepository.save(transaction);

            BankAccount bankAccount= account.get();
            double currentBalance=bankAccount.getBalance();
            double newBalance=currentBalance+newAmount;
            bankAccount.setBalance(newBalance);
            bankAccount.setLastTransaction(LocalDateTime.now());
            bankAccountRepository.save(bankAccount);
            return bankAccount;
        }
        else{
            throw new BankAccountNotFoundException("Account - "+id+" not found");
        }
    }

    public BankAccount withdraw(Long id, double amount) throws InsufficientFundsException,BankAccountNotFoundException{
        Optional<BankAccount> account = bankAccountRepository.findById(id);
        if(account.isPresent()){
            BankAccount bankAccount=account.get();

            //Transaction for withdraw
            Transaction transaction = new Transaction();
            transaction.setFromAccountId(bankAccount.getAccountId());
            transaction.setAmount(amount);
            transaction.setInitialBalance(bankAccount.getBalance());
            transaction.setRemainingBalance(bankAccount.getBalance()-amount);
            transaction.setTransactionType(TransactionType.WITHDRAW);
            if(bankAccount.getAccountType()==AccountType.SAVINGS && bankAccount.getBalance()<amount)
            {
                transaction.setStatus(Status.FAILED);
            }
            else if(bankAccount.getAccountType()==AccountType.CURRENT && (bankAccount.getBalance()+5000)<amount){
                transaction.setStatus(Status.FAILED);
            }
            else{
                transaction.setStatus(Status.SUCCESS);
            }
            transactionRepository.save(transaction);

            double currentBalance= bankAccount.getBalance();

            if(transaction.getStatus()==Status.FAILED) {
                throw new InsufficientFundsException("Withdraw amount exceeds avialable amount: " + currentBalance);
            }
            else{
                bankAccount.setBalance(currentBalance-amount);
                bankAccount.setLastTransaction(LocalDateTime.now());
                if(bankAccount.getAccountType()==AccountType.SAVINGS && amount==currentBalance)
                    bankAccount.setStatus(AccountStatus.CLOSED);
                else if(bankAccount.getAccountType()==AccountType.CURRENT && (currentBalance+5000)==amount)
                    bankAccount.setStatus(AccountStatus.CLOSED);

                bankAccountRepository.save(bankAccount);
                return bankAccount;
            }
        }
        else{
            throw new BankAccountNotFoundException("Account - "+id+" not found");
        }
    }


    public List<Transaction> getAllTransactions(Long accountId) {
        bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found: " + accountId));
        return transactionRepository.findTransactionsByAccount(accountId);
    }


    public List<BankAccount> getTopAccountsByBalance() {
        return bankAccountRepository.findTop5AccountsByBalance();
    }


    public List<BankAccount> getAccountOfType(AccountType accountType){
        return bankAccountRepository.findAccountsByType(accountType);
    }

}
