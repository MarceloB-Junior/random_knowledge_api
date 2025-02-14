package com.api.random_knowledge.dtos.responses;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserResponse(@NotBlank String name, @Email @NotBlank String email) {
}
