package fa.training.ex3.controllers;

import fa.training.ex3.dto.request.CreateUserRequest;
import fa.training.ex3.dto.request.ResetPasswordRequest;
import fa.training.ex3.dto.request.UpdateUserRequest;
import fa.training.ex3.entities.Role;
import fa.training.ex3.entities.User;
import fa.training.ex3.services.RoleService;
import fa.training.ex3.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("userRequest", new CreateUserRequest());
        model.addAttribute("isEdit", false);
        model.addAttribute("allRoles", roleService.findAll());
        return "users/form";
    }

    @PostMapping("/add")
    public String createUser(@Valid @ModelAttribute("userRequest") CreateUserRequest request,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("allRoles", roleService.findAll());
            return "users/form";
        }
        try {
            userService.create(request);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isEdit", false);
            model.addAttribute("allRoles", roleService.findAll());
            return "users/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        UpdateUserRequest request = UpdateUserRequest.builder()
                .user_id(user.getUser_id())
                .username(user.getUsername())
                .full_name(user.getFull_name())
                .status(user.getStatus())
                .roleIds(user.getRoles().stream().map(Role::getRole_id).collect(Collectors.toSet()))
                .build();

        model.addAttribute("userRequest", request);
        model.addAttribute("isEdit", true);
        model.addAttribute("allRoles", roleService.findAll());
        return "users/form";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id,
            @Valid @ModelAttribute("userRequest") UpdateUserRequest request,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("allRoles", roleService.findAll());
            return "users/form";
        }
        try {
            request.setUser_id(id);
            userService.update(request);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("isEdit", true);
            model.addAttribute("allRoles", roleService.findAll());
            return "users/form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/reset-password/{id}")
    public String showResetPasswordForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .user_id(user.getUser_id())
                .build();

        model.addAttribute("resetPasswordRequest", request);
        model.addAttribute("username", user.getUsername());
        return "users/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @ModelAttribute("resetPasswordRequest") ResetPasswordRequest request,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            User user = userService.findById(request.getUser_id()).orElse(null);
            model.addAttribute("username", user != null ? user.getUsername() : "");
            return "users/reset-password";
        }
        try {
            userService.resetPassword(request);
            redirectAttributes.addFlashAttribute("successMessage", "Password reset successfully!");
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            User user = userService.findById(request.getUser_id()).orElse(null);
            model.addAttribute("username", user != null ? user.getUsername() : "");
            return "users/reset-password";
        }
    }
}
