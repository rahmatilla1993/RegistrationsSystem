package com.example.accountingsystem.repository;

import com.example.accountingsystem.entity.Passport;
import com.example.accountingsystem.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Integer> {

    Optional<Passport> findBySeriesAndNumber(String series, int number);

    Optional<Passport> findByPerson(Person person);

    Optional<Passport> findByIdAndSeriesAndNumber(int id, String series, int number);
}
