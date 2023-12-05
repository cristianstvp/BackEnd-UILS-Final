package com.example.uilsback.repository;

import com.example.uilsback.model.Trip;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITripRepository extends JpaRepository<Trip, Long> {

    @Transactional
    public Integer deleteByIdAndDriverId(Long id, Long driverId);

    @Query("SELECT v FROM Trip v WHERE v.expirationDate = :actualDate AND v.seatsAvailable > 0 AND v.departureTime > :actualTime AND v.status = :status AND v.seatsAvailable > 0 " +
            "ORDER BY v.id DESC")
    List<Trip> findAllByStatusAndExpirationDateEquals(String status,LocalDate actualDate, LocalTime actualTime);

    @Query("SELECT v FROM Trip v WHERE v.expirationDate = :actualDate AND v.driverId = :driverId AND v.status = :status " +
            "ORDER BY v.id DESC LIMIT 1")
    Optional<Trip> findByDriverIdAndStatusAndExpirationDateEquals(Long driverId, String status, LocalDate actualDate);

}
