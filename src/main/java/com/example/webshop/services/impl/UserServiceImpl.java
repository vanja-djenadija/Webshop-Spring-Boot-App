package com.example.webshop.services.impl;

import com.example.webshop.exceptions.ConflictException;
import com.example.webshop.exceptions.InvalidPasswordException;
import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.exceptions.UnauthorizedException;
import com.example.webshop.models.dto.JWTUser;
import com.example.webshop.models.dto.Product;
import com.example.webshop.models.dto.User;
import com.example.webshop.models.entities.UserEntity;
import com.example.webshop.models.enums.ProductStatus;
import com.example.webshop.models.enums.UserStatus;
import com.example.webshop.models.requests.RegistrationRequest;
import com.example.webshop.models.requests.UserUpdateRequest;
import com.example.webshop.repositories.ProductEntityRepository;
import com.example.webshop.repositories.UserEntityRepository;
import com.example.webshop.services.AuthenticationService;
import com.example.webshop.services.UserService;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final String LOG_MESSAGE_FORMAT = "%s %d";
    private final UserEntityRepository userRepository;
    private final ProductEntityRepository productRepository;
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationService authenticationService;

    public UserServiceImpl(UserEntityRepository userRepository, ProductEntityRepository productRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @Override
    public User findById(Integer id) {
        return modelMapper.map(userRepository.findById(id).orElseThrow(NotFoundException::new), User.class);
    }

    @Override
    public User update(String username, UserUpdateRequest request, Authentication authentication) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        JWTUser user = (JWTUser) authentication.getPrincipal();
        if (!userEntity.getId().equals(user.getId()))
            throw new UnauthorizedException();
        if (passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userEntity.setFirstName(request.getFirstName());
            userEntity.setLastName(request.getLastName());
            userEntity.setCity(request.getCity());
            userEntity.setEmail(request.getEmail());
            userEntity.setAvatarUrl(request.getAvatarUrl());
            userEntity.setPhoneNumber(request.getPhoneNumber());
            Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "UPDATE USER", user.getId()));
            return modelMapper.map(userRepository.saveAndFlush(userEntity), User.class);
        } else
            throw new InvalidPasswordException();
    }

    @Override
    public void register(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new ConflictException();
        UserEntity userEntity = modelMapper.map(request, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setStatus(UserStatus.REQUESTED);
        userEntity = userRepository.saveAndFlush(userEntity);
        Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "REGISTER USER", userEntity.getId()));
        authenticationService.sendPin(userEntity.getUsername(), userEntity.getEmail());
    }

    @Override
    public User activateAccount(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(UnauthorizedException::new);
        userEntity.setStatus(UserStatus.ACTIVE);
        Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "LOGIN ACTIVATE USER", userEntity.getId()));
        return modelMapper.map(userRepository.saveAndFlush(userEntity), User.class);
    }

    @Override
    public Page<Product> findAllActiveProducts(Pageable page, String username, Authentication authentication) {
        JWTUser user = (JWTUser) authentication.getPrincipal();
        if (!user.getUsername().equals(username))
            throw new UnauthorizedException();
        Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "FIND ACTIVE PRODUCTS by user", user.getId()));
        return productRepository.findAllBySeller_IdAndStatus(page, user.getId(), ProductStatus.ACTIVE).map(p -> modelMapper.map(p, Product.class));
    }

    @Override
    public Page<Product> findAllSoldProducts(Pageable page, String username, Authentication authentication) {
        JWTUser user = (JWTUser) authentication.getPrincipal();
        if (!user.getUsername().equals(username))
            throw new UnauthorizedException();
        Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "FIND SOLD PRODUCTS by user", user.getId()));
        return productRepository.findAllBySeller_IdAndStatus(page, user.getId(), ProductStatus.SOLD).map(p -> modelMapper.map(p, Product.class));
    }

    @Override
    public Page<Product> findAllPurchasedProducts(Pageable page, String username, Authentication authentication) {
        JWTUser user = (JWTUser) authentication.getPrincipal();
        if (!user.getUsername().equals(username))
            throw new UnauthorizedException();
        Logger.getLogger(getClass()).info(String.format(LOG_MESSAGE_FORMAT, "FIND PURCHASED PRODUCTS by user", user.getId()));
        return productRepository.findAllByCustomer_Id(page, user.getId()).map(p -> modelMapper.map(p, Product.class));
    }
}