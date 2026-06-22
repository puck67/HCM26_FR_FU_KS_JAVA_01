package com.example.demo.controller;

import com.example.demo.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final MenuService menuService;

    public DashboardController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalMenus", menuService.countTotalMenus());
        model.addAttribute("totalParentMenus", menuService.countParentMenus());
        model.addAttribute("totalSubMenus", menuService.countSubMenus());
        return "dashboard/index";
    }

    @GetMapping("/switch-role")
    public String switchRole(@RequestParam("role") String role, HttpSession session, HttpServletRequest request) {
        if (role != null && (role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("TEACHER") || role.equalsIgnoreCase("STUDENT"))) {
            session.setAttribute("currentRole", role.toUpperCase());
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/dashboard");
    }
}
