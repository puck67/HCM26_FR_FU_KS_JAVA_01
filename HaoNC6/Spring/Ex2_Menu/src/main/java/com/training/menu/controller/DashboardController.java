package com.training.menu.controller;

import com.training.menu.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final MenuService menuService;

    public DashboardController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalMenus", menuService.getTotalMenus());
        model.addAttribute("totalParentMenus", menuService.getTotalParentMenus());
        model.addAttribute("totalSubMenus", menuService.getTotalSubMenus());
        model.addAttribute("pageTitle", "Dashboard");
        return "dashboard";
    }
}
