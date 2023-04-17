package com.example.webshop.services.impl;

import com.example.webshop.exceptions.UnauthorizedException;
import com.example.webshop.models.dto.JWTUser;
import com.example.webshop.repositories.UserEntityRepository;
import com.example.webshop.services.JWTUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JWTUserDetailsServiceImpl implements JWTUserDetailsService {
    private final UserEntityRepository userRepository;
    private final ModelMapper modelMapper;

    public JWTUserDetailsServiceImpl(UserEntityRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return modelMapper.map(userRepository.findByUsername(username).orElseThrow(UnauthorizedException::new), JWTUser.class);
    }
}