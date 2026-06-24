package com.example.menu.controller;

import com.example.menu.service.MenuService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final MenuService menuService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalMenus", menuService.getTotalMenus());
        model.addAttribute("totalParentMenus", menuService.getTotalParentMenus());
        model.addAttribute("totalSubMenus", menuService.getTotalSubMenus());
        return "dashboard";
    }

    @PostMapping("/switch-role")
    public String switchRole(@RequestParam String role, HttpSession session) {
        session.setAttribute("currentRole", role);
        return "redirect:/";
    }
}
