package com.example.webshop.models.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponse extends User implements ILoginResponse {
    private String token;

    private String refreshToken;
}