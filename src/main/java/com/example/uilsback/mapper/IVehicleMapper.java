package com.example.uilsback.mapper;

import com.example.uilsback.dto.VehicleDTO;
import com.example.uilsback.model.Trip;
import com.example.uilsback.model.Vehicle;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IVehicleMapper {

    IVehicleMapper INSTANCE = Mappers.getMapper(IVehicleMapper.class);

    @Mapping(source = "vehicleBrand", target = "brand")
    @Mapping(source = "vehicleModel", target = "model")
    @Mapping(source = "licensePlateNumber", target = "licensePlate")
    @Mapping(source = "vehicleColor", target = "color")
    @Mapping(source = "vehicleCapacity", target = "capacity")
    VehicleDTO vehicleToVehicleDTO(Vehicle vehicle);

    List<VehicleDTO> vehiclesToVehicleDTOs(List<Vehicle> vehicles);

}
