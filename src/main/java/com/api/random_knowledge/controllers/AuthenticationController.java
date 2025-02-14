package com.api.random_knowledge.controllers;

import com.api.random_knowledge.dtos.LoginDto;
import com.api.random_knowledge.dtos.RefreshTokenDto;
import com.api.random_knowledge.dtos.responses.TokenResponse;
import com.api.random_knowledge.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    @Operation(
            summary = "Authenticate user",
            description = "Login to obtain JWT tokens for accessing protected endpoints"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User authenticated successfully"
    )
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authUser(@RequestBody @Valid LoginDto loginDto){
        log.info("Received login request for user: {}", loginDto.email());
        var authUserToken = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());
        authenticationManager.authenticate(authUserToken);
        return ResponseEntity.ok(authenticationService.obtainJwtToken(loginDto));
    }

    @Operation(
            summary="Refresh JWT Token",
            description="Obtain a new JWT token using the refresh token"
    )
    @ApiResponse(
            responseCode="200",
            description="New JWT token obtained successfully"
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> authRefreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto){
        log.info("Received request to refresh token for token: {}",
                refreshTokenDto.refreshToken());
        return ResponseEntity.ok(authenticationService.obtainRefreshToken(refreshTokenDto.refreshToken()));
    }

}