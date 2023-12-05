package com.example.uilsback.controller;

import com.example.uilsback.dto.TripRequestDTO;
import com.example.uilsback.dto.TripResponseDTO;
import com.example.uilsback.exception.CustomException;
import com.example.uilsback.service.interfaces.ITripService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private ITripService tripService;

    @GetMapping("/all")
    public ResponseEntity<List<TripResponseDTO>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponseDTO> getTrip(
            @PathVariable Long tripId
    ) {
        TripResponseDTO trip = tripService.getTripById(tripId);
        if(trip != null) {
            return ResponseEntity.ok(trip);
        }

        throw new CustomException("No se encontró el viaje");
    }

    @GetMapping()
    public ResponseEntity<TripResponseDTO> getTrip(
            @RequestHeader("idUsuario") String userId
    ) {
        TripResponseDTO trip = tripService.getTripByUserId(userId);
        if(trip != null) {
            return ResponseEntity.ok(trip);
        }

        throw new CustomException("No tiene viajes activos");
    }

    @PreAuthorize("hasAuthority('driver')")
    @PostMapping("/new")
    public ResponseEntity<?> newTrip(
            @RequestBody TripRequestDTO trip,
            @RequestHeader("idUsuario") String driverId
    ) {
        tripService.createTrip(trip, driverId);
        return ResponseEntity.ok("true");
    }

    @PreAuthorize("hasAuthority('driver')")
    @DeleteMapping("/{tripId}")
    public ResponseEntity<?> deleteTrip(
            @PathVariable Long tripId,
            @RequestHeader("idUsuario") String driverId
    ) {
        if(tripService.deleteTrip(tripId, driverId)) {
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.badRequest().body("false");
    }

    @PreAuthorize("hasAuthority('passenger')")
    @PostMapping("/join/{tripId}")
    public ResponseEntity<?> joinTrip(
            @PathVariable Long tripId,
            @RequestHeader("idUsuario") String userId
    ) {
        if(tripService.joinTrip(tripId, userId)) {
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.badRequest().body("false");
    }

    @PreAuthorize("hasAuthority('passenger')")
    @DeleteMapping("/leave/{tripId}")
    public ResponseEntity<?> leaveTrip(
            @PathVariable Long tripId,
            @RequestHeader("idUsuario") String userId
    ) {
        if(tripService.leaveTrip(tripId, userId)) {
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.badRequest().body("false");
    }

    @PreAuthorize("hasAuthority('driver') || hasAuthority('passenger')")
    @PostMapping("/finish/{tripId}")
    public ResponseEntity<?> finishTrip(
            @PathVariable Long tripId,
            @RequestHeader("idUsuario") String driverId
    ) {
        if(tripService.finishTrip(tripId, driverId)) {
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.badRequest().body("false");
    }

    @Autowired
    public void setTripService(@Qualifier("tripServiceImpl") ITripService tripService){
        this.tripService = tripService;
    }

}
