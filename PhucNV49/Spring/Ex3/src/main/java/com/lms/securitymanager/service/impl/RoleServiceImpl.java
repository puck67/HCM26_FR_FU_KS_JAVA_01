package com.lms.securitymanager.service.impl;

import com.lms.securitymanager.entity.Role;
import com.lms.securitymanager.repository.RoleRepository;
import com.lms.securitymanager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    
    // Prevent deletion of these system critical roles
    private static final List<String> SYSTEM_ROLES = Arrays.asList("SUPER_ADMIN", "ADMIN", "TEACHER");

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Role saveRole(Role role) {
        // Enforce UPPER_CASE for role names
        role.setRoleName(role.getRoleName().trim().toUpperCase());
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + id));

        if (SYSTEM_ROLES.contains(role.getRoleName())) {
            throw new IllegalStateException("Cannot delete system critical role: " + role.getRoleName());
        }

        roleRepository.delete(role);
    }
}
