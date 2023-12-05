package com.example.uilsback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "trips", schema = "public")
public class Trip implements Serializable {
    private static final long serialVersionUID = -8217369680326727710L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "seats_available", nullable = false)
    private Integer seatsAvailable;

    @Column(name = "service_price", nullable = false)
    private Float servicePrice;

    @Column(name = "departure_time", nullable = false)
    private Time departureTime;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "starting_place", nullable = false)
    private String startingPlace;

    @Column(name = "address_1", nullable = false)
    private String address1;

    @Column(name = "coordinates_1", nullable = false)
    private String coordinates1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "coordinates_2")
    private String coordinates2;

    @Column(name = "address_3")
    private String address3;

    @Column(name = "coordinates_3")
    private String coordinates3;

    @Column(name = "address_4")
    private String address4;

    @Column(name = "coordinates_4")
    private String coordinates4;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", insertable = false, updatable = false)
    private User driver;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private Chat chat;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "trip_id", insertable = false, updatable = false)
    private List<UserTrip> userTrip;

}