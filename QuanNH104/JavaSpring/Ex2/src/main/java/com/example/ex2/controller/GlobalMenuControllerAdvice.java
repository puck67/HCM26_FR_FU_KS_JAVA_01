package com.example.ex2.controller;

import com.example.ex2.entity.Menu;
import com.example.ex2.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalMenuControllerAdvice {

    private final MenuService menuService;

    public GlobalMenuControllerAdvice(MenuService menuService) {
        this.menuService = menuService;
    }

    @ModelAttribute
    public void populateMenus(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String currentRole = (String) session.getAttribute("currentRole");
        if (currentRole == null) {
            currentRole = "ADMIN"; // Role mặc định ban đầu là ADMIN
            session.setAttribute("currentRole", currentRole);
        }

        // Lấy danh sách menu dạng cây (chỉ gồm level 1) đã được lọc theo role hiện tại
        List<Menu> menus = menuService.getMenusForRole(currentRole);
        
        model.addAttribute("sidebarMenus", menus);
        model.addAttribute("currentRole", currentRole);
    }
}
