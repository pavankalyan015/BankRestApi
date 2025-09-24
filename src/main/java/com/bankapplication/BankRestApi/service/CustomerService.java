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


    @PostConstruct
    public void preCustomerCreation() {
        if (customerRepository.count() == 0) {
            Customer cc1 = new Customer();
            cc1.setName("Pavan Kalyan");
            cc1.setPan("H1234567");
            cc1.setEmail("pavankalyan@gmail.com");
            cc1.setPhone("9876543210");
            customerRepository.save(cc1);

            Customer cc2 = new Customer();
            cc2.setName("Virat Kohli");
            cc2.setPan("H2345678");
            cc2.setEmail("viratkohli@gmail.com");
            cc2.setPhone("9876543211");
            customerRepository.save(cc2);

            Customer cc3 = new Customer();
            cc3.setName("Rohit Sharma");
            cc3.setPan("H3456789");
            cc3.setEmail("rohitsharma@gmail.com");
            cc3.setPhone("9876543212");
            customerRepository.save(cc3);

            Customer cc4 = new Customer();
            cc4.setName("Meghana Y");
            cc4.setPan("H4567890");
            cc4.setEmail("meghnay@gmail.com");
            cc4.setPhone("9876543213");
            customerRepository.save(cc4);

            Customer cc5 = new Customer();
            cc5.setName("Arjun Reddy");
            cc5.setPan("H5678901");
            cc5.setEmail("arjunreddy@gmail.com");
            cc5.setPhone("9876543214");
            customerRepository.save(cc5);

            Customer cc6 = new Customer();
            cc6.setName("Priya Nair");
            cc6.setPan("H6789012");
            cc6.setEmail("priyanair@gmail.com");
            cc6.setPhone("9876543215");
            customerRepository.save(cc6);

            Customer cc7 = new Customer();
            cc7.setName("Vikram rathod");
            cc7.setPan("H7890123");
            cc7.setEmail("vikramrathod@gmail.com");
            cc7.setPhone("9876543216");
            customerRepository.save(cc7);

            Customer cc8 = new Customer();
            cc8.setName("Meera Joshi");
            cc8.setPan("H8901234");
            cc8.setEmail("meerajoshi@gmail.com");
            cc8.setPhone("9876543217");
            customerRepository.save(cc8);

            Customer cc9 = new Customer();
            cc9.setName("Harshad Mehta");
            cc9.setPan("H9012345");
            cc9.setEmail("harshadmehta@gmail.com");
            cc9.setPhone("9876543218");
            customerRepository.save(cc9);

            Customer cc10 = new Customer();
            cc10.setName("Divya Sri");
            cc10.setPan("H0123456");
            cc10.setEmail("divyasri@gmail.com");
            cc10.setPhone("9876543219");
            customerRepository.save(cc10);


            Customer cc11 = new Customer();
            cc11.setName("Siddharth Rao");
            cc11.setPan("H1122334");
            cc11.setEmail("siddharthrao@gmail.com");
            cc11.setPhone("9876543220");
            customerRepository.save(cc11);

            Customer cc12 = new Customer();
            cc12.setName("Neha Samreen");
            cc12.setPan("H2233445");
            cc12.setEmail("nehasamreen@gmail.com");
            cc12.setPhone("9876543221");
            customerRepository.save(cc12);

            Customer cc13 = new Customer();
            cc13.setName("Aditya Roy");
            cc13.setPan("H3344556");
            cc13.setEmail("adityaroy@gmail.com");
            cc13.setPhone("9876543222");
            customerRepository.save(cc13);

            Customer cc14 = new Customer();
            cc14.setName("Riya Sharma");
            cc14.setPan("H4455667");
            cc14.setEmail("riyasharma@gmail.com");
            cc14.setPhone("9876543223");
            customerRepository.save(cc14);

            Customer cc15 = new Customer();
            cc15.setName("Harsh Vardhan");
            cc15.setPan("H5566778");
            cc15.setEmail("harshvardhan@gmail.com");
            cc15.setPhone("9876543224");
            customerRepository.save(cc15);


            System.out.println("Customer PostConstruct running...");


        }
    }


}
