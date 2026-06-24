package com.example.menu.controller;

import com.example.menu.entity.Menu;
import com.example.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class SidebarControllerAdvice {

    private final MenuService menuService;

    @ModelAttribute("sidebarMenus")
    public List<Menu> populateSidebar(HttpServletRequest request) {
        List<Menu> allRootMenus = menuService.getSidebarMenus();
        
        HttpSession session = request.getSession();
        String currentRole = (String) session.getAttribute("currentRole");
        if (currentRole == null) {
            currentRole = "Admin";
            session.setAttribute("currentRole", currentRole);
        }

        // Basic mock role filtering for Bonus point
        if ("Teacher".equals(currentRole)) {
            return allRootMenus.stream()
                    .filter(m -> m.getName().equals("Dashboard") || m.getName().equals("Training Management"))
                    // Note: Ideally we filter children too, but for mock purposes we keep it simple
                    .collect(Collectors.toList());
        } else if ("Student".equals(currentRole)) {
            return allRootMenus.stream()
                    .filter(m -> m.getName().equals("Dashboard") || m.getName().equals("My Courses"))
                    .collect(Collectors.toList());
        }
        
        // Admin gets all
        return allRootMenus;
    }

    @ModelAttribute("currentRole")
    public String currentRole(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("currentRole");
    }
}
