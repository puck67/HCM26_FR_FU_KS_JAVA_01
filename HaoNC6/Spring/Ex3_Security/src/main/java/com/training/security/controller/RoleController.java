package com.training.security.controller;

import com.training.security.entity.Role;
import com.training.security.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "roles/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("role", new Role());
        model.addAttribute("isEdit", false);
        return "roles/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        try {
            Role role = roleService.findById(id);
            model.addAttribute("role", role);
            model.addAttribute("isEdit", true);
            return "roles/form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/roles";
        }
    }

    @PostMapping("/save")
    public String saveRole(@ModelAttribute("role") Role role, Model model, RedirectAttributes redirectAttributes) {
        try {
            roleService.save(role);
            redirectAttributes.addFlashAttribute("successMessage", "Role saved successfully!");
            return "redirect:/roles";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isEdit", role.getRoleId() != null);
            return "roles/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            // Optional check: prevent deletion of core system roles
            Role role = roleService.findById(id);
            if (role.getRoleName().equals("SUPER_ADMIN") || role.getRoleName().equals("ADMIN") || role.getRoleName().equals("TEACHER")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete core system roles (SUPER_ADMIN, ADMIN, TEACHER)!");
                return "redirect:/roles";
            }
            roleService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Role deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/roles";
    }
}
