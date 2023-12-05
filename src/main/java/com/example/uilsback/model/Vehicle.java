package com.example.uilsback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "vehicles", schema = "public")
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 71177902570621482L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_brand", nullable = false)
    private String vehicleBrand;

    @Column(name = "vehicle_model", nullable = false)
    private String vehicleModel;

    @Column(name = "license_plate_number", nullable = false)
    private String licensePlateNumber;

    @Column(name = "vehicle_color", nullable = false)
    private String vehicleColor;

    @Column(name = "vehicle_capacity", nullable = false)
    private Integer vehicleCapacity;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, insertable = false, updatable = false)
    private User user;

}
