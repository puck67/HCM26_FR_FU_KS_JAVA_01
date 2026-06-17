package com.security.controller;

import com.security.entity.Role;
import com.security.entity.User;
import com.security.repository.RoleRepository;
import com.security.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("allUsers", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Clear password on edit form for security, but we will preserve it on save if not changed
            user.setPassword("");
            model.addAttribute("user", user);
            model.addAttribute("allRoles", roleRepository.findAll());
            return "user-form";
        }
        return "redirect:/users";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("user") User user,
                       BindingResult result,
                       @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "user-form";
        }

        // Check unique username on creation
        if (user.getId() == null) {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                model.addAttribute("errorMessage", "Username already exists!");
                model.addAttribute("allRoles", roleRepository.findAll());
                return "user-form";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // Editing user
            User existingUser = userRepository.findById(user.getId()).orElseThrow();
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                // Keep old password
                user.setPassword(existingUser.getPassword());
            } else {
                // Encode new password
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        // Assign roles
        user.setRoles(new HashSet<>());
        if (roleIds != null) {
            for (Long rId : roleIds) {
                roleRepository.findById(rId).ifPresent(user.getRoles()::add);
            }
        }

        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        // Prevent deleting superadmin to keep system accessible
        userRepository.findById(id).ifPresent(user -> {
            if (!"superadmin".equals(user.getUsername())) {
                userRepository.deleteById(id);
            }
        });
        return "redirect:/users";
    }

    @GetMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable("id") Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode("password123"));
            userRepository.save(user);
        });
        return "redirect:/users";
    }
}
