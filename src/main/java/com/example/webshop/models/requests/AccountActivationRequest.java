package com.example.webshop.models.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountActivationRequest {
    @NotBlank
    private String pin;
    @NotBlank
    private String username;
}