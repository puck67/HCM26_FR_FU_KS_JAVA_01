package com.example.Ex2.controller;

import com.example.Ex2.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @Autowired
    private MenuService menuService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(HttpSession session, Model model) {
        // Default role is ADMIN
        if (session.getAttribute("currentRole") == null) {
            session.setAttribute("currentRole", "ADMIN");
        }

        // Retrieve top level menus for the sidebar (which Thymeleaf sidebar fragment will loop over)
        model.addAttribute("sidebarMenus", menuService.getTopLevelMenus());

        // Dashboard statistics
        model.addAttribute("totalMenus", menuService.getTotalMenusCount());
        model.addAttribute("totalParentMenus", menuService.getParentMenusCount());
        model.addAttribute("totalSubMenus", menuService.getSubMenusCount());

        return "dashboard";
    }

    @GetMapping("/change-role")
    public String changeRole(@RequestParam("role") String role, HttpSession session) {
        session.setAttribute("currentRole", role.toUpperCase());
        return "redirect:/dashboard";
    }
}
