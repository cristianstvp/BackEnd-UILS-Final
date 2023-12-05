package com.example.uilsback.service.impl;

import com.example.uilsback.dto.EditUserRequestDTO;
import com.example.uilsback.dto.UserDataDTO;
import com.example.uilsback.exception.CustomException;
import com.example.uilsback.mapper.IVehicleMapper;
import com.example.uilsback.model.Role;
import com.example.uilsback.model.User;
import com.example.uilsback.repository.IUserRepository;
import com.example.uilsback.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private IUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public User loadUserByUsername(String username) {
                return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public List<User> findAll() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public User save(User newUser) {
        if (newUser.getId() == null){
            newUser.setCreatedAt(LocalDateTime.now());
        }else{
            newUser.setUpdatedAt(LocalDateTime.now());
            userRepository.editUser(newUser.getUsername(), newUser.getFullName(), newUser.getPhoneNumber(), newUser.getEmail(), newUser.getUpdatedAt(), newUser.getPassword(), newUser.getRole().toString(), newUser.getId());;
        }
        return userRepository.save(newUser);
    }


    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public UserDataDTO getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUsername(userDetails.getUsername());
        userDataDTO.setRole(userDetails.getAuthorities().toArray()[0].toString());
        userRepository.findByUsername(userDetails.getUsername()).ifPresent(user -> {
            userDataDTO.setFullName(user.getFullName());
            userDataDTO.setId(user.getId());
            userDataDTO.setEmail(user.getEmail());
            userDataDTO.setPhoneNumber(user.getPhoneNumber());
            if (user.getVehicle() != null){
                userDataDTO.setVehicle(IVehicleMapper.INSTANCE.vehicleToVehicleDTO(user.getVehicle()));
            }
        });
        return userDataDTO;
    }

    public boolean edit(EditUserRequestDTO editedUser, String userId) {
        User oldUser = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new CustomException("Usuario no encontrado"));
        if (oldUser != null){
            if(editedUser.getPassword() == null){
                editedUser.setPassword(oldUser.getPassword());
            }else{
                if(editedUser.getPassword().isBlank()){
                    editedUser.setPassword(oldUser.getPassword());
                }
                editedUser.setPassword(passwordEncoder.encode(editedUser.getPassword()));
            }
            User user = oldUser;
            if (editedUser.getFullName() != null) user.setFullName(editedUser.getFullName());
            if (editedUser.getPhoneNumber() != null) user.setPhoneNumber(editedUser.getPhoneNumber());
            if (editedUser.getEmail() != null) user.setEmail(editedUser.getEmail());
            if (editedUser.getUsername() != null) user.setUsername(editedUser.getUsername());
            if (editedUser.getPassword() != null) user.setPassword(editedUser.getPassword());
            if (editedUser.getRole() != null  && user != null){
                if (editedUser.getRole().equals("driver")){
                    user.setRole(Role.driver);
                } else if (editedUser.getRole().equals("passenger")){
                    user.setRole(Role.passenger);
                }else {
                    throw new CustomException("Rol no válido");
                }
            }

            /*User user = User
                    .builder()
                    .id(editedUser.getId())
                    .fullName(editedUser.getFullName())
                    .phoneNumber(editedUser.getPhoneNumber())
                    .email(editedUser.getEmail())
                    .username(editedUser.getUsername())
                    .password(editedUser.getPassword())
                    .role(Role.valueOf(editedUser.getRole()))
                    .build();
             */
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean checkUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
