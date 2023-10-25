package com.example.accountingsystem.service;

import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.projection.IEmployeeCount;
import com.example.accountingsystem.projection.IEmployeeSalary;
import com.example.accountingsystem.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    @Autowired
    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public ApiResponse getNumberOfEmployeesInDepartment() {
        List<IEmployeeCount> list = statisticsRepository.numberOfEmployeesInDepartments();
        return new ApiResponse(list, true);
    }

    public ApiResponse getEmployeesByAge(Integer age, String condition) {
        List<Employee> employees = switch (condition) {
            case ">" -> statisticsRepository.findAllByAgeGreaterThan(age);
            case "<" -> statisticsRepository.findAllByAgeLessThan(age);
            default -> statisticsRepository.findAllByAge(age);
        };
        return new ApiResponse(employees, true);
    }

    public ApiResponse getEmployeesByPage(Integer page, Integer size) {
        List<Employee> employeeList = statisticsRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
        return new ApiResponse(employeeList, true);
    }

    public ApiResponse getSumOfSalaryAllEmployeesByDepartments() {
        List<IEmployeeSalary> salaryList = statisticsRepository.getSumOfSalaryAllEmployeesByDepartments();
        return new ApiResponse(salaryList, true);
    }

    public ApiResponse getTotalSalary() {
        return new ApiResponse(
                statisticsRepository.totalSumOfSalary(),
                true
        );
    }
}
