package com.api.random_knowledge.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(@NotBlank String name) {
}
