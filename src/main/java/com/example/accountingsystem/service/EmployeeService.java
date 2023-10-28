package com.example.accountingsystem.service;

import com.example.accountingsystem.dto.EmployeeDto;
import com.example.accountingsystem.entity.Department;
import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.enums.Role;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final PassportService passportService;
    private final PasswordEncoder passwordEncoder;

    private static final String LOG_MESSAGE = "Employee with email {} send a request " +
            "to the {} endpoint via the {} method and " +
            "{} the table {}";

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentService departmentService,
                           PassportService passportService,
                           PasswordEncoder passwordEncoder
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentService = departmentService;
        this.passportService = passportService;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse getAll(Pageable pageable, Principal principal) {
        Page<Employee> employees = employeeRepository.findAll(pageable);
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/employee", "GET",
                "fetched all the data from", "employee");
        return new ApiResponse(employees, true);
    }

    public Employee findById(int id, Principal principal) {
        Optional<Employee> optionalPerson = employeeRepository.findById(id);
        if (optionalPerson.isPresent()) {
            log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/employee/%d".formatted(id), "GET",
                    "get one data from", "employee");
            return optionalPerson.get();
        } else {
            String message = "Employee with %d id not found".formatted(id);
            log.error("Exception occurred: {}", message);
            throw new ObjectNotFoundException(message);
        }
    }

    public Employee findByEmail(String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
        if (optionalEmployee.isPresent()) {
            return optionalEmployee.get();
        }
        String mes = "Employee not found";
        log.error("Exception occurred: {}", mes);
        throw new ObjectNotFoundException(mes);
    }

    private boolean existsByIdNotAndEmail(String email, int id) {
        return employeeRepository.existsByIdNotAndEmail(id, email);
    }

    private boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public void employeeIsAvailableInDepartment(Employee employee, String depName) {
        Optional<Department> optionalDepartment = departmentService.findByName(depName);
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            List<Employee> employeeList = employeeRepository.findAllByDepartment(department);
            if (employeeList.contains(employee) || employee.getRole().toString().equals("ROLE_DIRECTOR")) {
                return;
            } else {
                String message = "This employee does not exist in the %s department".formatted(depName);
                log.error("Exception occurred: {}", message);
                throw new ObjectNotFoundException(message);
            }
        }
        throw new ObjectNotFoundException("Department not found");
    }

    @Transactional
    public ApiResponse save(EmployeeDto employeeDto, Principal principal) {
        if (!existsByEmail(employeeDto.getEmail())) {
            Department department = departmentService.findById(employeeDto.getDepartmentId(), principal);
            Employee employee = new Employee(
                    employeeDto.getFirstName(),
                    employeeDto.getLastName(),
                    employeeDto.getAge(),
                    Role.ROLE_EMPLOYEE,
                    employeeDto.getSalary(),
                    employeeDto.getAddress(),
                    department,
                    employeeDto.getEmail(),
                    passwordEncoder.encode(employeeDto.getPassword())
            );
            Employee savedEmployee = employeeRepository.save(employee);
            passportService.save(employeeDto.getPassport(), savedEmployee);
            log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/employee", "POST",
                    "added data to", "employee");
            return new ApiResponse("Employee saved", true);
        }
        String mes = "This email already taken";
        log.error("Exception occurred: {}", mes);
        throw new ObjectExistsException(mes);
    }

    @Transactional
    public ApiResponse edit(EmployeeDto employeeDto, int id, Principal principal) {
        if (!existsByIdNotAndEmail(employeeDto.getEmail(), id)) {
            Employee employee = findById(id, principal);
            Department department = departmentService.findById(employeeDto.getDepartmentId(), principal);
            employee.setAddress(employeeDto.getAddress());
            employee.setAge(employeeDto.getAge());
            employee.setFirstName(employeeDto.getFirstName());
            employee.setLastName(employeeDto.getLastName());
            employee.setAddress(employeeDto.getAddress());
            employee.setSalary(employeeDto.getSalary());
            employee.setDepartment(department);
            employee.setEmail(employeeDto.getEmail());
            employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
            passportService.edit(employeeDto.getPassport(), employee);
            log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/employee/%d".formatted(id), "PUT",
                    "edited data to", "employee");
            return new ApiResponse("Employee edited", true);
        }
        String mes = "This email already taken";
        log.error("Exception occurred: {}", mes);
        throw new ObjectExistsException(mes);
    }

    @Transactional
    public ApiResponse delete(int id, Principal principal) {
        Employee employee = findById(id, principal);
        employeeRepository.delete(employee);
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/employee/%d".formatted(id), "DELETE",
                "delete data from", "employee");
        return new ApiResponse("Deleted", true);
    }

    private String getEmployeeEmail(Principal principal) {
        return principal.getName();
    }
}
