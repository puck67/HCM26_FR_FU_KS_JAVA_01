package com.example.menumanagement.controller;

import com.example.menumanagement.entity.Menu;
import com.example.menumanagement.service.MenuService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class MenuModelAttributeAdvice {

    private final MenuService menuService;

    public MenuModelAttributeAdvice(MenuService menuService) {
        this.menuService = menuService;
    }

    @ModelAttribute("sidebarMenus")
    public List<Menu> globalSidebarMenus() {
        return menuService.getAllRootMenus(); // Recursively fetched via Entity relation if accessed
    }
}
