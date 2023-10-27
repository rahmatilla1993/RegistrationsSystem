package com.example.accountingsystem.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccessDeniedResponse {
    private final String message = "Forbidden!! You are not allowed to enter this path";
    private final int statusCode = 403;
    private final String time = LocalDateTime.now().toString();
    private String path;
}
