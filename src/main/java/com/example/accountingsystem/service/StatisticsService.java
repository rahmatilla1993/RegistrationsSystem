package com.example.accountingsystem.service;

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

    public ApiResponse getNumberOfEmployeesInDepartment() {
        List<IEmployeeCount> list = employeeStatisticsRepository.numberOfEmployeesInDepartments();
        return new ApiResponse(list, true);
    }

    public ApiResponse getEmployeesByAge(Integer age, String condition) {
        List<Employee> employees = switch (condition) {
            case ">" -> employeeStatisticsRepository.findAllByAgeGreaterThan(age);
            case "<" -> employeeStatisticsRepository.findAllByAgeLessThan(age);
            default -> employeeStatisticsRepository.findAllByAge(age);
        };
        return new ApiResponse(employees, true);
    }

    public ApiResponse getEmployeesByPage(Integer page, Integer size) {
        List<Employee> employeeList = employeeStatisticsRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
        return new ApiResponse(employeeList, true);
    }

    public ApiResponse getSumOfSalaryAllEmployeesByDepartments() {
        List<IEmployeeSalary> salaryList = employeeStatisticsRepository.getSumOfSalaryAllEmployeesByDepartments();
        return new ApiResponse(salaryList, true);
    }

    public ApiResponse getTotalSalary() {
        return new ApiResponse(
                employeeStatisticsRepository.totalSumOfSalary(),
                true
        );
    }

    //Clients
    public ApiResponse getTopEmployeesWithMostCustomerRegistrations(int size) {
        List<IClient> list = clientStatisticsRepository.getEmployeesWithMostCustomerRegistrations(size);
        return new ApiResponse(list, true);
    }

    public ApiResponse getEmployeeWithMostClientRegistered() {
        List<IClient> clientList = clientStatisticsRepository
                .getEmployeesWithMostCustomerRegistrations(1);
        return new ApiResponse(clientList.get(0), true);
    }

    public ApiResponse getCountRegistrationClientsInTheLastMonth() {
        Integer count = clientStatisticsRepository
                .getCountRegistrationClientsInTheLastMonth();
        return new ApiResponse(count, true);
    }

    public ApiResponse getDayOfTheLastMonthMostClientsRegistered() {
        LocalDate date = clientStatisticsRepository
                .getDayOfTheLastMonthMostClientsRegistered();
        return new ApiResponse(date, true);
    }

    public ApiResponse getDailyRegisteredCount() {
        List<IDailyRegister> registerList = clientStatisticsRepository.getDailyRegisteredCount();
        return new ApiResponse(registerList, true);
    }

    //advertisements
    public ApiResponse getPopularTypeOfAdvertisingCosts() {
        List<IAdvertisement> list = advertisementStatisticsRepository
                .getPopularTypeOfAdvertisingCosts();
        return new ApiResponse(list.get(0), true);
    }

    public ApiResponse getEmployeeByMostAdvertisingCosts() {
        List<IEmployee> employeeList = advertisementStatisticsRepository
                .getEmployeeByMostAdvertisingCosts();
        return new ApiResponse(employeeList, true);
    }

    public ApiResponse getCountOfAdvertisementAddedInLastMonth() {
        Integer counts = advertisementStatisticsRepository
                .getCountOfAdvertisementAddedInLastMonth();
        return new ApiResponse(counts, true);
    }

    public ApiResponse getCountOfExpiredAdvertisementsInLastMonth() {
        Integer count = advertisementStatisticsRepository
                .getCountOfExpiredAdvertisementsInLastMonth();
        return new ApiResponse(count, true);
    }

    public ApiResponse getTypesOfAdvertisingCosts() {
        List<IAdvertisement> list = advertisementStatisticsRepository
                .getTypesOfAdvertisingCosts();
        return new ApiResponse(list, true);
    }
}
