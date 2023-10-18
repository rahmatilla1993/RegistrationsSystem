package com.example.accountingsystem.controller;

import com.example.accountingsystem.dto.PersonDto;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.service.PeopleService;
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
@RequestMapping("/api/employee")
public class PeopleController {

    private final PeopleService peopleService;
    private final ResponseErrorValidation errorValidation;

    @Autowired
    public PeopleController(PeopleService peopleService,
                            ResponseErrorValidation errorValidation) {
        this.peopleService = peopleService;
        this.errorValidation = errorValidation;
    }

    @GetMapping
    public HttpEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(
                peopleService.getAll(pageable)
        );
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable int id) {
        return ResponseEntity.ok(
                peopleService.findById(id)
        );
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody @Valid PersonDto personDto,
                              BindingResult bindingResult) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                peopleService.save(personDto)
        );
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable int id,
                              @RequestBody @Valid PersonDto personDto,
                              BindingResult bindingResult) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                peopleService.edit(personDto, id)
        );
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable int id) {
        return ResponseEntity.ok(
                peopleService.delete(id)
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
