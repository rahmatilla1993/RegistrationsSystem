package com.example.accountingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PassportDto {

    @Min(value = 1, message = "Do not enter a negative value")
    private int number;

    @NotEmpty(message = "Passport series should not be empty")
    private String series;

    @NotEmpty(message = "Nationality should not be empty")
    private String nationality;

    @Min(value = 1, message = "Do not enter a negative value")
    private int identityNumber;
}
