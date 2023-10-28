package com.example.accountingsystem.service;

import com.example.accountingsystem.dto.ClientDto;
import com.example.accountingsystem.dto.EmployeeDto;
import com.example.accountingsystem.entity.Client;
import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.exception.ObjectNotCreateException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final PassportService passportService;
    private final EmployeeService employeeService;

    private static final String LOG_MESSAGE = "Employee with email {} send a request " +
            "to the {} endpoint via the {} method and " +
            "{} the table {}";

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

    public ApiResponse findAllByCreatedBy(Principal principal) {
        List<Client> clientList = clientRepository.findAllByCreatedBy(getEmpEmail(principal));
        log.info(LOG_MESSAGE, getEmpEmail(principal), "/api/client", "GET",
                "fetched all clients data from", "client");
        return new ApiResponse(clientList, true);
    }

    public Client findByIdAndCreatedBy(String empUsername, int id) {
        if (findById(id)) {
            Optional<Client> optionalClient = clientRepository.findByIdAndCreatedBy(id, empUsername);
            if (optionalClient.isPresent()) {
                log.info(LOG_MESSAGE, empUsername, "/api/client/%d".formatted(id), "GET",
                        "fetched one client data from", "client");
                return optionalClient.get();
            } else {
                String message = "This employee has not added this client to the system";
                log.error(message);
                throw new ObjectNotCreateException(message);
            }
        } else {
            String message = "Client not found";
            log.error("Exception occurred: {}", message);
            throw new ObjectNotFoundException(message);
        }
    }

    @Transactional
    public ApiResponse save(ClientDto clientDto, Principal principal) {
        Employee employee = employeeService.findByEmail(getEmpEmail(principal));
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
        log.info(LOG_MESSAGE, getEmpEmail(principal), "/api/client", "POST",
                "added client object", "client");
        return new ApiResponse("Client saved", true);
    }

    @Transactional
    public ApiResponse edit(ClientDto clientDto, int clientId, Principal principal) {
        Client client = findByIdAndCreatedBy(getEmpEmail(principal), clientId);
        passportService.edit(clientDto.getPassport(), client);
        client.setAddress(clientDto.getAddress());
        client.setAge(clientDto.getAge());
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setAddress(clientDto.getAddress());
        log.info(LOG_MESSAGE, getEmpEmail(principal), "/api/client/%d".formatted(clientId), "PUT",
                "edited client data from", "client");
        return new ApiResponse("Client edited", true);
    }

    @Transactional
    public ApiResponse delete(int clientId, Principal principal) {
        Client client = findByIdAndCreatedBy(getEmpEmail(principal), clientId);
        clientRepository.delete(client);
        log.info(LOG_MESSAGE, getEmpEmail(principal), "/api/client/%d".formatted(clientId), "DELETE",
                "deleted client data from", "client");
        return new ApiResponse("Client deleted", true);
    }

    private String getEmpEmail(Principal principal) {
        return principal.getName();
    }
}
