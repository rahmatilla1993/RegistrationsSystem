package com.example.accountingsystem.controller;

import com.example.accountingsystem.dto.ClientDto;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotCreateException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.service.ClientService;
import com.example.accountingsystem.validations.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;
    private final ResponseErrorValidation errorValidation;

    @Autowired
    public ClientController(ClientService clientService,
                            ResponseErrorValidation errorValidation) {
        this.clientService = clientService;
        this.errorValidation = errorValidation;
    }

    @GetMapping
    public HttpEntity<?> getAll(Principal principal) {
        return ResponseEntity.ok(
                clientService.findAllByCreatedBy(principal)
        );
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable int id, Principal principal) {
        return ResponseEntity.ok(
                clientService.findByIdAndCreatedBy(principal.getName(), id)
        );
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody @Valid ClientDto clientDto,
                              BindingResult bindingResult,
                              Principal principal
    ) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                clientService.save(clientDto, principal)
        );
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable int id,
                              @RequestBody @Valid ClientDto clientDto,
                              BindingResult bindingResult,
                              Principal principal
    ) {
        var errors = errorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(
                clientService.edit(clientDto, id, principal)
        );
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable int id, Principal principal) {
        return ResponseEntity.ok(
                clientService.delete(id, principal)
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
