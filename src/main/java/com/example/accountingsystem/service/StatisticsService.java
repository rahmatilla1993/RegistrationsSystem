package com.example.accountingsystem.service;

import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.projection.IClient;
import com.example.accountingsystem.projection.IDailyRegister;
import com.example.accountingsystem.projection.IEmployeeCount;
import com.example.accountingsystem.projection.IEmployeeSalary;
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

    @Autowired
    public StatisticsService(EmployeeStatisticsRepository employeeStatisticsRepository,
                             ClientStatisticsRepository clientStatisticsRepository) {
        this.employeeStatisticsRepository = employeeStatisticsRepository;
        this.clientStatisticsRepository = clientStatisticsRepository;
    }

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
}
