package com.lms.securitymanager.controller;

import com.lms.securitymanager.entity.Role;
import com.lms.securitymanager.repository.RoleRepository;
import com.lms.securitymanager.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleRepository roleRepository;

    public RoleController(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "roles/role-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/role-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Role role = roleService.getRoleById(id);
        if (role == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Role not found.");
            return "redirect:/roles";
        }
        model.addAttribute("role", role);
        return "roles/role-form";
    }

    @PostMapping("/save")
    public String saveRole(@Valid @ModelAttribute("role") Role role, BindingResult result, RedirectAttributes redirectAttributes) {
        // Enforce role name constraints
        if (role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            result.rejectValue("roleName", "error.role", "Role name is required");
        } else {
            String roleName = role.getRoleName().trim().toUpperCase();
            
            // Check for uniqueness
            if (role.getRoleId() == null) {
                if (roleRepository.findByRoleName(roleName).isPresent()) {
                    result.rejectValue("roleName", "error.role", "Role name already exists");
                }
            } else {
                Role existing = roleService.getRoleById(role.getRoleId());
                if (existing != null && !existing.getRoleName().equalsIgnoreCase(roleName)) {
                    if (roleRepository.findByRoleName(roleName).isPresent()) {
                        result.rejectValue("roleName", "error.role", "Role name already exists");
                    }
                }
            }
        }

        if (result.hasErrors()) {
            return "roles/role-form";
        }

        roleService.saveRole(role);
        redirectAttributes.addFlashAttribute("successMessage", "Role saved successfully!");
        return "redirect:/roles";
    }

    @PostMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteRole(id);
            redirectAttributes.addFlashAttribute("successMessage", "Role deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/roles";
    }
}
