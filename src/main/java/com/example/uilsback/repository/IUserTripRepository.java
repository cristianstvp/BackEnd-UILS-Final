package com.example.uilsback.repository;

import com.example.uilsback.model.UserTrip;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserTripRepository extends JpaRepository<UserTrip, Long> {

    @Transactional
    public Integer deleteByTripIdAndUserId(Long tripId, Long userId);

    @Query("SELECT v FROM UserTrip v WHERE v.userId = :userId AND v.status = :status")
    Optional<List<UserTrip>> findAllByUserIdAndStatus(Long userId, String status);

    public boolean existsByTripIdAndUserId(Long tripId, Long userId);

    public UserTrip findByTripIdAndUserId(Long tripId, Long userId);

}
