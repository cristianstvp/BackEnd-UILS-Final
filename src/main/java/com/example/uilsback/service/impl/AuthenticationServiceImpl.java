package com.example.uilsback.service.impl;

import com.example.uilsback.dto.JwtAuthenticationResponse;
import com.example.uilsback.dto.SignInRequestDTO;
import com.example.uilsback.dto.SignUpRequestDTO;
import com.example.uilsback.exception.CustomException;
import com.example.uilsback.model.Role;
import com.example.uilsback.model.User;
import com.example.uilsback.repository.IUserRepository;
import com.example.uilsback.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {

    private IUserRepository userRepository;

    private IUserService userService;

    private final PasswordEncoder passwordEncoder;

    private JwtServiceImpl jwtService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signup(SignUpRequestDTO request) {
        var user = User
                .builder()
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .build();

        user = userService.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }


    public JwtAuthenticationResponse signin(SignInRequestDTO request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException("Usuario o contraseña inávlido."));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserService(@Qualifier("userServiceImpl") IUserService userService){
        this.userService = userService;
    }

    @Autowired
    public void setJwtService(@Qualifier("jwtServiceImpl") JwtServiceImpl jwtService){
        this.jwtService = jwtService;
    }

}
