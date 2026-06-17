package com.security.controller;

import com.security.model.User;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class GlobalSecurityControllerAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute("currentUser")
    public User addCurrentUserToModel(Principal principal) {
        if (principal != null) {
            Optional<User> userOpt = userService.getUserByUsername(principal.getName());
            return userOpt.orElse(null);
        }
        return null;
    }
}
