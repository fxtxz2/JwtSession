package com.example.demo.service;

import com.example.demo.comm.Result;
import com.example.demo.vo.UserReq;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface UserService {

    /**
     * 注册新用户
     * @param req 用户
     * @return 新用户
     */
    ResponseEntity<Result> register(UserReq req);


    String getEmail(Authentication authentication);
}
