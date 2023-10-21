package com.example.accountingsystem.entity;

import com.example.accountingsystem.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employee")
@PrimaryKeyJoinColumn(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends Person {

    @Column(name = "email", unique = true, nullable = false)
    private String email; //username
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Column(name = "salary")
    private int salary;
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    public Employee(String firstName, String lastName,
                    int age, Role role, int salary,
                    String address, Department department,
                    String email, String password
    ) {
        super(firstName, lastName, age, address);
        this.role = role;
        this.salary = salary;
        this.department = department;
        this.email = email;
        this.password = password;
    }
}
