package com.example.demo.service.imp;

import com.example.demo.exception.DemoException;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.model.Role;
import com.example.demo.service.RoleService;
import com.example.demo.vo.RoleReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class RoleServiceImp implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private ModelMapper modelMapper;

    @Override
    public List<Role> findByUserId(Long userId) {
        List<Role> roles = roleMapper.findRoleByUserId(userId);
        if (CollectionUtils.isEmpty(roles)){
            return Collections.emptyList();
        }
        return roles;
    }

    @Override
    public Role addRole(RoleReq req) {
        Role oldRole = roleMapper.findRoleByCode(req.getCode());
        if (oldRole != null){
            throw new DemoException("角色已经存在");
        }
        Role role = modelMapper.map(req, Role.class);
        roleMapper.insertRole(role);
        return role;
    }
}
