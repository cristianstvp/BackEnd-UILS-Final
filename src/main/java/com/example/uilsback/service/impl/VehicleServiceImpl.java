package com.example.uilsback.service.impl;

import com.example.uilsback.dto.EditVehicleDTO;
import com.example.uilsback.dto.RegisterVehicleDTO;
import com.example.uilsback.dto.VehicleDTO;
import com.example.uilsback.mapper.IVehicleMapper;
import com.example.uilsback.model.User;
import com.example.uilsback.model.Vehicle;
import com.example.uilsback.repository.IUserRepository;
import com.example.uilsback.repository.IVehicleRepository;
import com.example.uilsback.service.interfaces.IVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements IVehicleService {

    private IVehicleRepository vehicleRepository;

    private IUserRepository userRepository;

    @Override
    public Vehicle registerVehicle(String ownerId, RegisterVehicleDTO newVehicle) {

        if (vehicleRepository.existsByLicensePlateNumber(newVehicle.getLicensePlate())) {
            return null;
        }

        Vehicle vehicle = Vehicle
                .builder()
                .vehicleBrand(newVehicle.getBrand())
                .vehicleModel(newVehicle.getModel())
                .licensePlateNumber(newVehicle.getLicensePlate())
                .vehicleColor(newVehicle.getColor())
                .vehicleCapacity(newVehicle.getCapacity())
                .ownerId(Long.valueOf(ownerId))
                .build();

        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<VehicleDTO> findByOwnerId(String ownerId) {
        List<Vehicle> vehiculos = vehicleRepository.findAllByOwnerId(Long.valueOf(ownerId));

        List<VehicleDTO> vehiculosDTO = IVehicleMapper.INSTANCE.vehiclesToVehicleDTOs(vehiculos);

        return vehiculosDTO;
    }

    @Override
    public boolean edit(EditVehicleDTO vehiculo, String userId) {
        User usuario = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Vehicle vehiculoEditado = usuario.getVehicle();
        if (vehiculoEditado != null) {
            if (vehiculo.getBrand() != null) {
                vehiculoEditado.setVehicleBrand(vehiculo.getBrand());
            }
            if (vehiculo.getModel() != null) {
                vehiculoEditado.setVehicleModel(vehiculo.getModel());
            }
            if (vehiculo.getLicensePlate() != null) {
                vehiculoEditado.setLicensePlateNumber(vehiculo.getLicensePlate());
            }
            if (vehiculo.getColor() != null) {
                vehiculoEditado.setVehicleColor(vehiculo.getColor());
            }
            if (vehiculo.getCapacity() != null) {
                vehiculoEditado.setVehicleCapacity(vehiculo.getCapacity());
            }
            vehicleRepository.save(vehiculoEditado);
            return true;
        } else {
            throw new RuntimeException("El usuario no tiene vehículo");
        }
    }

    @Override
    public Integer deleteVehicle(String ownerId, Long id) {
        return vehicleRepository.deleteByOwnerIdAndId(Long.valueOf(ownerId), id);
    }

    @Autowired
    public void setVehicleRepository(IVehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
