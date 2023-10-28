package com.example.accountingsystem.controller;

import com.example.accountingsystem.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/statistics")
@PreAuthorize(
        value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')"
)
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public HttpEntity<?> getNumberOfEmployeesInDepartment(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getNumberOfEmployeesInDepartment(principal)
        );
    }

    @GetMapping("/byAge")
    public HttpEntity<?> getAllByAge(@RequestParam("age") int age,
                                     @RequestParam("condition") String condition,
                                     Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getEmployeesByAge(age, condition, principal)
        );
    }

    @GetMapping("/byPage")
    public HttpEntity<?> getAllByPage(@RequestParam("page") int page,
                                      @RequestParam("size") int size,
                                      Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getEmployeesByPage(page, size, principal)
        );
    }

    @GetMapping("/summa")
    public HttpEntity<?> getSumOfSalaryByDepartments(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getSumOfSalaryAllEmployeesByDepartments(principal)
        );
    }

    @GetMapping("/totalSum")
    public HttpEntity<?> getTotalSum(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getTotalSalary(principal)
        );
    }

    //clients

    //task-1
    @GetMapping("/dailyRegister")
    public HttpEntity<?> getDailyRegisteredCount(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getDailyRegisteredCount(principal)
        );
    }

    //task-2
    @GetMapping("/mostRegisteredClients")
    public HttpEntity<?> getEmployeeWithMostClientRegistered(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getEmployeeWithMostClientRegistered(principal)
        );
    }

    //task-3
    @GetMapping("/topEmployees")
    public HttpEntity<?> getTopEmployeesWithMostClientsRegistered(@RequestParam("size") int size,
                                                                  Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getTopEmployeesWithMostCustomerRegistrations(size, principal)
        );
    }

    //task-4
    @GetMapping("/registeredLastMonth")
    public HttpEntity<?> getCountRegistrationClientsInTheLastMonth(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getCountRegistrationClientsInTheLastMonth(principal)
        );
    }

    //task-5
    @GetMapping("/dayOfMostRegistered")
    public HttpEntity<?> getDayOfTheLastMonthMostClientsRegistered(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getDayOfTheLastMonthMostClientsRegistered(principal)
        );
    }

    //advertisements

    //task-1
    @GetMapping("/popularType")
    public HttpEntity<?> getPopularTypeOfAdvertisingCosts(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getPopularTypeOfAdvertisingCosts(principal)
        );
    }

    //task-2
    @GetMapping("/employeeByMostAdvertisingCost")
    public HttpEntity<?> getEmployeeByMostAdvertisingCosts(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getEmployeeByMostAdvertisingCosts(principal)
        );
    }

    //task-3
    @GetMapping("/countOfAdvertisementAdded")
    public HttpEntity<?> getCountOfAdvertisementAddedInLastMonth(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getCountOfAdvertisementAddedInLastMonth(principal)
        );
    }

    //task-4
    @GetMapping("/countOfExpiredAdvertisement")
    public HttpEntity<?> getCountOfExpiredAdvertisementsInLastMonth(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getCountOfExpiredAdvertisementsInLastMonth(principal)
        );
    }

    //task-5
    @GetMapping("/getAdvertisementTypes")
    public HttpEntity<?> getTypesOfAdvertisingCosts(Principal principal) {
        return ResponseEntity.ok(
                statisticsService.getTypesOfAdvertisingCosts(principal)
        );
    }
}
