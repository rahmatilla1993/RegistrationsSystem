package com.example.accountingsystem.service;

import com.example.accountingsystem.dto.ClientDto;
import com.example.accountingsystem.dto.EmployeeDto;
import com.example.accountingsystem.entity.Client;
import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.exception.ObjectNotCreateException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Client> getAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Client findById(int id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        }
        throw new ObjectNotFoundException("Client not found");
    }

    public List<Client> findAllByCreatedBy(String employeeUsername) {
        return clientRepository.findAllByCreatedBy(employeeUsername);
    }

    public Client findByIdAndCreatedBy(String empUsername, int id) {
        Optional<Client> optionalClient = clientRepository.findByIdAndCreatedBy(id, empUsername);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        }
        throw new ObjectNotCreateException("This employee has not added this client to the system");
    }

    @Transactional
    public ApiResponse save(ClientDto clientDto) {
        Employee employee = employeeService.findByEmail(clientDto.getEmpEmail());
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
    public ApiResponse edit(ClientDto clientDto, int clientId) {
        Client client = findByIdAndCreatedBy(clientDto.getEmpEmail(), clientId);
        passportService.edit(clientDto.getPassport(), client);
        client.setAddress(clientDto.getAddress());
        client.setAge(clientDto.getAge());
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setAddress(clientDto.getAddress());
        return new ApiResponse("Client edited", true);
    }

    @Transactional
    public ApiResponse delete(int clientId) {
        Client client = findById(clientId);
        clientRepository.delete(client);
        return new ApiResponse("Client deleted", true);
    }
}
