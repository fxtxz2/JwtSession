package com.example.demo.controller;

import com.example.demo.comm.JwtUtils;
import com.example.demo.comm.Result;
import com.example.demo.comm.UserDetailsImpl;
import com.example.demo.vo.LoginReq;
import com.example.demo.vo.UserInfoRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtils jwtUtils;
    @PostMapping("/login")
    public ResponseEntity<Result> authenticateUser(@RequestBody LoginReq loginRequest, HttpSession httpSession) throws JsonProcessingException {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        jwtUtils.generateJwtCookie(httpSession, userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        UserInfoRes userInfoResponse = UserInfoRes.builder()
                .username(userDetails.getUsername())
                .roles(roles)
                .build();

        return ResponseEntity.ok()
                .body(Result.builder().data(userInfoResponse).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Result> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Result.builder().build());
    }
}
