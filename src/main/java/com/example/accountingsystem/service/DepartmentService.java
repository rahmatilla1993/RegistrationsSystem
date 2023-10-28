package com.example.accountingsystem.service;

import com.example.accountingsystem.entity.Department;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private static final String LOG_MESSAGE = "Director with email {} send a request " +
            "to the {} endpoint via the {} method and " +
            "{} the table {}";

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public ApiResponse getAll(Pageable pageable, Principal principal) {
        Page<Department> departments = departmentRepository.findAll(pageable);
        log.info(LOG_MESSAGE, getEmpEmail(principal), "/api/department", "GET",
                "fetched all departments data from", "department");
        return new ApiResponse(departments, true);
    }

    public Department findById(int id, Principal principal) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            log.info(LOG_MESSAGE, getEmpEmail(principal), "/api/department/%d".formatted(id), "GET",
                    "fetched one department data from", "department");
            return optionalDepartment.get();
        } else {
            String message = "Department with %d id not found".formatted(id);
            log.info("Error occurred:{}", message);
            throw new ObjectNotFoundException(message);
        }

    }

    public ApiResponse save(Department department, Principal principal) {
        if (findByName(department.getName()).isEmpty()) {
            departmentRepository.save(department);
            log.info(LOG_MESSAGE, getEmpEmail(principal), "/api/department", "POST",
                    "added department data to", "department");
            return new ApiResponse("Department added", true);
        }
        String message = "This department already exists";
        log.error("Error occurred:{}", message);
        throw new ObjectExistsException(message);
    }

    public ApiResponse delete(int id, Principal principal) {
        Department department = findById(id, principal);
        if (department != null) {
            departmentRepository.delete(department);
        }
        log.info(LOG_MESSAGE, getEmpEmail(principal), "/api/department/%d".formatted(id), "DELETE",
                "deleted department data from", "department");
        return new ApiResponse("Department deleted", true);
    }

    public ApiResponse edit(Department department, int id, Principal principal) {
        Department editedDep = findById(id, principal);
        if (findByName(department.getName()).isEmpty()) {
            editedDep.setName(department.getName());
            departmentRepository.save(editedDep);
            log.info(LOG_MESSAGE, getEmpEmail(principal), "/api/department/%d".formatted(id), "PUT",
                    "edited department data from", "department");
            return new ApiResponse("Department edited", true);
        }
        String message = "This department already exists";
        log.error("Error occurred:{}", message);
        throw new ObjectExistsException(message);
    }

    public Optional<Department> findByName(String depName) {
        return departmentRepository.findByName(depName);
    }

    private String getEmpEmail(Principal principal) {
        return principal.getName();
    }
}
