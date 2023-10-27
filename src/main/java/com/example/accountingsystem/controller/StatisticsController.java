package com.example.accountingsystem.controller;

import com.example.accountingsystem.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public HttpEntity<?> getNumberOfEmployeesInDepartment() {
        return ResponseEntity.ok(
                statisticsService.getNumberOfEmployeesInDepartment()
        );
    }

    @GetMapping("/byAge")
    public HttpEntity<?> getAllByAge(@RequestParam("age") int age,
                                     @RequestParam("condition") String condition) {
        return ResponseEntity.ok(
                statisticsService.getEmployeesByAge(age, condition)
        );
    }

    @GetMapping("/byPage")
    public HttpEntity<?> getAllByPage(@RequestParam("page") int page,
                                      @RequestParam("size") int size) {
        return ResponseEntity.ok(
                statisticsService.getEmployeesByPage(page, size)
        );
    }

    @GetMapping("/summa")
    public HttpEntity<?> getSumOfSalaryByDepartments() {
        return ResponseEntity.ok(
                statisticsService.getSumOfSalaryAllEmployeesByDepartments()
        );
    }

    @GetMapping("/totalSum")
    public HttpEntity<?> getTotalSum() {
        return ResponseEntity.ok(
                statisticsService.getTotalSalary()
        );
    }

    //clients

    //task-1
    @GetMapping("/dailyRegister")
    public HttpEntity<?> getDailyRegisteredCount() {
        return ResponseEntity.ok(
                statisticsService.getDailyRegisteredCount()
        );
    }

    //task-2
    @GetMapping("/mostRegisteredClients")
    public HttpEntity<?> getEmployeeWithMostClientRegistered() {
        return ResponseEntity.ok(
                statisticsService.getEmployeeWithMostClientRegistered()
        );
    }

    //task-3
    @GetMapping("/topEmployees")
    public HttpEntity<?> getTopEmployeesWithMostClientsRegistered(@RequestParam("size") int size) {
        return ResponseEntity.ok(
                statisticsService.getTopEmployeesWithMostCustomerRegistrations(size)
        );
    }

    //task-4
    @GetMapping("/registeredLastMonth")
    public HttpEntity<?> getCountRegistrationClientsInTheLastMonth() {
        return ResponseEntity.ok(
                statisticsService.getCountRegistrationClientsInTheLastMonth()
        );
    }

    //task-5
    @GetMapping("/dayOfMostRegistered")
    public HttpEntity<?> getDayOfTheLastMonthMostClientsRegistered() {
        return ResponseEntity.ok(
                statisticsService.getDayOfTheLastMonthMostClientsRegistered()
        );
    }

    //advertisements

    //task-1
    @GetMapping("/popularType")
    public HttpEntity<?> getPopularTypeOfAdvertisingCosts() {
        return ResponseEntity.ok(
                statisticsService.getPopularTypeOfAdvertisingCosts()
        );
    }

    //task-2
    @GetMapping("/employeeByMostAdvertisingCost")
    public HttpEntity<?> getEmployeeByMostAdvertisingCosts() {
        return ResponseEntity.ok(
                statisticsService.getEmployeeByMostAdvertisingCosts()
        );
    }

    //task-3
    @GetMapping("/countOfAdvertisementAdded")
    public HttpEntity<?> getCountOfAdvertisementAddedInLastMonth() {
        return ResponseEntity.ok(
                statisticsService.getCountOfAdvertisementAddedInLastMonth()
        );
    }

    //task-4
    @GetMapping("/countOfExpiredAdvertisement")
    public HttpEntity<?> getCountOfExpiredAdvertisementsInLastMonth() {
        return ResponseEntity.ok(
                statisticsService.getCountOfExpiredAdvertisementsInLastMonth()
        );
    }

    //task-5
    @GetMapping("/getAdvertisementTypes")
    public HttpEntity<?> getTypesOfAdvertisingCosts() {
        return ResponseEntity.ok(
                statisticsService.getTypesOfAdvertisingCosts()
        );
    }
}
