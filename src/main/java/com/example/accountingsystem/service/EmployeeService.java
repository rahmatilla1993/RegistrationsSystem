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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final PassportService passportService;
    private final PasswordEncoder passwordEncoder;

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

    public ApiResponse getAll(Pageable pageable) {
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return new ApiResponse(employees, true);
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
            if (employeeList.contains(employee)) {
                return;
            } else throw new ObjectNotFoundException(
                    "This employee does not exist in the %s department".formatted(depName)
            );
        }
        throw new ObjectNotFoundException("Department not found");
    }

    @Transactional
    public ApiResponse save(EmployeeDto employeeDto) {
        if (!existsByEmail(employeeDto.getEmail())) {
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
                    passwordEncoder.encode(employeeDto.getPassword())
            );
            Employee savedEmployee = employeeRepository.save(employee);
            passportService.save(employeeDto.getPassport(), savedEmployee);
            return new ApiResponse("Employee saved", true);
        }
        throw new ObjectExistsException("This email already taken");
    }

    @Transactional
    public ApiResponse edit(EmployeeDto employeeDto, int id) {
        if (!existsByIdNotAndEmail(employeeDto.getEmail(), id)) {
            Employee employee = findById(id);
            Department department = departmentService.findById(employeeDto.getDepartmentId());
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
            return new ApiResponse("Employee edited", true);
        }
        throw new ObjectExistsException("This email already taken");
    }

    @Transactional
    public ApiResponse delete(int id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
        return new ApiResponse("Deleted", true);
    }
}
