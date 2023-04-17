package com.example.webshop.services;

import com.example.webshop.models.dto.ILoginResponse;
import com.example.webshop.models.dto.RefreshTokenResponse;
import com.example.webshop.models.requests.AccountActivationRequest;
import com.example.webshop.models.requests.LoginRequest;
import com.example.webshop.models.requests.RefreshTokenRequest;

public interface AuthenticationService {

    ILoginResponse login(LoginRequest loginRequest);

    void sendPin(String username, String email);

    boolean activateAccount(AccountActivationRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
}