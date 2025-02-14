package com.api.random_knowledge.dtos.responses;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExceptionsResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {
}
