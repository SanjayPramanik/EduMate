package com.edumate.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public static class LoginRequest {
        @Email
        @NotBlank
        public String email;

        @NotBlank
        @Size(min = 6)
        public String password;
    }

    public static class RegisterRequest {
        @NotBlank
        public String firstName;

        public String lastName;

        @Email
        @NotBlank
        public String email;

        @NotBlank
        @Size(min = 6)
        public String password;
    }

    public static class AuthResponse {
        public String token;
        public UserPayload user;
    }

    public static class UserPayload {
        public Integer id;
        public String firstName;
        public String lastName;
        public String email;
    }
}


