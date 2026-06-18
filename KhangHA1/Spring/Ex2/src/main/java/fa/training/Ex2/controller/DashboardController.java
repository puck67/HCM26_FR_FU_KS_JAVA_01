package fa.training.Ex2.controller;

import fa.training.Ex2.service.MenuService;
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

        model.addAttribute(
                "totalMenus",
                menuService.totalMenus());

        model.addAttribute(
                "totalParents",
                menuService.totalParentMenus());

        model.addAttribute(
                "totalChildren",
                menuService.totalSubMenus());

        return "dashboard";
    }
}