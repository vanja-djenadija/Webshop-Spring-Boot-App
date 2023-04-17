package com.example.webshop.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String city;
    @NotBlank
    private String avatarUrl;
    @NotBlank
    private String phoneNumber;
}