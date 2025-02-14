package com.api.random_knowledge.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    ADMIN("admin"),
    USER("user");

    final String role;
}
