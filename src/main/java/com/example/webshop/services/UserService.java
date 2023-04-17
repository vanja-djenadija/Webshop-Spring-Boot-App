package com.example.webshop.services;

import com.example.webshop.models.dto.Product;
import com.example.webshop.models.dto.User;
import com.example.webshop.models.requests.RegistrationRequest;
import com.example.webshop.models.requests.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface UserService {
    User findById(Integer id);

    User update(String username, UserUpdateRequest request, Authentication authentication);

    void register(RegistrationRequest request);

    User activateAccount(String username);

    Page<Product> findAllActiveProducts(Pageable page, String username, Authentication authentication);

    Page<Product> findAllSoldProducts(Pageable page, String username, Authentication authentication);

    Page<Product> findAllPurchasedProducts(Pageable page, String username, Authentication authentication);
}