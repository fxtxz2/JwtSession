package com.example.demo.controller;

import com.example.demo.comm.Result;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserReq;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Result> register(@Valid @RequestBody UserReq req){
        return userService.register(req);
    }


    @GetMapping("/greetings")
    public String greetings(Authentication authentication) {
        String email = userService.getEmail(authentication);
        return "Hello World " + email;
    }
}
