package com.menu.controller;

import com.menu.model.Menu;
import com.menu.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private MenuService menuService;

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        // Retrieve current role from session, default to ADMIN if not set
        String currentRole = (String) session.getAttribute("currentRole");
        if (currentRole == null) {
            currentRole = "ADMIN";
            session.setAttribute("currentRole", currentRole);
        }
        
        // Retrieve menus allowed for the current role
        List<Menu> sidebarMenus = menuService.getActiveRootMenusForRole(currentRole);
        
        // Add to model
        model.addAttribute("sidebarMenus", sidebarMenus);
        model.addAttribute("currentRole", currentRole);
    }
}
