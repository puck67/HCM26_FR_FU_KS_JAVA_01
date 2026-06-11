package com.example.demo.controller;

import com.example.demo.config.GlobalModelAdvice;
import com.example.demo.service.MenuService;
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
        model.addAttribute("totalMenus", menuService.totalMenus());
        model.addAttribute("totalParents", menuService.totalParentMenus());
        model.addAttribute("totalSubs", menuService.totalSubMenus());
        return "dashboard";
    }

    @PostMapping("/switch-role")
    public String switchRole(@RequestParam String role, HttpSession session) {
        session.setAttribute(GlobalModelAdvice.SESSION_ROLE, role.toUpperCase());
        return "redirect:/";
    }
}
