package com.example.demo.comm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN("ADMIN", "超级管理员"),
    USER("USER", "普通用户");

    private final String code;
    private final String name;

    public static RoleEnum getByCode(String code){
        for (RoleEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
