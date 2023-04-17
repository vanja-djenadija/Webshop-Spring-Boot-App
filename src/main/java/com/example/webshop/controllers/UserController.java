package com.example.webshop.controllers;

import com.example.webshop.models.dto.Product;
import com.example.webshop.models.dto.User;
import com.example.webshop.models.requests.UserUpdateRequest;
import com.example.webshop.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PutMapping("/{username}")
    public User update(@PathVariable String username, @RequestBody @Valid UserUpdateRequest request, Authentication authentication) {
        return service.update(username, request, authentication);
    }

    @GetMapping("/{username}/active")
    public Page<Product> findAllActiveProducts(Pageable page, @PathVariable String username, Authentication authentication) {
        return service.findAllActiveProducts(page, username, authentication);
    }

    @GetMapping("/{username}/sold")
    public Page<Product> findAllInactiveProducts(Pageable page, @PathVariable String username, Authentication authentication) {
        return service.findAllSoldProducts(page, username, authentication);
    }

    @GetMapping("/{username}/purchased")
    public Page<Product> findAllPurchasedProducts(Pageable page, @PathVariable String username, Authentication authentication) {
        return service.findAllPurchasedProducts(page, username, authentication);
    }

}