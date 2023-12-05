package com.example.uilsback.repository;

import com.example.uilsback.model.Vehicle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVehicleRepository extends JpaRepository<Vehicle, Long> {

    public boolean existsByLicensePlateNumber(String licensePlateNumber);

    public List<Vehicle> findAllByOwnerId(Long ownerId);

    @Transactional
    public Integer deleteByOwnerIdAndId(Long ownerId, Long id);

}
