package com.example.accountingsystem.repository;

import com.example.accountingsystem.entity.Employee;
import com.example.accountingsystem.projection.IEmployeeCount;
import com.example.accountingsystem.projection.IEmployeeSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeStatisticsRepository extends JpaRepository<Employee, Integer> {

    @Query(value = "select d.name as departmentName," +
            "count(*) as employeeCount," +
            "(trunc(cast(count(*) as decimal) / (select count(*) from employee), 2) * 100) as employeePercent" +
            " from employee e inner join department d" +
            " on e.department_id = d.id" +
            " group by d.id", nativeQuery = true)
    List<IEmployeeCount> numberOfEmployeesInDepartments();

    List<Employee> findAllByAgeGreaterThan(Integer age);

    List<Employee> findAllByAgeLessThan(Integer age);

    List<Employee> findAllByAge(Integer age);

    @Query(value = "select d.name as departmentName, sum(salary) as sumOfsalary " +
            "from employee e " +
            "inner join public.department d on d.id = e.department_id " +
            "group by d.id", nativeQuery = true)
    List<IEmployeeSalary> getSumOfSalaryAllEmployeesByDepartments();

    @Query(value = "select sum(salary) from employee", nativeQuery = true)
    Integer totalSumOfSalary();
}
