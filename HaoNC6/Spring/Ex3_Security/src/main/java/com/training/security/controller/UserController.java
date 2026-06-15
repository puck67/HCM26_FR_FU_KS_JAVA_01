package com.training.security.controller;

import com.training.security.entity.User;
import com.training.security.service.RoleService;
import com.training.security.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("isEdit", false);
        return "users/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            User user = userService.findById(id);
            model.addAttribute("user", user);
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("isEdit", true);
            return "users/form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/users";
        }
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user, 
                           @RequestParam(value = "isEdit", defaultValue = "false") boolean isEdit,
                           Model model, RedirectAttributes redirectAttributes) {
        try {
            if (isEdit) {
                userService.update(user);
                redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            } else {
                if (user.getPassword() == null || user.getPassword().isEmpty()) {
                    throw new IllegalArgumentException("Password is required for new users");
                }
                userService.register(user);
                redirectAttributes.addFlashAttribute("successMessage", "User registered successfully!");
            }
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("allRoles", roleService.findAll());
            model.addAttribute("isEdit", isEdit);
            return "users/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            // Optional check: prevent superadmin from deleting themselves to avoid locking the database
            User user = userService.findById(id);
            if (user.getUsername().equals("superadmin")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete the default 'superadmin' user!");
                return "redirect:/users";
            }
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("userId") Long userId, 
                                 @RequestParam("newPassword") String newPassword, 
                                 RedirectAttributes redirectAttributes) {
        try {
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            userService.resetPassword(userId, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password reset successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }
}
