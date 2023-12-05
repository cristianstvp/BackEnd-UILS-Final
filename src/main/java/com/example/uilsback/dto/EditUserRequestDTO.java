package com.example.uilsback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditUserRequestDTO {

    Long id;

    String username;

    String fullName;

    String phoneNumber;

    String email;

    String password;

    String role;

}
