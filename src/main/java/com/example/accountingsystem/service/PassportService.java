package com.example.accountingsystem.service;

import com.example.accountingsystem.dto.PassportDto;
import com.example.accountingsystem.entity.Passport;
import com.example.accountingsystem.entity.Person;
import com.example.accountingsystem.exception.ObjectExistsException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.repository.PassportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PassportService {

    private final PassportRepository passportRepository;
    private static final String LOG_MESSAGE = "Person with id {}" +
            "{} the table {}";

    @Autowired
    public PassportService(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    private boolean findByNumberAndSeries(String series, String number) {
        if (passportRepository.existsBySeriesAndNumber(series, number)) {
            throw new ObjectExistsException("Passport already exists");
        }
        return true;
    }

    private boolean findByIdentityNumber(String identityNumber) {
        if (passportRepository.existsByIdentityNumber(identityNumber)) {
            throw new ObjectExistsException("Identity number already taken");
        }
        return true;
    }

    private boolean findByIdNotAndIdentityNumber(String identityNumber, int id) {
        if (passportRepository.existsByIdNotAndIdentityNumber(id, identityNumber)) {
            throw new ObjectExistsException("This identity number already taken");
        }
        return true;
    }

    private boolean findByIdNotAndSeriesAndNumber(String series, String number, int id) {
        if (passportRepository.existsByIdNotAndSeriesAndNumber(id, series, number)) {
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

    private Passport findById(int id) {
        Optional<Passport> optionalPassport = passportRepository.findById(id);
        if (optionalPassport.isPresent()) {
            return optionalPassport.get();
        } else {
            String message = "Passport with %d id not found!".formatted(id);
            log.error("Error occurred:{}", message);
            throw new ObjectNotFoundException(message);
        }
    }

    @Transactional
    public void save(PassportDto passportDto, Person person) {
        if (findByNumberAndSeries(passportDto.getSeries(), passportDto.getNumber()) &&
                findByIdentityNumber(passportDto.getIdentityNumber())
        ) {
            Passport passport = new Passport(
                    passportDto.getNumber(),
                    passportDto.getSeries(),
                    passportDto.getIdentityNumber(),
                    passportDto.getNationality(),
                    person
            );
            passportRepository.save(passport);
            log.info(LOG_MESSAGE, person.getId(), "added passport data to", "passport");
        }
    }

    @Transactional
    public void edit(PassportDto passportDto, Person person) {
        Passport passport = findByPerson(person);
        if (findByIdNotAndSeriesAndNumber(passportDto.getSeries(), passportDto.getNumber(), passport.getId())
                && findByIdNotAndIdentityNumber(passportDto.getIdentityNumber(), passport.getId())
        ) {
            passport.setSeries(passportDto.getSeries());
            passport.setNumber(passportDto.getNumber());
            passport.setNationality(passportDto.getNationality());
            passport.setIdentityNumber(passportDto.getIdentityNumber());
            passportRepository.save(passport);
            log.info(LOG_MESSAGE, person.getId(), "edited passport data from", "passport");
        }
    }

    @Transactional
    public void delete(Person person) {
        Passport passport = findByPerson(person);
        passportRepository.delete(passport);
        log.info(LOG_MESSAGE, person.getId(), "delete passport data from", "passport");
    }
}
