package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    /**
     * 角色id
     */
    private Long id;

    private String name;

    /**
     * 角色编码 唯一
     */
    private String code;
}
