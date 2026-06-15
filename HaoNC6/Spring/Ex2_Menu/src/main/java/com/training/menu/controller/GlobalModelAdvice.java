package com.training.menu.controller;

import com.training.menu.entity.Menu;
import com.training.menu.service.MenuService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Global model advice that injects sidebar menu data into every view.
 * This ensures the sidebar is dynamically rendered on ALL pages
 * without repeating the logic in each controller.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final MenuService menuService;

    public GlobalModelAdvice(MenuService menuService) {
        this.menuService = menuService;
    }

    @ModelAttribute("sidebarMenus")
    public List<Menu> populateSidebarMenus() {
        return menuService.getActiveRootMenus();
    }
}
