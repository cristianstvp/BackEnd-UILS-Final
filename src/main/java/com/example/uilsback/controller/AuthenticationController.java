package com.example.uilsback.controller;

import com.example.uilsback.dto.JwtAuthenticationResponse;
import com.example.uilsback.dto.SignInRequestDTO;

import com.example.uilsback.dto.SignUpRequestDTO;
import com.example.uilsback.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/signup")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequestDTO request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequestDTO request) {
        return authenticationService.signin(request);
    }

}
