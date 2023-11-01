package com.example.accountingsystem.service;

import com.example.accountingsystem.annotation.Logger;
import com.example.accountingsystem.dto.ClientDto;
import com.example.accountingsystem.entity.Client;
import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.exception.ObjectNotCreateException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.ClientRepository;
import com.example.accountingsystem.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;
    private final PassportService passportService;
    private final EmployeeService employeeService;

    @Autowired
    public ClientService(ClientRepository clientRepository,
                         PassportService passportService,
                         EmployeeService employeeService) {
        this.clientRepository = clientRepository;
        this.passportService = passportService;
        this.employeeService = employeeService;
    }

    public ApiResponse getAll(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        return new ApiResponse(clients, true);
    }

    public boolean findById(int id) {
        return clientRepository.existsById(id);
    }

    @Logger(message = "fetched all client data", tableName = "client")
    public ApiResponse findAllByCreatedBy() {
        String username = getEmployeeUsername();
        List<Client> clientList = clientRepository.findAllByCreatedBy(username);
        return new ApiResponse(clientList, true);
    }

    @Logger(message = "fetched one client data from", tableName = "client")
    public Client findByIdAndCreatedBy(String empUsername, int id) {
        if (findById(id)) {
            Optional<Client> optionalClient = clientRepository.findByIdAndCreatedBy(id, empUsername);
            if (optionalClient.isPresent()) {
                return optionalClient.get();
            } else {
                throw new ObjectNotCreateException("This employee has not added this client to the system");
            }
        } else {
            throw new ObjectNotFoundException("Client not found");
        }
    }

    @Transactional
    @Logger(message = "added client data to", tableName = "client")
    public ApiResponse save(ClientDto clientDto) {
        String username = getEmployeeUsername();
        Employee employee = employeeService.findByEmail(username);
        Client client = new Client(
                clientDto.getFirstName(),
                clientDto.getLastName(),
                clientDto.getAge(),
                clientDto.getAddress(),
                LocalDateTime.now(),
                employee.getEmail()
        );
        Client savedClient = clientRepository.save(client);
        passportService.save(clientDto.getPassport(), savedClient);
        return new ApiResponse("Client saved", true);
    }

    @Transactional
    @Logger(message = "edited client data from", tableName = "client")
    public ApiResponse edit(ClientDto clientDto, int clientId) {
        String username = getEmployeeUsername();
        Client client = findByIdAndCreatedBy(username, clientId);
        passportService.edit(clientDto.getPassport(), client);
        client.setAddress(clientDto.getAddress());
        client.setAge(clientDto.getAge());
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setAddress(clientDto.getAddress());
        return new ApiResponse("Client edited", true);
    }

    @Transactional
    @Logger(message = "deleted client data from", tableName = "client")
    public ApiResponse delete(int clientId) {
        String username = getEmployeeUsername();
        Client client = findByIdAndCreatedBy(username, clientId);
        clientRepository.delete(client);
        return new ApiResponse("Client deleted", true);
    }

    private String getEmployeeUsername() {
        return ((SecurityUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
