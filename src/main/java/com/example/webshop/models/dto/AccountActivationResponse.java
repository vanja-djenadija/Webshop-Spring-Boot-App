package com.example.webshop.models.dto;

import lombok.Data;

@Data
public class AccountActivationResponse implements ILoginResponse {
    private String username;
}