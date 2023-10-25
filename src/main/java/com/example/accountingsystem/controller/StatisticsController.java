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
}
