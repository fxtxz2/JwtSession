package com.example.demo.mapper;

import com.example.demo.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface RoleMapper {
    int insertRole(@Param("role") Role role);

    Role findRoleByCode(@Param("code") String code);

    /**
     * 根据用户id查角色
     * @param userId 用户id
     * @return 角色
     */
    List<Role> findRoleByUserId(@Param("userId") Long userId);
}
