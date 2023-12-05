package com.example.uilsback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripRequestDTO {

    private String[] dirRoutes;

    private String[] coordinates;

    private Float price;

    private String departureTime;

    private String startingPlace;

}
