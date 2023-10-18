package com.example.accountingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passport")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "series", nullable = false)
    private String series;

    @Column(name = "j_sh_shIR")
    private int jShShIR;

    @Column(name = "nationality")
    private String nationality;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    public Passport(int number, String series, int jShShIR, String nationality, Person person) {
        this.number = number;
        this.series = series;
        this.jShShIR = jShShIR;
        this.nationality = nationality;
        this.person = person;
    }
}
