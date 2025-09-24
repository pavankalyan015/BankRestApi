package com.bankapplication.BankRestApi.exception;

public class MinimumBalanceExpection extends RuntimeException {
    public MinimumBalanceExpection(String message) {
        super(message);
    }
}
