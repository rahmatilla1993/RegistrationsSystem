package com.example.accountingsystem.controller;

import com.example.accountingsystem.dto.EmployeeDto;
import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.service.EmployeeService;
import com.example.accountingsystem.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ResponseErrorValidation errorValidation;

    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              ResponseErrorValidation errorValidation) {
        this.employeeService = employeeService;
        this.errorValidation = errorValidation;
    }

    @GetMapping
    public HttpEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(
                employeeService.getAll(pageable)
        );
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable int id) {
        Employee employee = employeeService.findById(id);
        return ResponseEntity.ok(
                new ApiResponse(employee, true)
        );
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody @Valid EmployeeDto employeeDto,
                              BindingResult bindingResult) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                employeeService.save(employeeDto)
        );
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable int id,
                              @RequestBody @Valid EmployeeDto employeeDto,
                              BindingResult bindingResult) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                employeeService.edit(employeeDto, id)
        );
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable int id) {
        return ResponseEntity.ok(
                employeeService.delete(id)
        );
    }

    @ExceptionHandler
    public HttpEntity<?> exceptionHandler(ObjectExistsException ex) {
        var apiResponse = new ApiResponse(ex.getMessage(), false);
        return ResponseEntity
                .badRequest()
                .body(apiResponse);
    }

    @ExceptionHandler
    public HttpEntity<?> exceptionHandler(ObjectNotFoundException ex) {
        var apiResponse = new ApiResponse(ex.getMessage(), false);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiResponse);
    }
}
