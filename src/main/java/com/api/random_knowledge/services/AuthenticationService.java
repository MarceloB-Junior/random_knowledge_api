package com.api.random_knowledge.services;

import com.api.random_knowledge.dtos.LoginDto;
import com.api.random_knowledge.dtos.responses.TokenResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {
    TokenResponse obtainJwtToken(LoginDto loginDto);
    TokenResponse obtainRefreshToken(String refreshToken);
    String validateJwtToken(String jwtToken);
}