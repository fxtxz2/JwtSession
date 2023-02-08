package com.example.demo.controller;

import com.example.demo.comm.Result;
import com.example.demo.model.Role;
import com.example.demo.service.RoleService;
import com.example.demo.vo.RoleReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@Slf4j
@RestController
@RequestScope
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;


    @PostMapping("/add")
    public ResponseEntity<Result> addRole(@RequestBody RoleReq req){
        Role role = roleService.addRole(req);
        Result result = Result.builder()
                .data(role)
                .build();
        return ResponseEntity.ok().body(result);
    }
}
