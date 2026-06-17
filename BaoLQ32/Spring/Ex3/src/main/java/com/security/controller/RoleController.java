package com.security.controller;

import com.security.model.Role;
import com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private UserService userService;

    // List all roles
    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", userService.getAllRoles());
        return "role/list";
    }

    // Show create form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        return "role/form";
    }

    // Process create form
    @PostMapping("/new")
    public String createRole(@Valid @ModelAttribute("role") Role role,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            result.rejectValue("roleName", "error.role", "Role name cannot be empty");
        } else if (userService.getRoleByName(role.getRoleName().trim().toUpperCase()).isPresent()) {
            result.rejectValue("roleName", "error.role", "Role '" + role.getRoleName() + "' already exists");
        }

        if (result.hasErrors()) {
            return "role/form";
        }

        userService.saveRole(role);
        redirectAttributes.addFlashAttribute("successMessage", "Role '" + role.getRoleName() + "' created successfully.");
        return "redirect:/roles";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Role role = userService.getRoleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
        model.addAttribute("role", role);
        return "role/form";
    }

    // Process edit form
    @PostMapping("/edit/{id}")
    public String updateRole(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("role") Role roleForm,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (roleForm.getRoleName() == null || roleForm.getRoleName().trim().isEmpty()) {
            result.rejectValue("roleName", "error.role", "Role name cannot be empty");
        }

        Role existingRole = userService.getRoleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));

        // If name changed, check uniqueness
        String newName = roleForm.getRoleName().trim().toUpperCase();
        if (!existingRole.getRoleName().equalsIgnoreCase(newName)) {
            if (userService.getRoleByName(newName).isPresent()) {
                result.rejectValue("roleName", "error.role", "Role name '" + newName + "' is already taken");
            }
        }

        if (result.hasErrors()) {
            return "role/form";
        }

        existingRole.setRoleName(newName);
        userService.saveRole(existingRole);
        redirectAttributes.addFlashAttribute("successMessage", "Role '" + existingRole.getRoleName() + "' updated successfully.");
        return "redirect:/roles";
    }

    // Process delete form
    @PostMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Role role = userService.getRoleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));

        // Prevent deleting core system roles to avoid breaking user mappings
        String name = role.getRoleName();
        if ("SUPER_ADMIN".equalsIgnoreCase(name) || "ADMIN".equalsIgnoreCase(name) || "TEACHER".equalsIgnoreCase(name)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Core system role '" + name + "' cannot be deleted.");
            return "redirect:/roles";
        }

        try {
            userService.deleteRole(id);
            redirectAttributes.addFlashAttribute("successMessage", "Role deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Role cannot be deleted. It is probably still assigned to some users.");
        }
        return "redirect:/roles";
    }
}
