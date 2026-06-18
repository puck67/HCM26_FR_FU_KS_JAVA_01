package com.lms.securitymanager.service;

import com.lms.securitymanager.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role saveRole(Role role);
    void deleteRole(Long id);
}
