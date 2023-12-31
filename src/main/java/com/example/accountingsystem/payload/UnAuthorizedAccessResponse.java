package com.example.accountingsystem.payload;

import lombok.Data;

@Data
public class UnAuthorizedAccessResponse {
    private final String message = "Email or password invalid";
    private final int statusCode = 401;
}
