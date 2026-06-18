package com.example.ex3.service;

import com.example.ex3.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    User saveUser(User user, List<Long> roleIds);
    User updateUser(Long id, User userDetails, List<Long> roleIds);
    void deleteById(Long id);
    void resetPassword(Long id, String newPassword);
}
