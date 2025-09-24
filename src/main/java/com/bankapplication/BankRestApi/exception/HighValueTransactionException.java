package com.bankapplication.BankRestApi.exception;

public class HighValueTransactionException extends RuntimeException {
    public HighValueTransactionException(String message) {
        super(message);
    }
}
