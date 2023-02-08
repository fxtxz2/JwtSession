package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private Long id;

    /**
     * 用户id
     * @see com.example.demo.model.User
     */
    private Long userId;

    /**
     * 用户角色id
     * @see com.example.demo.model.Role
     */
    private Long roleId;
}
