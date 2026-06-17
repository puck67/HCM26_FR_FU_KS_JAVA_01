package com.security.controller;

import com.security.model.Role;
import com.security.model.User;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // List all users
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user/list";
    }

    // Show create form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "user/form";
    }

    // Process create form
    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             @RequestParam(value = "selectedRoles", required = false) List<Long> selectedRoleIds,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username is already taken");
        }

        if (result.hasErrors()) {
            model.addAttribute("allRoles", userService.getAllRoles());
            return "user/form";
        }

        // Map selected roles
        Set<Role> roles = new HashSet<>();
        if (selectedRoleIds != null) {
            for (Long roleId : selectedRoleIds) {
                userService.getRoleById(roleId).ifPresent(roles::add);
            }
        }
        user.setRoles(roles);

        userService.saveNewUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "User '" + user.getUsername() + "' created successfully.");
        return "redirect:/users";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.getAllRoles());
        return "user/form";
    }

    // Process edit form
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("user") User userForm,
                             BindingResult result,
                             @RequestParam(value = "selectedRoles", required = false) List<Long> selectedRoleIds,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Principal principal) {
        if (result.hasFieldErrors("username") || result.hasFieldErrors("fullName")) {
            model.addAttribute("allRoles", userService.getAllRoles());
            return "user/form";
        }

        User existingUser = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Prevent self-deactivation or self-role changes for the logged-in user to avoid accidental lockout
        if (principal != null && principal.getName().equalsIgnoreCase(existingUser.getUsername())) {
            if ("INACTIVE".equalsIgnoreCase(userForm.getStatus())) {
                redirectAttributes.addFlashAttribute("errorMessage", "You cannot deactivate your own account.");
                return "redirect:/users";
            }
        }

        existingUser.setFullName(userForm.getFullName());
        existingUser.setStatus(userForm.getStatus());

        // Map roles
        Set<Role> roles = new HashSet<>();
        if (selectedRoleIds != null) {
            for (Long roleId : selectedRoleIds) {
                userService.getRoleById(roleId).ifPresent(roles::add);
            }
        }
        existingUser.setRoles(roles);

        userService.updateUser(existingUser, false);
        redirectAttributes.addFlashAttribute("successMessage", "User '" + existingUser.getUsername() + "' updated successfully.");
        return "redirect:/users";
    }

    // Reset password form
    @GetMapping("/reset-password/{id}")
    public String showResetPasswordForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "user/reset-password";
    }

    // Process reset password
    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable("id") Long id,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        if (newPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password cannot be empty.");
            return "redirect:/users/reset-password/" + id;
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match.");
            return "redirect:/users/reset-password/" + id;
        }

        userService.resetPassword(id, newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Password reset successfully.");
        return "redirect:/users";
    }

    // Delete user
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Principal principal) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Prevent self-deletion
        if (principal != null && principal.getName().equalsIgnoreCase(user.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete your own account.");
            return "redirect:/users";
        }

        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        return "redirect:/users";
    }
}
