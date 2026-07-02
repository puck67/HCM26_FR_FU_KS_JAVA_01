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

        // Map Roles using Streams
        Set<Role> roles = requestDTO.getRoles() == null ? new HashSet<>() :
                requestDTO.getRoles().stream()
                        .map(roleName -> roleRepository.findByRoleName(roleName.toUpperCase())
                                .orElseThrow(() -> new ResourceNotFoundException("Role " + roleName + " not found")))
                        .collect(Collectors.toSet());

        User user = new User()
                .setUsername(requestDTO.getUsername())
                .setPassword(passwordEncoder.encode(requestDTO.getPassword()))
                .setFullName(requestDTO.getFullName())
                .setEmail(requestDTO.getEmail())
                .setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : "ACTIVE")
                .setRoles(roles);

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

        user.setUsername(requestDTO.getUsername())
                .setFullName(requestDTO.getFullName())
                .setEmail(requestDTO.getEmail())
                .setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : user.getStatus());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        // Map Roles using Streams
        if (requestDTO.getRoles() != null) {
            Set<Role> roles = requestDTO.getRoles().stream()
                    .map(roleName -> roleRepository.findByRoleName(roleName.toUpperCase())
                            .orElseThrow(() -> new ResourceNotFoundException("Role " + roleName + " not found")))
                    .collect(Collectors.toSet());
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
        return new UserResponseDTO()
                .setId(user.getUserId())
                .setUsername(user.getUsername())
                .setFullName(user.getFullName())
                .setEmail(user.getEmail())
                .setStatus(user.getStatus())
                .setRoles(user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toSet()));
    }
}
