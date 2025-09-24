package com.bankapplication.BankRestApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
/*@ComponentScan(basePackages = {
        "com.bankapplication.BankRestApi.service",
        "com.bankapplication.BankRestApi.repository",
        "com.bankapplication.BankRestApi.entity"
})*/

public class BankRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankRestApiApplication.class, args);

	}

}
