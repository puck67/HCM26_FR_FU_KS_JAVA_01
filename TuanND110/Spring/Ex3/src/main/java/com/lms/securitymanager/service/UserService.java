package com.lms.securitymanager.service;

import com.lms.securitymanager.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    User resetPassword(Long id, String newPassword);
}
