package fa.training.ex3.controllers;

import fa.training.ex3.dto.request.CreateRoleRequest;
import fa.training.ex3.dto.request.UpdateRoleRequest;
import fa.training.ex3.dto.request.CreateRoleRequest;
import fa.training.ex3.entities.Role;
import fa.training.ex3.enums.RoleEnum;
import fa.training.ex3.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "roles/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("roleRequest", new CreateRoleRequest());
        model.addAttribute("isEdit", false);
        model.addAttribute("roleEnums", RoleEnum.values());
        return "roles/form";
    }

    @PostMapping("/add")
    public String createRole(@Valid @ModelAttribute("roleRequest") CreateRoleRequest request,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("roleEnums", RoleEnum.values());
            return "roles/form";
        }
        try {
            roleService.create(request);
            redirectAttributes.addFlashAttribute("successMessage", "Role created successfully!");
            return "redirect:/roles";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isEdit", false);
            model.addAttribute("roleEnums", RoleEnum.values());
            return "roles/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Role role = roleService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + id));

        UpdateRoleRequest request = UpdateRoleRequest.builder()
                .role_id(role.getRole_id())
                .role_name(role.getRole_name())
                .build();

        model.addAttribute("roleRequest", request);
        model.addAttribute("isEdit", true);
        model.addAttribute("roleEnums", RoleEnum.values());
        return "roles/form";
    }

    @PostMapping("/edit/{id}")
    public String updateRole(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("roleRequest") UpdateRoleRequest request,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("roleEnums", RoleEnum.values());
            return "roles/form";
        }
        try {
            request.setRole_id(id);
            roleService.update(request);
            redirectAttributes.addFlashAttribute("successMessage", "Role updated successfully!");
            return "redirect:/roles";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isEdit", true);
            model.addAttribute("roleEnums", RoleEnum.values());
            return "roles/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            roleService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Role deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/roles";
    }
}
