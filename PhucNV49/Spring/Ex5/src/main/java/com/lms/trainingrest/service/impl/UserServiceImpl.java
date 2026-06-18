package com.lms.trainingrest.service.impl;

import com.lms.trainingrest.dto.UserRequestDTO;
import com.lms.trainingrest.dto.UserResponseDTO;
import com.lms.trainingrest.entity.Role;
import com.lms.trainingrest.entity.User;
import com.lms.trainingrest.exception.ResourceNotFoundException;
import com.lms.trainingrest.repository.RefreshTokenRepository;
import com.lms.trainingrest.repository.RoleRepository;
import com.lms.trainingrest.repository.UserRepository;
import com.lms.trainingrest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
                           RoleRepository roleRepository, 
                           RefreshTokenRepository refreshTokenRepository, 
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return mapToDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setFullName(requestDTO.getFullName());
        user.setEmail(requestDTO.getEmail());
        user.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : "ACTIVE");

        // Map Roles
        Set<Role> roles = new HashSet<>();
        if (requestDTO.getRoles() != null) {
            for (String roleName : requestDTO.getRoles()) {
                Role role = roleRepository.findByRoleName(roleName.toUpperCase())
                        .orElseThrow(() -> new ResourceNotFoundException("Role " + roleName + " not found"));
                roles.add(role);
            }
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check duplicates if username/email are changing
        if (!user.getUsername().equals(requestDTO.getUsername()) && userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (!user.getEmail().equals(requestDTO.getEmail()) && userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        user.setUsername(requestDTO.getUsername());
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }
        user.setFullName(requestDTO.getFullName());
        user.setEmail(requestDTO.getEmail());
        user.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : user.getStatus());

        // Map Roles
        Set<Role> roles = new HashSet<>();
        if (requestDTO.getRoles() != null) {
            for (String roleName : requestDTO.getRoles()) {
                Role role = roleRepository.findByRoleName(roleName.toUpperCase())
                        .orElseThrow(() -> new ResourceNotFoundException("Role " + roleName + " not found"));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Delete refresh token first
        refreshTokenRepository.deleteByUser(user);
        
        userRepository.delete(user);
    }

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet()));
        return dto;
    }
}
