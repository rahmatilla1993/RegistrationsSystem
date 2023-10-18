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
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public HttpEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(
                departmentService.getAll(pageable)
        );
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(
                departmentService.findById(id)
        );
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody @Valid Department department,
                              BindingResult bindingResult) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                departmentService.save(department)
        );
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@RequestBody @Valid Department department,
                              BindingResult bindingResult,
                              @PathVariable int id) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                departmentService.edit(department, id)
        );
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable int id) {
        return ResponseEntity.ok(
                departmentService.delete(id)
        );
    }

    @ExceptionHandler
    public HttpEntity<?> exceptionHandler(ObjectExistsException ex) {
        return getApiResponse(ex.getMessage());
    }

    @ExceptionHandler
    public HttpEntity<?> exceptionHandler(ObjectNotFoundException ex) {
        return getApiResponse(ex.getMessage());
    }

    public HttpEntity<?> getApiResponse(String exceptionMessage) {
        return ResponseEntity
                .badRequest()
                .body(new ApiResponse(exceptionMessage, false));
    }
}
