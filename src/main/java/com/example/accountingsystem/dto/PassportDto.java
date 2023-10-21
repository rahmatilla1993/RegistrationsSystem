package com.example.accountingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PassportDto {

    @Min(value = 1, message = "Do not enter a negative value")
    @Pattern(regexp = "^\\d{7}$", message = "Must be 7 digits long")
    private String number;

    @NotEmpty(message = "Passport series should not be empty")
    @Pattern(regexp = "[A-Z]{2}", message = "Passport series consists of 2 letters")
    private String series;

    @NotEmpty(message = "Nationality should not be empty")
    private String nationality;

    @NotEmpty(message = "Identity number series should not be empty")
    @Pattern(regexp = "^\\d{14}$", message = "Must be 14 digits long")
    private String identityNumber;
}
