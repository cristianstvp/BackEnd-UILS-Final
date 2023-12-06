package com.example.uilsback.service.impl;

import com.example.uilsback.dto.TripRequestDTO;
import com.example.uilsback.dto.TripResponseDTO;
import com.example.uilsback.exception.CustomException;
import com.example.uilsback.mapper.ITripMapper;
import com.example.uilsback.model.Chat;
import com.example.uilsback.model.Trip;
import com.example.uilsback.model.User;
import com.example.uilsback.model.UserTrip;
import com.example.uilsback.repository.IChatRepository;
import com.example.uilsback.repository.ITripRepository;
import com.example.uilsback.repository.IUserRepository;
import com.example.uilsback.repository.IUserTripRepository;
import com.example.uilsback.service.interfaces.IChatService;
import com.example.uilsback.service.interfaces.ITripService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.ap.shaded.freemarker.template.SimpleDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.DoubleToIntFunction;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements ITripService {

    private ITripRepository tripRepository;

    private IUserRepository userRepository;

    private IChatService chatService;

    private IUserTripRepository userTripRepository;

    private IChatRepository chatRepository;

    public static String formatearFecha(Date fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        return formato.format(fecha);
    }

    @Override
    public List<TripResponseDTO> getAllTrips() {
        LocalDate actualDate = LocalDate.now();
        LocalTime actualTime = LocalTime.now();
        String status = "active";

        List<Trip> trips = tripRepository.findAllByStatusAndExpirationDateEquals(status, actualDate, actualTime);

        List<TripResponseDTO> result = ITripMapper.INSTANCE.tripsToTripResponseDTOs(trips);

        return result;
    }

    @Override
    public TripResponseDTO getTripById(Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(
                () -> new CustomException("No se encontró el viaje")
        );
        TripResponseDTO tripResponseDTO = ITripMapper.INSTANCE.tripToTripResponseDTO(trip);
        return tripResponseDTO;
    }

    @Override
    public TripResponseDTO getTripByUserId(String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("El usuario ya existe"));
        System.out.println(user.getRole().toString());
        String status = "active";
        Trip trip = null;
        if (user.getRole().toString().equals("driver")) {
            trip = tripRepository.findByDriverIdAndStatusAndExpirationDateEquals(Long.valueOf(userId), status, LocalDate.now()).orElseThrow(
                    () -> new CustomException("No tiene viajes activos")
            );
            if (trip.getStatus().equals("inactive")) {
                throw new CustomException("No tiene viajes activos");
            }
        }

        if (user.getRole().toString().equals("passenger")) {

            List<UserTrip> userTrips = userTripRepository.findAllByUserIdAndStatus(Long.valueOf(userId), status).orElseThrow(
                    () -> new CustomException("No se encontraron viajes")
            );

            List<Trip> trips = userTrips.stream().map(UserTrip::getTrip).toList();
            if (trips.isEmpty()) {
                throw new CustomException("No se encontraron viajes");
            }
            for(Trip temp : trips) {
                if(temp.getExpirationDate().isEqual(LocalDate.now()) && temp.getStatus().equals("active")) {
                    trip = temp;
                }
            }

            if(trips.size() == 0) {
                return null;
            }
        }
        TripResponseDTO tripResponseDTO = ITripMapper.INSTANCE.tripToTripResponseDTO(trip);
        return tripResponseDTO;
    }

    @Override
    public void createTrip(TripRequestDTO trip, String driverId) {

        User driver = userRepository.findById(Long.valueOf(driverId))
                .orElseThrow(() -> new CustomException("No existe el usuario"));

        Chat chat = chatService.createChat(Long.valueOf(driverId));
        LocalDate localDate = LocalDate.now();
        Trip newTrip = Trip
                .builder()
                .startingPlace(trip.getStartingPlace())
                .driverId(Long.valueOf(driverId))
                .address1(trip.getDirRoutes()[0])
                .coordinates1(trip.getCoordinates()[0])
                .seatsAvailable(driver.getVehicle().getVehicleCapacity())
                .servicePrice(trip.getPrice())
                .chatId(chat.getId())
                .departureTime(Time.valueOf(trip.getDepartureTime()))
                .expirationDate(localDate)
                .status("active")
                .build();
        if (trip.getDirRoutes().length > 1) {
            newTrip.setAddress2(trip.getDirRoutes()[1]);
            newTrip.setCoordinates2(trip.getCoordinates()[1]);
        }
        if (trip.getDirRoutes().length > 2) {
            newTrip.setAddress3(trip.getDirRoutes()[2]);
            newTrip.setCoordinates3(trip.getCoordinates()[2]);
        }
        if (trip.getDirRoutes().length > 3) {
            newTrip.setAddress4(trip.getDirRoutes()[3]);
            newTrip.setCoordinates4(trip.getCoordinates()[3]);
        }

        Trip saved = tripRepository.save(newTrip);
    }

    @Override
    public boolean joinTrip(Long tripId, String userId) {

        Trip trip = tripRepository.findById(tripId).orElseThrow();

        System.out.println(LocalTime.now().minusHours(1).compareTo(LocalTime.now()));

        if (trip.getDepartureTime().compareTo(Time.valueOf(LocalTime.now())) < 0) {
            return false;
        }

        System.out.println(trip.getSeatsAvailable());

        if(trip.getSeatsAvailable() > 0 && userRepository.existsById(Long.valueOf(userId)) && !userTripRepository.existsByTripIdAndUserId(tripId, Long.valueOf(userId)) && trip.getStatus().equals("active")) {
            trip.setSeatsAvailable(trip.getSeatsAvailable() - 1);
            userTripRepository.save(UserTrip.builder().tripId(tripId).userId(Long.valueOf(userId)).status("active").build());
            tripRepository.save(trip);
            chatService.addUserToChat(trip.getChatId(), Long.valueOf(userId));
            return true;
        }

        System.out.println(trip.getSeatsAvailable());

        return false;
    }

    @Override
    public boolean leaveTrip(Long tripId, String userId) {

        Trip trip = tripRepository.findById(tripId).orElseThrow(
                () -> new CustomException("No existe el viaje")
        );

        if(trip.getDepartureTime().compareTo(Time.valueOf(LocalTime.now())) > 0 && userTripRepository.existsByTripIdAndUserId(tripId, Long.valueOf(userId))) {
            trip.setSeatsAvailable(trip.getSeatsAvailable() + 1);
            tripRepository.save(trip);
            userTripRepository.deleteByTripIdAndUserId(tripId, Long.valueOf(userId));
            chatService.removeUserFromChat(trip.getChatId(), Long.valueOf(userId));
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteTrip(Long tripId, String driverId) {

        Integer deleted = tripRepository.deleteByIdAndDriverId(tripId, Long.valueOf(driverId));

        return deleted == 1;
    }

    @Override
    public boolean finishTripDriver(Long tripId, String driverId) {
        Trip trip = tripRepository.findById(tripId).orElse(null);
        if (trip == null) return false;
        if(trip.getDriverId() != Long.valueOf(driverId)) {
            return false;
        }
        if(trip.getStatus().equals("inactive")) {
            return false;
        }
        trip.setStatus("inactive");
        tripRepository.save(trip);
        trip.getChat().setChatStatus("closed");
        chatRepository.save(trip.getChat());
        return true;
    }

    @Override
    public boolean finishTripUser(Long tripId, String userId) {
        UserTrip userTrip = userTripRepository.findByTripIdAndUserId(tripId, Long.valueOf(userId));
        if (userTrip == null) return false;
        if(userTrip.getStatus().equals("inactive")) {
            return false;
        }
        userTrip.setStatus("inactive");
        userTripRepository.save(userTrip);
        chatService.removeUserFromChat(userTrip.getTrip().getChatId(), Long.valueOf(userId));
        return true;
    }

    @Override
    public boolean finishTrip(Long tripId, String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElse(null);
        if (user == null) return false;
        if(user.getRole().toString().equals("driver")) {
            return finishTripDriver(tripId, userId);
        }
        if(user.getRole().toString().equals("passenger")) {
            return finishTripUser(tripId, userId);
        }
        return false;
    }

    @Autowired
    public void setTripRepository(ITripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserTripRepository(IUserTripRepository userTripRepository) {
        this.userTripRepository = userTripRepository;
    }

    @Autowired
    public void setChatService(@Qualifier("chatServiceImpl") IChatService chatService) {
        this.chatService = chatService;
    }

    @Autowired
    public void setChatRepository(IChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }
}
