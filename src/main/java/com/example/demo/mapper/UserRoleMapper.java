package com.example.demo.mapper;

import com.example.demo.model.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper {
    int insertUserRole(@Param("userRole") UserRole userRole);
    UserRole findUserRoleByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
