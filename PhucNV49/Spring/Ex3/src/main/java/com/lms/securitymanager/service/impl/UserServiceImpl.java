package com.lms.securitymanager.service.impl;

import com.lms.securitymanager.entity.User;
import com.lms.securitymanager.repository.UserRepository;
import com.lms.securitymanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        // Encode password before saving new user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User existing = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getUserId()));
        
        existing.setFullName(user.getFullName());
        existing.setStatus(user.getStatus());
        existing.setRoles(user.getRoles());
        
        // Don't modify the password here
        return userRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        // Safety check: Cannot delete superadmin account
        if ("superadmin".equalsIgnoreCase(user.getUsername())) {
            throw new IllegalStateException("Cannot delete the system Super Admin account.");
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}
