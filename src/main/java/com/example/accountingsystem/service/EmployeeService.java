package com.example.accountingsystem.service;

import com.example.accountingsystem.dto.EmployeeDto;
import com.example.accountingsystem.entity.Department;
import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.enums.Role;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final PassportService passportService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentService departmentService,
                           PassportService passportService
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentService = departmentService;
        this.passportService = passportService;
    }

    public Page<Employee> getAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Employee findById(int id) {
        Optional<Employee> optionalPerson = employeeRepository.findById(id);
        if (optionalPerson.isPresent()) {
            return optionalPerson.get();
        } else throw new ObjectNotFoundException(
                "Employee with %d id not found".formatted(id)
        );
    }

    public Employee findByEmail(String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
        if (optionalEmployee.isPresent()) {
            return optionalEmployee.get();
        }
        throw new ObjectNotFoundException("Employee not found");
    }

    private boolean findByIdNotAndEmail(String email, int id) {
        if (employeeRepository.existsByIdNotAndEmail(id, email)) {
            throw new ObjectExistsException("This email already taken");
        }
        return true;
    }

    @Transactional
    public ApiResponse save(EmployeeDto employeeDto) {
        Department department = departmentService.findById(employeeDto.getDepartmentId());
        Employee employee = new Employee(
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getAge(),
                Role.ROLE_EMPLOYEE,
                employeeDto.getSalary(),
                employeeDto.getAddress(),
                department,
                employeeDto.getEmail(),
                employeeDto.getPassword()
        );
        Employee savedEmployee = employeeRepository.save(employee);
        passportService.save(employeeDto.getPassport(), savedEmployee);
        return new ApiResponse("Employee saved", true);
    }

    @Transactional
    public ApiResponse edit(EmployeeDto employeeDto, int id) {
        Employee employee = findById(id);
        Department department = departmentService.findById(employeeDto.getDepartmentId());
        employee.setAddress(employeeDto.getAddress());
        employee.setAge(employeeDto.getAge());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setAddress(employeeDto.getAddress());
        employee.setSalary(employeeDto.getSalary());
        employee.setDepartment(department);
        passportService.edit(employeeDto.getPassport(), employee);
        return new ApiResponse("Employee edited", true);
    }

    @Transactional
    public ApiResponse delete(int id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
        return new ApiResponse("Deleted", true);
    }
}
