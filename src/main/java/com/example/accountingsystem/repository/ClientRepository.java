package com.example.accountingsystem.repository;

import com.example.accountingsystem.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findAllByCreatedBy(String employeeUsername);

    Optional<Client> findByIdAndCreatedBy(int id, String empUsername);
    boolean existsById(int id);
}
