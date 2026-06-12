package com.example.ex3.controller;

import com.example.ex3.entity.User;
import com.example.ex3.service.RoleService;
import com.example.ex3.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "users/add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            // handle error
            return "redirect:/users/add?error=roles";
        }
        userService.saveUser(user, roleIds);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "users/edit";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, @ModelAttribute("user") User user, @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return "redirect:/users/edit/" + id + "?error=roles";
        }
        userService.updateUser(id, user, roleIds);
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
    
    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable("id") Long id, @RequestParam("newPassword") String newPassword) {
        userService.resetPassword(id, newPassword);
        return "redirect:/users";
    }
}
