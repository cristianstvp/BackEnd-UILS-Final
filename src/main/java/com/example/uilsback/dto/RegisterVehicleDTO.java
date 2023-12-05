package com.example.uilsback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterVehicleDTO {

    String brand;

    String model;

    String licensePlate;

    String color;

    Integer capacity;

}
