package com.example.uilsback.service.interfaces;

import com.example.uilsback.dto.TripRequestDTO;
import com.example.uilsback.dto.TripResponseDTO;
import com.example.uilsback.model.Trip;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITripService {

    public List<TripResponseDTO> getAllTrips();

    public void createTrip(TripRequestDTO trip, String driverId);

    public boolean deleteTrip(Long tripId, String driverId);

    public boolean joinTrip(Long tripId, String userId);

    public boolean leaveTrip(Long tripId, String userId);

    public TripResponseDTO getTripById(Long tripId);

    public TripResponseDTO getTripByUserId(String userId);

    public boolean finishTrip(Long tripId, String userId);

    public boolean finishTripDriver(Long tripId, String driverId);

    public boolean finishTripUser(Long tripId, String driverId);

}
