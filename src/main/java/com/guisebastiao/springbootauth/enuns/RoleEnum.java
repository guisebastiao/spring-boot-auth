package com.guisebastiao.springbootauth.enuns;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN("admin"),
    USER("user");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public static RoleEnum getRole(String role) {
        return switch (role) {
            case "admin" -> RoleEnum.ADMIN;
            default -> RoleEnum.USER;
        };
    }
}
