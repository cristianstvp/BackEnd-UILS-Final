package com.example.uilsback.controller;

import com.example.uilsback.dto.EditUserRequestDTO;
import com.example.uilsback.dto.SignUpRequestDTO;
import com.example.uilsback.dto.UserDataDTO;
import com.example.uilsback.exception.CustomException;
import com.example.uilsback.model.User;
import com.example.uilsback.service.impl.AuthenticationServiceImpl;
import com.example.uilsback.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationServiceImpl authenticationService;

    private IUserService userService;

    @GetMapping("/user/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.findAll();
        if(users.isEmpty()){
            throw new CustomException("No se encontraron usuarios");
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @PostMapping("/user/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void create(@RequestBody SignUpRequestDTO request) {
        authenticationService.signup(request);
    }

    @PutMapping("/user")
    public ResponseEntity<?> edit(
            @RequestBody EditUserRequestDTO request,
            @RequestHeader("idUsuario") String userId
    ) {
        if(userService.edit(request, userId)){
            return ResponseEntity.ok(true);
        } else {
            throw new CustomException("No se pudo editar el usuario");
        }
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/public/user/checkUser")
    public ResponseEntity<?> checkUser(
            @RequestParam String username
    ) {
        if(userService.checkUser(username)){
            return ResponseEntity.ok(true);
        } else {
            throw new CustomException("El usuario ya existe");
        }
    }

    @GetMapping("/user/data")
    @PreAuthorize("hasAuthority('passenger') || hasAuthority('driver')")
    public ResponseEntity<UserDataDTO> getUserData() {
        return ResponseEntity.ok(userService.getUserData());
    }

    @Autowired
    public void setUserService(@Qualifier("userServiceImpl") IUserService userService){
        this.userService = userService;
    }

}
