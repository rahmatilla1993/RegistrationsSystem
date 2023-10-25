package com.example.accountingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdvertisementDto {

    @NotEmpty(message = "Type should not be empty")
    private String type;

    @Min(value = 0, message = "The cost value will not be negative")
    private double cost;

    @NotEmpty(message = "PeriodTime should not be empty")
    @Pattern(
            regexp = "(^0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4}$)",
            message = "Date must match the 'dd-MM-yyyy' template"
    )
    private String period;
}
