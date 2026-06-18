package com.menu.controller;

import com.menu.entity.Menu;
import com.menu.service.MenuService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class MenuAdvice {

    private final MenuService menuService;

    @ModelAttribute("menus")
    public List<Menu> populateMenus(HttpSession session) {
        String role = (String) session.getAttribute("currentRole");
        if (role == null) {
            role = "Admin";
            session.setAttribute("currentRole", role);
        }
        return menuService.getFilteredRootMenus(role);
    }

    @ModelAttribute("currentRole")
    public String currentRole(HttpSession session) {
        String role = (String) session.getAttribute("currentRole");
        if (role == null) {
            role = "Admin";
            session.setAttribute("currentRole", role);
        }
        return role;
    }
}
