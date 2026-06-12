package fa.training.Ex2.controller;

import fa.training.Ex2.entity.Menu;
import fa.training.Ex2.service.MenuService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final MenuService menuService;

    public GlobalControllerAdvice(MenuService menuService) {
        this.menuService = menuService;
    }

    @ModelAttribute("sidebarMenus")
    public List<Menu> sidebarMenus() {
        List<Menu> menus = menuService.getSidebarMenus();

        System.out.println("Sidebar size = " + menus.size());

        return menus;
    }
}