package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRes {
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
    private String email;
    private boolean deleted = false;
}
