package com.example.accountingsystem.service;

import com.example.accountingsystem.dto.PassportDto;
import com.example.accountingsystem.dto.PersonDto;
import com.example.accountingsystem.entity.Department;
import com.example.accountingsystem.entity.Person;
import com.example.accountingsystem.enums.Role;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final DepartmentService departmentService;
    private final PassportService passportService;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository,
                         DepartmentService departmentService,
                         PassportService passportService
    ) {
        this.peopleRepository = peopleRepository;
        this.departmentService = departmentService;
        this.passportService = passportService;
    }

    public Page<Person> getAll(Pageable pageable) {
        return peopleRepository.findAll(pageable);
    }

    public Person findById(int id) {
        Optional<Person> optionalPerson = peopleRepository.findById(id);
        if (optionalPerson.isPresent()) {
            return optionalPerson.get();
        } else throw new ObjectNotFoundException(
                "Employee with %d id not found".formatted(id)
        );
    }

    @Transactional
    public ApiResponse save(PersonDto personDto) {
        String firstName = personDto.getFirstName();
        String lastName = personDto.getLastName();
        String address = personDto.getAddress();
        int age = personDto.getAge();
        int salary = personDto.getSalary();
        Department department = departmentService.findById(personDto.getDepartmentId());
        Person person = new Person(firstName, lastName, age, Role.ROLE_EMPLOYEE,
                salary, address, department);
        Person savedPerson = peopleRepository.save(person);
        passportService.save(personDto.getPassport(), savedPerson);
        return new ApiResponse("Employee saved", true);
    }

    @Transactional
    public ApiResponse edit(PersonDto personDto, int id) {
        Person person = findById(id);
        person.setAddress(personDto.getAddress());
        person.setAge(personDto.getAge());
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setAddress(personDto.getAddress());
        person.setSalary(personDto.getSalary());
        PassportDto passportDto = personDto.getPassport();
        passportService.edit(passportDto, person);
        return new ApiResponse("Employee edited", true);
    }

    @Transactional
    public ApiResponse delete(int id) {
        Person person = findById(id);
        passportService.delete(person);
        peopleRepository.delete(person);
        return new ApiResponse("Deleted", true);
    }
}
