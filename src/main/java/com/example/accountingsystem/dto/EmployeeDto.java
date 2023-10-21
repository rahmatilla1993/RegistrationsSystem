package com.example.accountingsystem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeDto {

    @NotEmpty(message = "Firstname should not be empty")
    @Size(min = 3, message = "Name must not be less than 5 characters long")
    private String firstName;

    @NotEmpty(message = "Lastname should not be empty")
    @Size(min = 4, message = "Lastname must not be less than 5 characters long")
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "The email did not match")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{4,}$",
            message = "Length is at least 4 characters, consists of letters and numbers"
    )
    private String password;

    @Min(value = 1, message = "Age will not be less than 1")
    @Max(value = 100, message = "Age will not be more than 100")
    private int age;

    @Min(value = 0, message = "Salary will not be negative")
    private int salary;

    @NotEmpty(message = "Address should not be empty")
    private String address;

    @Valid
    private PassportDto passport;

    @Min(value = 1, message = "No such id department")
    private int departmentId;
}
