package com.example.accountingsystem.projection;

import java.time.LocalDate;

public interface IDailyRegister {
    LocalDate getDate();
    Long getCount();
}
