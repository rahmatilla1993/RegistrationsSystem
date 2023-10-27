package com.example.accountingsystem.repository;

import com.example.accountingsystem.entity.Advertisement;
import com.example.accountingsystem.projection.IAdvertisement;
import com.example.accountingsystem.projection.IEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementStatisticsRepository extends JpaRepository<Advertisement, Integer> {

    @Query(
            nativeQuery = true,
            value = "select type, count(*) count " +
                    "from advertisement " +
                    "group by type " +
                    "order by count desc " +
                    "limit 1"
    )
    List<IAdvertisement> getPopularTypeOfAdvertisingCosts();

    @Query(
            nativeQuery = true,
            value = "select p.id, p.first_name firstName, p.last_name lastName, " +
                    "p.age, e.email, e.salary, count(*) count " +
                    "from advertisement a " +
                    "         inner join public.employee e on e.employee = a.created_by " +
                    "         inner join public.person p on p.id = e.employee " +
                    "group by a.created_by, p.id, e.employee " +
                    "order by count desc " +
                    "limit 1;"
    )
    List<IEmployee> getEmployeeByMostAdvertisingCosts();

    @Query(
            nativeQuery = true,
            value = "select count(*) from advertisement " +
                    "where date(started_date_time) >= current_date - interval '1 month';"
    )
    Integer getCountOfAdvertisementAddedInLastMonth();

    @Query(
            nativeQuery = true,
            value = "select count(*) from advertisement " +
                    "where period_time between current_date - interval '1 month' and current_date;"
    )
    Integer getCountOfExpiredAdvertisementsInLastMonth();

    @Query(
            nativeQuery = true,
            value = "select type, count(*) count from advertisement " +
                    "group by type order by count desc;"
    )
    List<IAdvertisement> getTypesOfAdvertisingCosts();
}
