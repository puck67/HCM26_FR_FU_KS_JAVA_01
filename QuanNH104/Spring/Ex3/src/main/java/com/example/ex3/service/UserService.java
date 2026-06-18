package com.example.ex3.service;

import com.example.ex3.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User saveUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    void resetPassword(Long id, String newPassword);
    boolean existsByUsername(String username);
}
