package com.bankapplication.BankRestApi.exception;

public class NotACustomerException extends RuntimeException {
    public NotACustomerException(String message) {
        super(message);
    }
}
