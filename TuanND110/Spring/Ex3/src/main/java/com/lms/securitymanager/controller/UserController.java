package com.lms.securitymanager.controller;

import com.lms.securitymanager.dto.UserForm;
import com.lms.securitymanager.entity.Role;
import com.lms.securitymanager.entity.User;
import com.lms.securitymanager.service.RoleService;
import com.lms.securitymanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/user-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        UserForm form = new UserForm();
        model.addAttribute("userForm", form);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "users/user-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
            return "redirect:/users";
        }

        UserForm form = new UserForm();
        form.setUserId(user.getUserId());
        form.setUsername(user.getUsername());
        form.setFullName(user.getFullName());
        form.setStatus(user.getStatus());
        form.setRoleIds(user.getRoles().stream().map(Role::getRoleId).collect(Collectors.toList()));

        model.addAttribute("userForm", form);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "users/user-form";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("userForm") UserForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        // Validation check for new user password
        if (form.getUserId() == null) {
            if (form.getPassword() == null || form.getPassword().trim().isEmpty()) {
                result.rejectValue("password", "error.userForm", "Password is required for new users");
            } else if (form.getPassword().length() < 6) {
                result.rejectValue("password", "error.userForm", "Password must be at least 6 characters");
            }

            // Check if username unique
            if (userService.getUserByUsername(form.getUsername()).isPresent()) {
                result.rejectValue("username", "error.userForm", "Username is already taken");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "users/user-form";
        }

        // Map form to entity
        User user = new User();
        user.setUserId(form.getUserId());
        user.setUsername(form.getUsername());
        user.setFullName(form.getFullName());
        user.setStatus(form.getStatus());

        Set<Role> roles = new HashSet<>();
        for (Long roleId : form.getRoleIds()) {
            Role role = roleService.getRoleById(roleId);
            if (role != null) {
                roles.add(role);
            }
        }
        user.setRoles(roles);

        try {
            if (form.getUserId() == null) {
                user.setPassword(form.getPassword());
                userService.saveUser(user);
                redirectAttributes.addFlashAttribute("successMessage", "User registered successfully!");
            } else {
                userService.updateUser(user);
                redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving user: " + e.getMessage());
            return "redirect:/users";
        }

        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam Long userId, @RequestParam String newPassword, RedirectAttributes redirectAttributes) {
        if (newPassword == null || newPassword.trim().isEmpty() || newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 6 characters long.");
            return "redirect:/users";
        }

        try {
            userService.resetPassword(userId, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password reset successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error resetting password: " + e.getMessage());
        }
        return "redirect:/users";
    }
}
