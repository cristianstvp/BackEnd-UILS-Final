package com.example.uilsback.repository;

import com.example.uilsback.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.username = ?1, u.fullName = ?2, u.phoneNumber = ?3, u.email = ?4, u.updatedAt = ?5, u.password = ?6, u.role = ?7 WHERE u.id = ?8")
    void editUser(String username, String fullName, String phoneNumber, String email, LocalDateTime updatedAt, String password, String role, Long id);

}
