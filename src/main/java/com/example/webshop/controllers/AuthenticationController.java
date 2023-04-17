package com.example.webshop.controllers;

import com.example.webshop.models.dto.ILoginResponse;
import com.example.webshop.models.dto.RefreshTokenResponse;
import com.example.webshop.models.dto.User;
import com.example.webshop.models.requests.AccountActivationRequest;
import com.example.webshop.models.requests.LoginRequest;
import com.example.webshop.models.requests.RefreshTokenRequest;
import com.example.webshop.models.requests.RegistrationRequest;
import com.example.webshop.services.AuthenticationService;
import com.example.webshop.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserService userService;


    public AuthenticationController(AuthenticationService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ILoginResponse login(@RequestBody @Valid LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegistrationRequest request) {
        userService.register(request);
    }

    @PostMapping("/activate")
    public User activateAccount(@RequestBody @Valid AccountActivationRequest request) {
        if (service.activateAccount(request)) {
            return userService.activateAccount(request.getUsername());
        }
        return null;
    }

    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return service.refreshToken(request);
    }
}