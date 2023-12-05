package com.example.uilsback.mapper;

import com.example.uilsback.dto.TripResponseDTO;
import com.example.uilsback.dto.VehicleDTO;
import com.example.uilsback.model.Trip;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ITripMapper {

    ITripMapper INSTANCE = Mappers.getMapper(ITripMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "driver.fullName", target = "driverName")
    @Mapping(source = "seatsAvailable", target = "availableSeats")
    @Mapping(source = "servicePrice", target = "price")
    @Mapping(source = "startingPlace", target = "startingPlace")
    TripResponseDTO tripToTripResponseDTO(Trip trip);

    List<TripResponseDTO> tripsToTripResponseDTOs(List<Trip> trips);

    @AfterMapping
    default void fillArrays(Trip trip, @MappingTarget TripResponseDTO dto) {
        dto.setDirRoutes(new String[]{trip.getAddress1(), trip.getAddress2(), trip.getAddress3(), trip.getAddress4()});
        dto.setCoordinates(new String[]{trip.getCoordinates1(), trip.getCoordinates2(), trip.getCoordinates3(), trip.getCoordinates4()});
        if(trip.getDriver().getVehicle() != null){
            dto.setVehicle(IVehicleMapper.INSTANCE.vehicleToVehicleDTO(trip.getDriver().getVehicle()));
        }
        if(trip.getDepartureTime() != null){
            dto.setDepartureTime(trip.getDepartureTime().toString());
        }

    }

}
