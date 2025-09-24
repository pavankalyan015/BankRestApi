package com.bankapplication.BankRestApi.controller;

import com.bankapplication.BankRestApi.entity.Customer;
import com.bankapplication.BankRestApi.dto.CustomerDto;
import com.bankapplication.BankRestApi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    @Autowired
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path="/")
    public String bankApplication(){
        return "Bank Rest Application";
    }

    @PostMapping(path="/create")
    public void createCustomer(@RequestBody CustomerDto customerDto){
        Customer customer = new Customer();
        customer.setName(customerDto.getCustomerName());
        customer.setPan(customerDto.getPan());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());
        customerService.createCustomer(customer);
       // customerService.createCustomer(customer);
    }

    @GetMapping(path="/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        Customer customer= customerService.getCustomerById(id);
        return customer;
    }

    @GetMapping(path="/all" )
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @PutMapping(path = "/update/{id}")
    public void updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        //Long Cid = customer.getCustomerId();
        customerService.upDateCustomer(id,customer);
    }

}
