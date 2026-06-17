package fa.training.controller;

import fa.training.entity.Role;
import fa.training.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "roles/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/form";
    }

    @PostMapping("/add")
    public String addRole(@ModelAttribute Role role) {
        roleRepository.save(role);
        return "redirect:/roles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Role role = roleRepository.findById(id).orElseThrow();
        model.addAttribute("role", role);
        return "roles/form";
    }

    @PostMapping("/edit/{id}")
    public String editRole(@PathVariable Long id, @ModelAttribute Role updatedRole) {
        Role existingRole = roleRepository.findById(id).orElseThrow();
        existingRole.setRoleName(updatedRole.getRoleName());
        roleRepository.save(existingRole);
        return "redirect:/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleRepository.deleteById(id);
        return "redirect:/roles";
    }
}
