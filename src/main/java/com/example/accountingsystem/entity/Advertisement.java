package com.example.accountingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "advertisement")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "period_time", nullable = false)
    private LocalDate periodTime;

    @CreationTimestamp
    @Column(name = "started_date_time", updatable = false)
    private LocalDateTime startedDateTime;

    @ManyToOne
    @JoinColumn(
            name = "created_by",
            referencedColumnName = "employee",
            updatable = false
    )
    private Employee createdBy;

}
