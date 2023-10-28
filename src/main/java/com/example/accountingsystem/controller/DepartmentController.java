package com.example.accountingsystem.controller;

import com.example.accountingsystem.entity.Department;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.service.DepartmentService;
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

import java.security.Principal;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final ResponseErrorValidation errorValidation;

    @Autowired
    public DepartmentController(DepartmentService departmentService,
                                ResponseErrorValidation errorValidation
    ) {
        this.departmentService = departmentService;
        this.errorValidation = errorValidation;
    }

    @GetMapping
    public HttpEntity<?> getAll(Pageable pageable, Principal principal) {
        return ResponseEntity.ok(
                departmentService.getAll(pageable, principal)
        );
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable int id, Principal principal) {
        Department department = departmentService.findById(id, principal);
        return ResponseEntity.ok(
                new ApiResponse(department, true)
        );
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody @Valid Department department,
                              BindingResult bindingResult, Principal principal
    ) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                departmentService.save(department, principal)
        );
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@RequestBody @Valid Department department,
                              BindingResult bindingResult,
                              @PathVariable int id, Principal principal
    ) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                departmentService.edit(department, id, principal)
        );
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable int id, Principal principal) {
        return ResponseEntity.ok(
                departmentService.delete(id, principal)
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
