package com.training.security.service;

import com.training.security.entity.Role;
import java.util.List;

public interface RoleService {
    List<Role> findAll();
    Role findById(Long id);
    Role save(Role role);
    void deleteById(Long id);
    Role findByName(String name);
}
