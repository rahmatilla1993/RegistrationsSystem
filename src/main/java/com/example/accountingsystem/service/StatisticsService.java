package com.example.accountingsystem.service;

import com.example.accountingsystem.annotation.Logger;
import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.projection.*;
import com.example.accountingsystem.repository.AdvertisementStatisticsRepository;
import com.example.accountingsystem.repository.ClientStatisticsRepository;
import com.example.accountingsystem.repository.EmployeeStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsService {

    private final EmployeeStatisticsRepository employeeStatisticsRepository;
    private final ClientStatisticsRepository clientStatisticsRepository;
    private final AdvertisementStatisticsRepository advertisementStatisticsRepository;

    @Autowired
    public StatisticsService(EmployeeStatisticsRepository employeeStatisticsRepository,
                             ClientStatisticsRepository clientStatisticsRepository,
                             AdvertisementStatisticsRepository advertisementStatisticsRepository
    ) {
        this.employeeStatisticsRepository = employeeStatisticsRepository;
        this.clientStatisticsRepository = clientStatisticsRepository;
        this.advertisementStatisticsRepository = advertisementStatisticsRepository;
    }

    //employees

    @Logger(
            message = "fetched number of employees in department from",
            tableName = "employees"
    )
    public ApiResponse getNumberOfEmployeesInDepartment() {
        List<IEmployeeCount> list = employeeStatisticsRepository.numberOfEmployeesInDepartments();
        return new ApiResponse(list, true);
    }

    @Logger(
            message = "fetched all employees by age from",
            tableName = "employees"
    )
    public ApiResponse getEmployeesByAge(Integer age, String condition) {
        List<Employee> employees = switch (condition) {
            case ">" -> employeeStatisticsRepository.findAllByAgeGreaterThan(age);
            case "<" -> employeeStatisticsRepository.findAllByAgeLessThan(age);
            default -> employeeStatisticsRepository.findAllByAge(age);
        };
        return new ApiResponse(employees, true);
    }

    @Logger(
            message = "fetched all employees by pagination from",
            tableName = "employees"
    )
    public ApiResponse getEmployeesByPage(Integer page, Integer size) {
        List<Employee> employeeList = employeeStatisticsRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
        return new ApiResponse(employeeList, true);
    }

    @Logger(
            message = "fetched sum of salary all employees by departments from",
            tableName = "employees"
    )
    public ApiResponse getSumOfSalaryAllEmployeesByDepartments() {
        List<IEmployeeSalary> salaryList = employeeStatisticsRepository.getSumOfSalaryAllEmployeesByDepartments();
        return new ApiResponse(salaryList, true);
    }

    @Logger(
            message = "get total salary from",
            tableName = "employees"
    )
    public ApiResponse getTotalSalary() {
        return new ApiResponse(
                employeeStatisticsRepository.totalSumOfSalary(),
                true
        );
    }

    //Clients
    @Logger(
            message = "fetched top employees with most customer registrations data from",
            tableName = "employees"
    )
    public ApiResponse getTopEmployeesWithMostCustomerRegistrations(int size) {
        List<IClient> list = clientStatisticsRepository.getEmployeesWithMostCustomerRegistrations(size);
        return new ApiResponse(list, true);
    }

    @Logger(
            message = "get employee data with most client registered from",
            tableName = "employees"
    )
    public ApiResponse getEmployeeWithMostClientRegistered() {
        List<IClient> clientList = clientStatisticsRepository
                .getEmployeesWithMostCustomerRegistrations(1);
        return new ApiResponse(clientList.get(0), true);
    }

    @Logger(
            message = "get count registration clients in last month data from",
            tableName = "employees"
    )
    public ApiResponse getCountRegistrationClientsInTheLastMonth() {
        Integer count = clientStatisticsRepository
                .getCountRegistrationClientsInTheLastMonth();
        return new ApiResponse(count, true);
    }

    @Logger(
            message = "get date the last month most clients registered data from",
            tableName = "employees"
    )
    public ApiResponse getDayOfTheLastMonthMostClientsRegistered() {
        LocalDate date = clientStatisticsRepository
                .getDayOfTheLastMonthMostClientsRegistered();
        return new ApiResponse(date, true);
    }

    @Logger(
            message = "get daily registered clients data from",
            tableName = "employees"
    )
    public ApiResponse getDailyRegisteredCount() {
        List<IDailyRegister> registerList = clientStatisticsRepository.getDailyRegisteredCount();
        return new ApiResponse(registerList, true);
    }

    //advertisements
    @Logger(
            message = "get popular type of advertising costs data from",
            tableName = "employees"
    )
    public ApiResponse getPopularTypeOfAdvertisingCosts() {
        List<IAdvertisement> list = advertisementStatisticsRepository
                .getPopularTypeOfAdvertisingCosts();
        return new ApiResponse(list.get(0), true);
    }

    @Logger(
            message = "fetched employees by most advertising costs data from",
            tableName = "employees"
    )
    public ApiResponse getEmployeeByMostAdvertisingCosts() {
        List<IEmployee> employeeList = advertisementStatisticsRepository
                .getEmployeeByMostAdvertisingCosts();
        return new ApiResponse(employeeList, true);
    }

    @Logger(
            message = "get count of expenses added in last month data from",
            tableName = "employees"
    )
    public ApiResponse getCountOfAdvertisementAddedInLastMonth() {
        Integer counts = advertisementStatisticsRepository
                .getCountOfAdvertisementAddedInLastMonth();
        return new ApiResponse(counts, true);
    }

    @Logger(
            message = "get count of expired expenses in last month data from",
            tableName = "employees"
    )
    public ApiResponse getCountOfExpiredAdvertisementsInLastMonth() {
        Integer count = advertisementStatisticsRepository
                .getCountOfExpiredAdvertisementsInLastMonth();
        return new ApiResponse(count, true);
    }

    @Logger(
            message = "get types of expenses costs data from",
            tableName = "employees"
    )
    public ApiResponse getTypesOfAdvertisingCosts() {
        List<IAdvertisement> list = advertisementStatisticsRepository
                .getTypesOfAdvertisingCosts();
        return new ApiResponse(list, true);
    }
}
