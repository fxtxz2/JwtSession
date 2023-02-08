package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名 唯一的
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;
    private String password;
    private String email;
    private boolean deleted = false;
}
