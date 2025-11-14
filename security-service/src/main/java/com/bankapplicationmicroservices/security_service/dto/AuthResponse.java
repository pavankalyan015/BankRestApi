package com.bankapplicationmicroservices.security_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private long expiresIn;
}