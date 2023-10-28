package com.example.accountingsystem.service;

import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.repository.EmployeeRepository;
import com.example.accountingsystem.security.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
        if (optionalEmployee.isPresent()) {
            Employee user = optionalEmployee.get();
            log.info("User with {} email:{}", email, user);
            return new SecurityUser(user);
        }
        String message = "Employee with %s email not found".formatted(email);
        log.error("Error occurred:{}", message);
        throw new UsernameNotFoundException(message);
    }
}
