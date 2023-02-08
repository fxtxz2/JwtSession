package com.example.demo.service;


import com.example.demo.model.Role;
import com.example.demo.vo.RoleReq;

import java.util.List;

public interface RoleService {
    List<Role> findByUserId(Long userId);

    Role addRole(RoleReq req);
}
