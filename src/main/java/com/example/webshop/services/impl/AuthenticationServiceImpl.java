package com.example.webshop.services.impl;

import com.example.webshop.exceptions.NotFoundException;
import com.example.webshop.exceptions.UnauthorizedException;
import com.example.webshop.models.dto.ILoginResponse;
import com.example.webshop.models.dto.LoginResponse;
import com.example.webshop.models.dto.RefreshTokenResponse;
import com.example.webshop.models.entities.UserEntity;
import com.example.webshop.models.enums.UserStatus;
import com.example.webshop.models.requests.AccountActivationRequest;
import com.example.webshop.models.requests.LoginRequest;
import com.example.webshop.models.requests.RefreshTokenRequest;
import com.example.webshop.repositories.UserEntityRepository;
import com.example.webshop.services.AuthenticationService;
import com.example.webshop.services.MailService;
import com.example.webshop.util.LoggingUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Value("3600000")
    private String tokenExpirationTime; // TODO: set to 15 min

    @Value("3600000") // 1 hour
    private String refreshTokenExpirationTime;

    @Value("${authorization.token.secret}")
    private String tokenSecret;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final UserEntityRepository userEntityRepository;
    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;
    private final HashMap<String, String> pins = new HashMap<>();

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     ModelMapper modelMapper, UserEntityRepository userEntityRepository, MailService mailService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.userEntityRepository = userEntityRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ILoginResponse login(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            UserEntity userEntity = userEntityRepository.findByUsername(loginRequest.getUsername()).orElseThrow(NotFoundException::new);
            if (userEntity.getStatus().equals(UserStatus.ACTIVE)) {
                LoginResponse response = modelMapper.map(userEntity, LoginResponse.class);
                response.setToken(generateJWT(userEntity));
                response.setRefreshToken(generateRefresh(userEntity));
                Logger.getLogger(getClass()).info("LOGIN USER" + " " + userEntity.getId());
                return response;
            } else {
                sendPin(userEntity.getUsername(), userEntity.getEmail());
                Logger.getLogger(getClass()).info("LOGIN USER SEND PIN" + " " + userEntity.getId());
                return null;
            }
        } catch (Exception e) {
            LoggingUtil.logException(e, getClass());
            throw new UnauthorizedException();
        }
    }

    @Override
    public void sendPin(String username, String email) {
        SecureRandom random = new SecureRandom();
        String pin = String.valueOf(random.nextInt(9000) + 1000);
        while (pins.containsValue(pin)) {
            pin = String.valueOf(random.nextInt(9000) + 1000);
        }
        pins.put(username, pin);
        mailService.sendMail(email, pin);
    }

    /**
     * Check if provided PIN for account activation is the one sent by email.
     */
    @Override
    public boolean activateAccount(AccountActivationRequest request) {
        return pins.containsKey(request.getUsername()) && pins.get(request.getUsername()).equals(request.getPin());
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        Claims claims = parseJWT(refreshToken);
        UserEntity user = userEntityRepository.findByUsername(claims.getSubject()).orElseThrow(NotFoundException::new);
        String token = generateJWT(user);
        Logger.getLogger(getClass()).info("REFRESH TOKEN by user " + " " + user.getId());
        return new RefreshTokenResponse(token);
    }

    private String generateJWT(UserEntity user) {
        return Jwts.builder()
                .setId(user.getId().toString())
                .setSubject(user.getUsername())
                .claim("role", user.getStatus().name())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenExpirationTime)))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    private String generateRefresh(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime)))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    private Claims parseJWT(String token) {
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token).getBody();
    }
}