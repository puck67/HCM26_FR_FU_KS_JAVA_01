package com.example.ex5.controller;

import com.example.ex5.dto.UserRequestDTO;
import com.example.ex5.dto.UserResponseDTO;
import com.example.ex5.entity.Role;
import com.example.ex5.entity.User;
import com.example.ex5.repository.RoleRepository;
import com.example.ex5.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management API", description = "Các API quản lý người dùng (Chỉ dành cho ADMIN)")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả người dùng")
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @PostMapping
    @Operation(summary = "Tạo mới người dùng")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            return ResponseEntity.badRequest().body("{\"message\": \"Error: Username is already taken!\"}");
        }

        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            return ResponseEntity.badRequest().body("{\"message\": \"Error: Email is already in use!\"}");
        }

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setFullName(userRequestDTO.getFullName());
        user.setEmail(userRequestDTO.getEmail());

        Set<String> strRoles = userRequestDTO.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName("STUDENT")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role mappedRole = roleRepository.findByRoleName(role)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(mappedRole);
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("{\"message\": \"User registered successfully!\"}");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin người dùng theo ID")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(userRequestDTO.getFullName());
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        Set<String> strRoles = userRequestDTO.getRoles();
        if (strRoles != null) {
            Set<Role> roles = new HashSet<>();
            strRoles.forEach(role -> {
                Role mappedRole = roleRepository.findByRoleName(role)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(mappedRole);
            });
            user.setRoles(roles);
        }

        userRepository.save(user);
        return ResponseEntity.ok("{\"message\": \"User updated successfully!\"}");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá người dùng theo ID")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("{\"message\": \"User deleted successfully!\"}");
    }

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        
        Set<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
        dto.setRoles(roles);
        
        return dto;
    }
}
