package com.example.accountingsystem.service;

import com.example.accountingsystem.dto.AdvertisementDto;
import com.example.accountingsystem.entity.Advertisement;
import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.exception.ObjectNotCreateException;
import com.example.accountingsystem.exception.ObjectNotFoundException;
import com.example.accountingsystem.payload.ApiResponse;
import com.example.accountingsystem.repository.AdvertisementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class AdvertisementService {

    private static final String DEPARTMENT_SALES = "Sales";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final AdvertisementRepository advertisementRepository;
    private final EmployeeService employeeService;

    private static final String LOG_MESSAGE = "Employee with email {} send a request " +
            "to the {} endpoint via the {} method and " +
            "{} the table {}";

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository,
                                EmployeeService employeeService) {
        this.advertisementRepository = advertisementRepository;
        this.employeeService = employeeService;
    }

    public ApiResponse getAll(Pageable pageable, Principal principal) {
        Page<Advertisement> advertisements = advertisementRepository.findAll(pageable);
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/advertisement", "GET",
                "fetched all advertisement data from", "advertisement");
        return new ApiResponse(advertisements, true);
    }

    public ApiResponse getAllByPage(int page, int limit, Principal principal) {
        List<Advertisement> advertisementList = advertisementRepository
                .findAll(PageRequest.of(page, limit))
                .getContent();
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/advertisement", "GET",
                "fetched all advertisement data by paging from", "advertisement");
        return new ApiResponse(advertisementList, true);
    }

    public Advertisement findById(int id, Principal principal) {
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/advertisement/%d".formatted(id), "GET",
                "fetched advertisement data from", "advertisement");
        return advertisementRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Advertisement not found"));
    }

    private boolean isAddAdvertisementByEmployee(Advertisement advertisement, Employee employee) {
        return advertisement.getCreatedBy().equals(employee);
    }

    //Only employees and managers in the sales department and director add advertisement
    @Transactional
    public ApiResponse save(AdvertisementDto advertisementDto, Principal principal) {
        Employee employee = employeeService.findByEmail(getEmployeeEmail(principal));
        employeeService.employeeIsAvailableInDepartment(employee, DEPARTMENT_SALES);
        Advertisement advertisement = Advertisement.builder()
                .cost(advertisementDto.getCost())
                .type(advertisementDto.getType())
                .periodTime(LocalDate.parse(advertisementDto.getPeriod(), formatter))
                .startedDateTime(LocalDateTime.now())
                .createdBy(employee)
                .build();
        advertisementRepository.save(advertisement);
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/advertisement", "POST",
                "added advertisement data to", "advertisement");
        return new ApiResponse("Saved", true);
    }

    //Only employee and managers in the sales department and director add advertisement
    @Transactional
    public ApiResponse edit(AdvertisementDto advertisementDto, int id, Principal principal) {
        Employee employee = employeeService.findByEmail(getEmployeeEmail(principal));
        employeeService.employeeIsAvailableInDepartment(employee, DEPARTMENT_SALES);
        Advertisement advertisement = findById(id, principal);
        if (isAddAdvertisementByEmployee(advertisement, employee)) {
            advertisement.setCost(advertisementDto.getCost());
            advertisement.setType(advertisementDto.getType());
            advertisement.setPeriodTime(LocalDate.parse(advertisementDto.getPeriod(), formatter));
            log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/advertisement/%d".formatted(id), "PUT",
                    "edited advertisement data from", "advertisement");
            return new ApiResponse("Edited", true);
        }
        String message = "This employee can't edit advertisement." +
                "Because this employee has not added an advertisement";
        log.error("Exception occurred:{}", message);
        throw new ObjectNotCreateException(message);
    }

    //Only managers in the sales department and director add advertisement
    @Transactional
    public ApiResponse delete(int id, Principal principal) {
        Employee employee = employeeService.findByEmail(getEmployeeEmail(principal));
        employeeService.employeeIsAvailableInDepartment(employee, DEPARTMENT_SALES);
        Advertisement advertisement = findById(id, principal);
        advertisementRepository.delete(advertisement);
        log.info(LOG_MESSAGE, getEmployeeEmail(principal), "/api/advertisement/%d".formatted(id), "DELETE",
                "delete advertisement data from", "advertisement");
        return new ApiResponse("Deleted", true);
    }

    private String getEmployeeEmail(Principal principal) {
        return principal.getName();
    }
}
