package com.example.accountingsystem.controller;

import com.example.accountingsystem.dto.AdvertisementDto;
import com.example.accountingsystem.entity.Advertisement;
import com.example.accountingsystem.exception.ObjectNotCreateException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.service.AdvertisementService;
import com.example.accountingsystem.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/advertisement")
@PreAuthorize(
        value = "hasAnyRole('ROLE_MANAGER', 'ROLE_DIRECTOR', 'ROLE_EMPLOYEE')"
)
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final ResponseErrorValidation errorValidation;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService,
                                   ResponseErrorValidation errorValidation) {
        this.advertisementService = advertisementService;
        this.errorValidation = errorValidation;
    }

    @GetMapping
    public HttpEntity<?> getAll(Pageable pageable) {
        return ResponseEntity.ok(
                advertisementService.getAll(pageable)
        );
    }

    @GetMapping("/byPage")
    public HttpEntity<?> getAllByPage(@RequestParam("page") int page,
                                      @RequestParam("limit") int limit) {
        return ResponseEntity.ok(
                advertisementService.getAllByPage(page, limit)
        );
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable("id") int id) {
        Advertisement advertisement = advertisementService.findById(id);
        return ResponseEntity.ok(
                new ApiResponse(advertisement, true)
        );
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody @Valid AdvertisementDto advertisementDto,
                              BindingResult bindingResult) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                advertisementService.save(advertisementDto)
        );
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable("id") int id,
                              @RequestBody @Valid AdvertisementDto advertisementDto,
                              BindingResult bindingResult) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                advertisementService.edit(advertisementDto, id)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_MANAGER', 'ROLE_DIRECTOR')")
    public HttpEntity<?> delete(@PathVariable("id") int id) {
        return ResponseEntity.ok(
                advertisementService.delete(id)
        );
    }

    @ExceptionHandler
    public HttpEntity<?> exceptionHandler(ObjectNotCreateException ex) {
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
