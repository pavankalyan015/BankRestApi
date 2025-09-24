package com.bankapplication.BankRestApi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponce {
    private LocalDateTime timeStamp;
    private int status;
    private String error;         // e.g., "Not Found"
    private String message;       // user-friendly message
    private String path;          // request path

}
