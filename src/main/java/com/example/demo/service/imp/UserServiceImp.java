package com.example.demo.service.imp;

import com.example.demo.comm.Result;
import com.example.demo.comm.RoleEnum;
import com.example.demo.comm.UserDetailsImpl;
import com.example.demo.exception.DemoException;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserRoleMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserReq;
import com.example.demo.vo.UserRes;
import jakarta.annotation.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImp implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private PasswordEncoder encoder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<Result> register(UserReq req) {
        // 判断用户是否已经存在
        User oldUser = userMapper.findUserByUsername(req.getUsername());
        if (oldUser != null) {
            throw new DemoException(req.getUsername() + "用户名已经存在");
        }
        // 密码加密
        req.setPassword(encoder.encode(req.getPassword()));
        User user = modelMapper.map(req, User.class);
        // 注册用户
        userMapper.insertUser(user);
        // 查询普通用户角色
        Role role = roleMapper.findRoleByCode(RoleEnum.USER.getCode());
        if (role == null) {
            throw new DemoException(RoleEnum.USER.getCode() + "角色不存在");
        }
        // 查询是否存在普通用户关系
        UserRole userRole = userRoleMapper.findUserRoleByUserIdAndRoleId(user.getId(), role.getId());
        // 添加普通用户角色关系
        if (userRole == null) {
            userRoleMapper.insertUserRole(UserRole.builder()
                            .userId(user.getId())
                            .roleId(role.getId())
                    .build());
        }
        // 返回新用户
        Result result = Result.builder()
                .data(modelMapper.map(user, UserRes.class))
                .build();

        return ResponseEntity.ok().body(result);

    }

    @Override
    public String getEmail(Authentication authentication) {
        UserDetailsImpl userDetailsImpl= (UserDetailsImpl) authentication.getPrincipal();
        return userDetailsImpl.getEmail();
    }
}
