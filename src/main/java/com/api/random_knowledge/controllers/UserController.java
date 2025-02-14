package com.api.random_knowledge.controllers;

import com.api.random_knowledge.dtos.UserDto;
import com.api.random_knowledge.dtos.responses.ExceptionsResponse;
import com.api.random_knowledge.dtos.responses.UserResponse;
import com.api.random_knowledge.exceptions.UserAlreadyExistsException;
import com.api.random_knowledge.models.UserModel;
import com.api.random_knowledge.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Create a user account for the Random Knowledge API"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "User created successfully"),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Email already in use",
                            content = @Content(schema = @Schema(implementation = ExceptionsResponse.class))
                    )
            }
    )
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserDto userDto){
        log.info("Received request to sign up user with email: {}", userDto.email());
        if(userService.existsByEmail(userDto.email())){
            log.warn("Attempt to sign up failed: Email already in use - {}", userDto.email());
            throw new UserAlreadyExistsException("This email is already in use!");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        return new ResponseEntity<>(userService.save(userModel), HttpStatus.CREATED);
    }
}