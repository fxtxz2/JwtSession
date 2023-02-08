package com.example.demo.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReq {
    /**
     * 用户名 唯一的
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 昵称
     */
    @NotEmpty(message = "昵称不能为空")
    private String nickname;

    @NotEmpty(message = "密码不能为空")
    @Size(min = 8, message = "至少为8个字符")
    @Size(max = 20, message = "最多只能是20个字符")
    private String password;
    @Email
    private String email;
}
