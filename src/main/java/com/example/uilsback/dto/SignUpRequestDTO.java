package com.example.uilsback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {

        String username;

        String fullName;

        String phoneNumber;

        String email;

        String password;

        String role;

}
