package com.example.accountingsystem.repository;

import com.example.accountingsystem.entity.Passport;
import com.example.accountingsystem.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Integer> {

    boolean existsBySeriesAndNumber(String series, String number);

    Optional<Passport> findByPerson(Person person);

    boolean existsByIdNotAndSeriesAndNumber(int id, String series, String number);

    boolean existsByIdentityNumber(String identityNumber);

    boolean existsByIdNotAndIdentityNumber(int id, String identityNumber);
}
