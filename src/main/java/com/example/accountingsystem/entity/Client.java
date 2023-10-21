package com.example.accountingsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn(name = "client")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client extends Person {

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    //added by which employee (username)
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    public Client(String firstName, String lastName, int age,
                  String address, LocalDateTime createdAt, String createdBy
    ) {
        super(firstName, lastName, age, address);
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
