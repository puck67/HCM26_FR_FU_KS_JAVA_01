package com.example.ex3.controller;

import com.example.ex3.entity.Role;
import com.example.ex3.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "roles/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/add";
    }

    @PostMapping("/add")
    public String addRole(@Valid @ModelAttribute("role") Role role, BindingResult result) {
        if (roleService.existsByRoleName(role.getRoleName())) {
            result.rejectValue("roleName", "error.role", "Role name already exists");
        }
        if (result.hasErrors()) {
            return "roles/add";
        }
        roleService.saveRole(role);
        return "redirect:/roles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Role role = roleService.getRoleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
        model.addAttribute("role", role);
        return "roles/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateRole(@PathVariable("id") Long id, @Valid @ModelAttribute("role") Role role, BindingResult result) {
        if (result.hasErrors()) {
            return "roles/edit";
        }
        roleService.updateRole(id, role);
        return "redirect:/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return "redirect:/roles";
    }
}
