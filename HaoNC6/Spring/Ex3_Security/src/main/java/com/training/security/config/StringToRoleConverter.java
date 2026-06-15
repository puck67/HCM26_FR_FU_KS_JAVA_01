package com.training.security.config;

import com.training.security.entity.Role;
import com.training.security.repository.RoleRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    private final RoleRepository roleRepository;

    public StringToRoleConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        try {
            Long id = Long.parseLong(source);
            return roleRepository.findById(id).orElse(null);
        } catch (NumberFormatException e) {
            // Fallback: try to find by roleName
            return roleRepository.findByRoleName(source).orElse(null);
        }
    }
}
