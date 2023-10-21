package com.example.accountingsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "passport",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"series", "number"})}
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "series", nullable = false)
    private String series;

    @Column(name = "identity_number", unique = true)
    private String identityNumber;

    @Column(name = "nationality")
    private String nationality;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @JsonIgnore
    private Person person;

    public Passport(String number, String series, String identityNumber, String nationality, Person person) {
        this.number = number;
        this.series = series;
        this.identityNumber = identityNumber;
        this.nationality = nationality;
        this.person = person;
    }
}
