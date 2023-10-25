package com.example.accountingsystem.repository;

import com.example.accountingsystem.entity.Department;
import com.example.accountingsystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);

    boolean existsByIdNotAndEmail(int id, String email);

    boolean existsByEmail(String email);

    List<Employee> findAllByDepartment(Department department);
}
