package com.example.demo.controller;

import com.example.demo.comm.JwtUtils;
import com.example.demo.comm.Result;
import com.example.demo.comm.UserDetailsImpl;
import com.example.demo.model.CsrfResponse;
import com.example.demo.vo.LoginReq;
import com.example.demo.vo.UserInfoRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private SecurityContextRepository securityContextRepository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Resource
    private JwtUtils jwtUtils;

    @GetMapping("/csrf")
    public CsrfResponse csrf(@RequestAttribute("_csrf") CsrfToken csrf) {
        return CsrfResponse.builder().token(csrf.getToken()).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Result> authenticateUser(@RequestBody LoginReq loginRequest, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);

        log.info("会话id:" + httpSession.getId());

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
