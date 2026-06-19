package com.example.demo.controller;

import com.example.demo.model.Menu;
import com.example.demo.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final MenuService menuService;

    public GlobalControllerAdvice(MenuService menuService) {
        this.menuService = menuService;
    }

    @ModelAttribute("sidebarMenus")
    public List<Menu> getSidebarMenus(HttpSession session) {
        String role = (String) session.getAttribute("currentRole");
        if (role == null) {
            role = "ADMIN";
            session.setAttribute("currentRole", role);
        }
        return menuService.getSidebarMenus(role);
    }

    @ModelAttribute("currentRole")
    public String getCurrentRole(HttpSession session) {
        String role = (String) session.getAttribute("currentRole");
        if (role == null) {
            role = "ADMIN";
            session.setAttribute("currentRole", role);
        }
        return role;
    }
}
