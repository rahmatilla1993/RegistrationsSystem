package com.example.accountingsystem.repository;

import com.example.accountingsystem.entity.Client;
import com.example.accountingsystem.projection.IClient;
import com.example.accountingsystem.projection.IDailyRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientStatisticsRepository extends JpaRepository<Client, Integer> {

    @Query(
            nativeQuery = true,
            value = "select * from getemployeeswithmostcustomerregistrations(:size)"
    )
    List<IClient> getEmployeesWithMostCustomerRegistrations(Integer size);

    @Query(
            nativeQuery = true,
            value = "select count(*) " +
                    "from client " +
                    "where date(created_at) >= current_date - interval '1 month';"
    )
    Integer getCountRegistrationClientsInTheLastMonth();

    @Query(
            nativeQuery = true,
            value = "select getdayofthelastmonthmostclientsregistered()"
    )
    LocalDate getDayOfTheLastMonthMostClientsRegistered();

    @Query(
            nativeQuery = true,
            value = "select date(created_at), count(*) " +
                    "from client " +
                    "group by date(created_at);"
    )
    List<IDailyRegister> getDailyRegisteredCount();
}
