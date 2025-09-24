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
    private String error;
    private String message;
    private String path;

}
