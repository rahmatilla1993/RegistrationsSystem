package com.example.accountingsystem.service;

import com.example.accountingsystem.annotation.Logger;
import com.example.accountingsystem.entity.Department;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Logger(message = "fetch all departments from", tableName = "department")
    public ApiResponse getAll(Pageable pageable) {
        Page<Department> departments = departmentRepository.findAll(pageable);
        return new ApiResponse(departments, true);
    }

    @Logger(message = "get department by id from", tableName = "department")
    public Department findById(int id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            return optionalDepartment.get();
        } else {
            throw new ObjectNotFoundException("Department with %d id not found".formatted(id));
        }

    }

    @Logger(message = "added department data to", tableName = "department")
    public ApiResponse save(Department department) {
        if (findByName(department.getName()).isEmpty()) {
            departmentRepository.save(department);
            return new ApiResponse("Department added", true);
        }
        throw new ObjectExistsException("This department already exists");
    }

    @Logger(message = "deleted department from", tableName = "department")
    public ApiResponse delete(int id) {
        Department department = findById(id);
        if (department != null) {
            departmentRepository.delete(department);
        }
        return new ApiResponse("Department deleted", true);
    }

    @Logger(message = "edited department data from", tableName = "department")
    public ApiResponse edit(Department department, int id) {
        Department editedDep = findById(id);
        if (findByName(department.getName()).isEmpty()) {
            editedDep.setName(department.getName());
            departmentRepository.save(editedDep);
            return new ApiResponse("Department edited", true);
        }
        throw new ObjectExistsException("This department already exists");
    }

    public Optional<Department> findByName(String depName) {
        return departmentRepository.findByName(depName);
    }
}
