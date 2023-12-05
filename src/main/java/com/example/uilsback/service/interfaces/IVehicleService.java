package com.example.uilsback.service.interfaces;

import com.example.uilsback.dto.EditVehicleDTO;
import com.example.uilsback.dto.RegisterVehicleDTO;
import com.example.uilsback.dto.VehicleDTO;
import com.example.uilsback.model.Vehicle;
import com.example.uilsback.repository.IVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IVehicleService {

    public Vehicle registerVehicle(String ownerId, RegisterVehicleDTO newVehicle);

    public List<VehicleDTO> findByOwnerId(String ownerId);

    public Integer deleteVehicle(String ownerId, Long id);

    public boolean edit(EditVehicleDTO vehiculo, String userId);

}
