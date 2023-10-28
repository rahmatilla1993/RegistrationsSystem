package com.example.accountingsystem.service;

import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.projection.*;
import com.example.accountingsystem.repository.AdvertisementStatisticsRepository;
import com.example.accountingsystem.repository.ClientStatisticsRepository;
import com.example.accountingsystem.repository.EmployeeStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class StatisticsService {

    private final EmployeeStatisticsRepository employeeStatisticsRepository;
    private final ClientStatisticsRepository clientStatisticsRepository;
    private final AdvertisementStatisticsRepository advertisementStatisticsRepository;

    private static final String LOG_MESSAGE = "Employee with email {} send a request " +
            "to the {} endpoint via the {} method and " +
            "{} the table {}";

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

    public ApiResponse getNumberOfEmployeesInDepartment(Principal principal) {
        List<IEmployeeCount> list = employeeStatisticsRepository.numberOfEmployeesInDepartments();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics", "GET",
                "fetched number of employees in department from", "employees");
        return new ApiResponse(list, true);
    }

    public ApiResponse getEmployeesByAge(Integer age, String condition, Principal principal) {
        List<Employee> employees = switch (condition) {
            case ">" -> employeeStatisticsRepository.findAllByAgeGreaterThan(age);
            case "<" -> employeeStatisticsRepository.findAllByAgeLessThan(age);
            default -> employeeStatisticsRepository.findAllByAge(age);
        };
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/byAge", "GET",
                "fetched all employees by age from", "employees");
        return new ApiResponse(employees, true);
    }

    public ApiResponse getEmployeesByPage(Integer page, Integer size, Principal principal) {
        List<Employee> employeeList = employeeStatisticsRepository
                .findAll(PageRequest.of(page, size))
                .getContent();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/byPage", "GET",
                "fetched all employees by pagination from", "employees");
        return new ApiResponse(employeeList, true);
    }

    public ApiResponse getSumOfSalaryAllEmployeesByDepartments(Principal principal) {
        List<IEmployeeSalary> salaryList = employeeStatisticsRepository.getSumOfSalaryAllEmployeesByDepartments();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/summa", "GET",
                "fetched sum of salary all employees by departments from", "employees");
        return new ApiResponse(salaryList, true);
    }

    public ApiResponse getTotalSalary(Principal principal) {
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/totalSum", "GET",
                "get total salary from", "employees");
        return new ApiResponse(
                employeeStatisticsRepository.totalSumOfSalary(),
                true
        );
    }

    //Clients
    public ApiResponse getTopEmployeesWithMostCustomerRegistrations(int size, Principal principal) {
        List<IClient> list = clientStatisticsRepository.getEmployeesWithMostCustomerRegistrations(size);
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/topEmployees", "GET",
                "fetched top employees with most customer registrations data from", "employees");
        return new ApiResponse(list, true);
    }

    public ApiResponse getEmployeeWithMostClientRegistered(Principal principal) {
        List<IClient> clientList = clientStatisticsRepository
                .getEmployeesWithMostCustomerRegistrations(1);
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/mostRegisteredClients", "GET",
                "get employee data with most client registered from", "client");
        return new ApiResponse(clientList.get(0), true);
    }

    public ApiResponse getCountRegistrationClientsInTheLastMonth(Principal principal) {
        Integer count = clientStatisticsRepository
                .getCountRegistrationClientsInTheLastMonth();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/registeredLastMonth", "GET",
                "get count registration clients in last month data from", "client");
        return new ApiResponse(count, true);
    }

    public ApiResponse getDayOfTheLastMonthMostClientsRegistered(Principal principal) {
        LocalDate date = clientStatisticsRepository
                .getDayOfTheLastMonthMostClientsRegistered();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/dayOfMostRegistered", "GET",
                "get date the last month most clients registered data from", "client");
        return new ApiResponse(date, true);
    }

    public ApiResponse getDailyRegisteredCount(Principal principal) {
        List<IDailyRegister> registerList = clientStatisticsRepository.getDailyRegisteredCount();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/dailyRegister", "GET",
                "get daily registered clients data from", "client");
        return new ApiResponse(registerList, true);
    }

    //advertisements
    public ApiResponse getPopularTypeOfAdvertisingCosts(Principal principal) {
        List<IAdvertisement> list = advertisementStatisticsRepository
                .getPopularTypeOfAdvertisingCosts();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/popularType", "GET",
                "get popular type of advertising costs data from", "advertisement");
        return new ApiResponse(list.get(0), true);
    }

    public ApiResponse getEmployeeByMostAdvertisingCosts(Principal principal) {
        List<IEmployee> employeeList = advertisementStatisticsRepository
                .getEmployeeByMostAdvertisingCosts();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/employeeByMostAdvertisingCost", "GET",
                "fetched employees by most advertising costs data from", "advertisement");
        return new ApiResponse(employeeList, true);
    }

    public ApiResponse getCountOfAdvertisementAddedInLastMonth(Principal principal) {
        Integer counts = advertisementStatisticsRepository
                .getCountOfAdvertisementAddedInLastMonth();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/countOfAdvertisementAdded", "GET",
                "get count of expenses added in last month data from", "advertisement");
        return new ApiResponse(counts, true);
    }

    public ApiResponse getCountOfExpiredAdvertisementsInLastMonth(Principal principal) {
        Integer count = advertisementStatisticsRepository
                .getCountOfExpiredAdvertisementsInLastMonth();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/countOfExpiredAdvertisement", "GET",
                "get count of expired expenses in last month data from", "advertisement");
        return new ApiResponse(count, true);
    }

    public ApiResponse getTypesOfAdvertisingCosts(Principal principal) {
        List<IAdvertisement> list = advertisementStatisticsRepository
                .getTypesOfAdvertisingCosts();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/statistics/getAdvertisementTypes", "GET",
                "get types of expenses costs data from", "advertisement");
        return new ApiResponse(list, true);
    }

    private String getEmployeeEmail(Principal principal) {
        return principal.getName();
    }
}
