package com.example.demo.config;

import com.example.demo.dto.MenuView;
import com.example.demo.entity.Role;
import com.example.demo.service.MenuService;
import com.example.demo.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

    public static final String SESSION_ROLE = "currentRole";

    private final MenuService menuService;
    private final RoleService roleService;

    @ModelAttribute("sidebarMenus")
    public List<MenuView> sidebarMenus(HttpSession session) {
        String role = (String) session.getAttribute(SESSION_ROLE);
        if (role == null) {
            role = "ADMIN";
            session.setAttribute(SESSION_ROLE, role);
        }
        return menuService.getSidebarMenus(role);
    }

    @ModelAttribute("currentRole")
    public String currentRole(HttpSession session) {
        String role = (String) session.getAttribute(SESSION_ROLE);
        if (role == null) {
            role = "ADMIN";
            session.setAttribute(SESSION_ROLE, role);
        }
        return role;
    }

    @ModelAttribute("roles")
    public List<Role> roles() {
        return roleService.findAll();
    }

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
