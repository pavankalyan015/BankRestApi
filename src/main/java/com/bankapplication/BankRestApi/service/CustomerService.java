package com.bankapplication.BankRestApi.service;


import com.bankapplication.BankRestApi.entity.Customer;
import com.bankapplication.BankRestApi.exception.BankAccountNotFoundException;
import com.bankapplication.BankRestApi.exception.CustomerNotFoundException;
import com.bankapplication.BankRestApi.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//import static jdk.internal.org.jline.reader.impl.LineReaderImpl.CompletionType.List;

@Service
public class CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createCustomer(Customer customer){
        customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id){
        Optional<Customer> customer1 = customerRepository.findById(id);
        if(customer1.isEmpty()){
            throw new BankAccountNotFoundException("Customer with id- "+id+" is not exists");
        }else {
            return customer1.get();
        }
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public void upDateCustomer(Long id, Customer customer){
        Optional<Customer> customer1= customerRepository.findById(id);
        if(customer1.isPresent()){
            Customer existedCustomer = customer1.get();
            existedCustomer.setName(customer.getName());
            existedCustomer.setPan(customer.getPan());
            existedCustomer.setPhone(customer.getPhone());
            existedCustomer.setEmail(customer.getEmail());
            existedCustomer.setAccounts(customer.getAccounts());
            existedCustomer.setUpdatedAt(LocalDateTime.now());
            customerRepository.save(existedCustomer);
        }
        else{
            throw new CustomerNotFoundException("Customer with id - "+id+" not found");
        }
    }

}
