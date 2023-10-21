package com.example.accountingsystem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientDto {

    @NotEmpty(message = "Firstname should not be empty")
    @Size(min = 3, message = "Name must not be less than 5 characters long")
    private String firstName;

    @NotEmpty(message = "Lastname should not be empty")
    @Size(min = 4, message = "Lastname must not be less than 5 characters long")
    private String lastName;

    @NotEmpty(message = "Address should not be empty")
    private String address;

    @Min(value = 1, message = "Age will not be less than 1")
    @Max(value = 100, message = "Age will not be more than 100")
    private int age;

    @NotEmpty(message = "Employee email should not be empty")
    private String empEmail;

    @Valid
    private PassportDto passport;
}
