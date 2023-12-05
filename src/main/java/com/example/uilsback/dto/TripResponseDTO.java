package com.example.uilsback.dto;

import com.example.uilsback.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripResponseDTO {

    private Long id;

    private String startingPlace;

    private String driverName;

    private String[] dirRoutes;

    private Integer availableSeats;

    private String[] coordinates;

    private VehicleDTO vehicle;

    private Float price;

    private String departureTime;

}
