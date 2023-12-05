package com.example.uilsback.controller;

import com.example.uilsback.dto.EditUserRequestDTO;
import com.example.uilsback.dto.EditVehicleDTO;
import com.example.uilsback.dto.RegisterVehicleDTO;
import com.example.uilsback.dto.VehicleDTO;
import com.example.uilsback.exception.CustomException;
import com.example.uilsback.service.interfaces.IUserService;
import com.example.uilsback.service.interfaces.IVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private IVehicleService vehicleService;

    @PreAuthorize("hasAuthority('driver')")
    @PostMapping("/new")
    public void registerVehicle(
            @RequestHeader("idUsuario") String ownerId,
            @RequestBody RegisterVehicleDTO newVehicle
    ) {
        vehicleService.registerVehicle(ownerId, newVehicle);
    }

    @PreAuthorize("hasAuthority('driver')")
    @GetMapping("/all")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(
            @RequestHeader("idUsuario") String ownerId
    ) {
        return ResponseEntity.ok(vehicleService.findByOwnerId(ownerId));
    }

    @PreAuthorize("hasAuthority('driver')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(
            @RequestHeader("idUsuario") String ownerId,
            @PathVariable Long id
    ) {
        Integer deleted = vehicleService.deleteVehicle(ownerId, id);
        if(deleted > 0) {
            return ResponseEntity.ok().build();
        } else {
            throw new CustomException("No se pudo eliminar el vehículo");
        }
    }

    @PutMapping()
    public ResponseEntity<?> edit(
            @RequestBody EditVehicleDTO request,
            @RequestHeader("idUsuario") String userId
    ) {
        if(vehicleService.edit(request, userId)){
            return ResponseEntity.ok(true);
        } else {
            throw new CustomException("No se pudo editar el vehículo");
        }
    }

    @Autowired
    public void setVehicleService(@Qualifier("vehicleServiceImpl") IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

}
