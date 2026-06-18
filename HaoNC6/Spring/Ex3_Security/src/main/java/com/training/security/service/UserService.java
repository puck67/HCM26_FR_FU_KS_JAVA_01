package com.training.security.service;

import com.training.security.entity.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User findByUsername(String username);
    User register(User user);
    User update(User user);
    void resetPassword(Long userId, String newPassword);
    void deleteById(Long id);
}
