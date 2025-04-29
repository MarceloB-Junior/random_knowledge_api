package com.api.random_knowledge.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TokenResponse(
        @NotBlank
        @JsonProperty(value = "access_token", index = 1)
        String accessToken,
        @NotBlank
        @JsonProperty(value = "refresh_token", index = 2)
        String refreshToken,
        @NotNull
        @JsonProperty(value = "expires_at", index = 3)
        Instant expiresAt
) {
}