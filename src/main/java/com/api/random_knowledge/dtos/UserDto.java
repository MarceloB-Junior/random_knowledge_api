package com.api.random_knowledge.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank
        String name,
        @NotBlank
        String password,
        @Email @NotBlank
        String email
) {
}