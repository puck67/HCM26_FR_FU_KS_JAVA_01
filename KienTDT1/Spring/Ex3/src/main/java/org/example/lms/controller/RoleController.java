package org.example.lms.controller;

import org.example.lms.model.Role;
import org.example.lms.model.User;
import org.example.lms.repository.RoleRepository;
import org.example.lms.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleController(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "roles/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/add";
    }

    @PostMapping("/add")
    public String addRole(@ModelAttribute("role") Role role, Model model) {
        if (roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
            model.addAttribute("error", "Role Name '" + role.getRoleName() + "' already exists!");
            return "roles/add";
        }
        roleRepository.save(role);
        return "redirect:/roles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
        model.addAttribute("role", role);
        return "roles/edit";
    }

    @PostMapping("/edit/{id}")
    public String editRole(@PathVariable("id") Long id,
                           @ModelAttribute("role") Role roleDetails,
                           Model model) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));

        // Unique role name validation
        if (!role.getRoleName().equals(roleDetails.getRoleName())) {
            Optional<Role> existing = roleRepository.findByRoleName(roleDetails.getRoleName());
            if (existing.isPresent()) {
                model.addAttribute("error", "Role Name '" + roleDetails.getRoleName() + "' already exists!");
                return "roles/edit";
            }
        }

        role.setRoleName(roleDetails.getRoleName());
        roleRepository.save(role);
        return "redirect:/roles";
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String deleteRole(@PathVariable("id") Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));

        // Remove role from all users who have it
        for (User user : userRepository.findAll()) {
            if (user.getRoles().contains(role)) {
                user.getRoles().remove(role);
                userRepository.save(user);
            }
        }

        roleRepository.delete(role);
        return "redirect:/roles";
    }
}
