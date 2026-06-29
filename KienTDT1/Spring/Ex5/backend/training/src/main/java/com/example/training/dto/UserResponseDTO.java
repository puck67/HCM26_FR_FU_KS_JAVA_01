package com.example.training.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Set<String> roles;

    // Helper getters for frontend compatibility
    public String getName() {
        return fullName;
    }

    public String getRole() {
        if (roles != null && !roles.isEmpty()) {
            return roles.iterator().next();
        }
        return "";
    }
}
