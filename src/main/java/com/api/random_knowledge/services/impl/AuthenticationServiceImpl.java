package com.api.random_knowledge.services.impl;

import com.api.random_knowledge.dtos.LoginDto;
import com.api.random_knowledge.dtos.responses.TokenResponse;
import com.api.random_knowledge.models.UserModel;
import com.api.random_knowledge.repositories.UserRepository;
import com.api.random_knowledge.services.AuthenticationService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Value("${api.jwt.token.secret}")
    private String jwtSecret;

    @Value("${api.jwt.token.iss}")
    private String iss;

    @Value("${api.jwt.token.expiration}")
    private Integer tokenExpiration;

    @Value("${api.jwt.refresh.token.expiration}")
    private Integer refreshTokenExpiration;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @Override
    public TokenResponse obtainJwtToken(LoginDto loginDto) {
       var userModel = userRepository.findByEmail(loginDto.email())
               .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginDto.email()));

       log.debug("User {} obtained a new access token", userModel.getEmail());
       return TokenResponse.builder()
               .accessToken(generateJwtToken(userModel,tokenExpiration))
               .refreshToken(generateJwtToken(userModel, refreshTokenExpiration))
               .expiresAt(generateExpirationDate(tokenExpiration))
               .build();
    }

    @Override
    public TokenResponse obtainRefreshToken(String refreshToken) {
        String userEmail = validateJwtToken(refreshToken);
        var userModel = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        var authentication = new UsernamePasswordAuthenticationToken(userModel,
                null,userModel.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.debug("User {} obtained a new access token with refresh token", userModel.getEmail());
        return TokenResponse.builder()
                .accessToken(generateJwtToken(userModel,tokenExpiration))
                .refreshToken(generateJwtToken(userModel, refreshTokenExpiration))
                .expiresAt(generateExpirationDate(tokenExpiration))
                .build();
    }

    private String generateJwtToken(UserModel userModel, Integer expirationHour){
        try {
            return JWT.create()
                    .withIssuer(iss)
                    .withSubject(userModel.getEmail())
                    .withExpiresAt(generateExpirationDate(expirationHour))
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch(JWTCreationException e) {
            log.error("Error in generating the JWT token for user {}: {}", userModel.getEmail(),
                    e.getMessage());
            throw new JWTCreationException("Error in generating the JWT token.", e);
        }
    }

    private Instant generateExpirationDate(Integer expirationHour){
        return LocalDateTime.now().plusHours(expirationHour)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    @Override
    public String validateJwtToken(String jwtToken) {
        try {
            return JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withIssuer(iss)
                    .build()
                    .verify(jwtToken)
                    .getSubject();

        } catch(JWTVerificationException e) {
            log.error("Error in validating the JWT token: {}", e.getMessage());
            throw new JWTVerificationException("Invalid JWT token.");
        }
    }
}