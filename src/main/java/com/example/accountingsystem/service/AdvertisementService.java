package com.example.accountingsystem.service;

import com.example.accountingsystem.annotation.Logger;
import com.example.accountingsystem.dto.AdvertisementDto;
import com.example.accountingsystem.entity.Advertisement;
import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.exception.ObjectNotCreateException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.AdvertisementRepository;
import com.example.accountingsystem.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdvertisementService {

    private static final String DEPARTMENT_SALES = "Sales";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final AdvertisementRepository advertisementRepository;
    private final EmployeeService employeeService;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository,
                                EmployeeService employeeService) {
        this.advertisementRepository = advertisementRepository;
        this.employeeService = employeeService;
    }

    @Logger(
            message = "fetched all advertisement data from",
            tableName = "advertisement"
    )
    public ApiResponse getAll(Pageable pageable) {
        Page<Advertisement> advertisements = advertisementRepository.findAll(pageable);
        return new ApiResponse(advertisements, true);
    }

    @Logger(
            message = "fetched all advertisement data by paging from",
            tableName = "advertisement"
    )
    public ApiResponse getAllByPage(int page, int limit) {
        List<Advertisement> advertisementList = advertisementRepository
                .findAll(PageRequest.of(page, limit))
                .getContent();
        return new ApiResponse(advertisementList, true);
    }

    @Logger(
            message = "fetched advertisement data from",
            tableName = "advertisement"
    )
    public Advertisement findById(int id) {
        return advertisementRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Advertisement not found"));
    }

    private boolean isAddAdvertisementByEmployee(Advertisement advertisement, Employee employee) {
        return advertisement.getCreatedBy().equals(employee);
    }

    //Only employees and managers in the sales department and director add advertisement
    @Transactional
    @Logger(
            message = "added advertisement data to",
            tableName = "advertisement"
    )
    public ApiResponse save(AdvertisementDto advertisementDto) {
        String username = getEmployeeUsername();
        Employee employee = employeeService.findByEmail(username);
        employeeService.employeeIsAvailableInDepartment(employee, DEPARTMENT_SALES);
        Advertisement advertisement = Advertisement.builder()
                .cost(advertisementDto.getCost())
                .type(advertisementDto.getType())
                .periodTime(LocalDate.parse(advertisementDto.getPeriod(), formatter))
                .startedDateTime(LocalDateTime.now())
                .createdBy(employee)
                .build();
        advertisementRepository.save(advertisement);
        return new ApiResponse("Saved", true);
    }

    //Only employee and managers in the sales department and director add advertisement
    @Transactional
    @Logger(
            message = "edited advertisement data from",
            tableName = "advertisement"
    )
    public ApiResponse edit(AdvertisementDto advertisementDto, int id) {
        String username = getEmployeeUsername();
        Employee employee = employeeService.findByEmail(username);
        employeeService.employeeIsAvailableInDepartment(employee, DEPARTMENT_SALES);
        Advertisement advertisement = findById(id);
        if (isAddAdvertisementByEmployee(advertisement, employee)) {
            advertisement.setCost(advertisementDto.getCost());
            advertisement.setType(advertisementDto.getType());
            advertisement.setPeriodTime(LocalDate.parse(advertisementDto.getPeriod(), formatter));
            return new ApiResponse("Edited", true);
        }
        throw new ObjectNotCreateException("This employee can't edit advertisement." +
                "Because this employee has not added an advertisement");
    }

    //Only managers in the sales department and director add advertisement
    @Transactional
    @Logger(message = "delete advertisement data from", tableName = "advertisement")
    public ApiResponse delete(int id) {
        String username = getEmployeeUsername();
        Employee employee = employeeService.findByEmail(username);
        employeeService.employeeIsAvailableInDepartment(employee, DEPARTMENT_SALES);
        Advertisement advertisement = findById(id);
        advertisementRepository.delete(advertisement);
        return new ApiResponse("Deleted", true);
    }

    private String getEmployeeUsername() {
        return ((SecurityUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
