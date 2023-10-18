package com.example.accountingsystem.service;

import com.example.accountingsystem.dto.PassportDto;
import com.example.accountingsystem.entity.Passport;
import com.example.accountingsystem.entity.Person;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.repository.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PassportService {

    private final PassportRepository passportRepository;

    @Autowired
    public PassportService(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    private boolean findByNumberAndSeries(String series, int number) {
        Optional<Passport> optional = passportRepository.findBySeriesAndNumber(series, number);
        if (optional.isPresent()) {
            throw new ObjectExistsException("Passport already exists");
        }
        return true;
    }

    private boolean findByIdAndSeriesAndNumber(String series, int number, int id) {
        Optional<Passport> optional =
                passportRepository.findByIdAndSeriesAndNumber(id, series, number);
        if (optional.isEmpty()) {
            throw new ObjectExistsException("Passport already exists");
        }
        return true;
    }

    private Passport findByPerson(Person person) {
        Optional<Passport> optional = passportRepository.findByPerson(person);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new ObjectNotFoundException("Passport not found");
    }

    public Passport findById(int id) {
        Optional<Passport> optionalPassport = passportRepository.findById(id);
        if (optionalPassport.isPresent()) {
            return optionalPassport.get();
        } else throw new ObjectNotFoundException(
                "Passport with %d id not found!".formatted(id)
        );
    }

    @Transactional
    public void save(PassportDto passportDto, Person person) {
        if (findByNumberAndSeries(passportDto.getSeries(), passportDto.getNumber())) {
            Passport passport = new Passport(
                    passportDto.getNumber(),
                    passportDto.getSeries(),
                    passportDto.getIdentityNumber(),
                    passportDto.getNationality(),
                    person
            );
            passportRepository.save(passport);
        }
    }

    @Transactional
    public void edit(PassportDto passportDto, Person person) {
        Passport passport = findByPerson(person);
        passport.setSeries(passportDto.getSeries());
        passport.setNumber(passportDto.getNumber());
        passport.setNationality(passportDto.getNationality());
        passport.setJShShIR(passportDto.getIdentityNumber());
        passportRepository.save(passport);
    }

    @Transactional
    public void delete(Person person) {
        Passport passport = findByPerson(person);
        passportRepository.delete(passport);
    }
}
