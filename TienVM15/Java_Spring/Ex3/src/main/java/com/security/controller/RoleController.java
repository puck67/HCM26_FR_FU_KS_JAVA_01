package com.security.controller;

import com.security.entity.Role;
import com.security.repository.RoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("allRoles", roleRepository.findAll());
        return "role-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("role", new Role());
        return "role-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (roleOpt.isPresent()) {
            model.addAttribute("role", roleOpt.get());
            return "role-form";
        }
        return "redirect:/roles";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("role") Role role,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {
            return "role-form";
        }

        // Unique check
        Optional<Role> existingRole = roleRepository.findByName(role.getName());
        if (existingRole.isPresent()) {
            if (role.getId() == null || !existingRole.get().getId().equals(role.getId())) {
                model.addAttribute("errorMessage", "Role name already exists!");
                return "role-form";
            }
        }

        roleRepository.save(role);
        return "redirect:/roles";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        roleRepository.findById(id).ifPresent(role -> {
            // Prevent deleting system critical roles to keep system accessible
            String name = role.getName();
            if (!"SUPER_ADMIN".equals(name) && !"ADMIN".equals(name) && !"TEACHER".equals(name)) {
                roleRepository.deleteById(id);
            }
        });
        return "redirect:/roles";
    }
}
