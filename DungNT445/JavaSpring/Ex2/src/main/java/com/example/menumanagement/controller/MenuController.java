package com.example.menumanagement.controller;

import com.example.menumanagement.entity.Menu;
import com.example.menumanagement.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public String listMenus(Model model) {
        model.addAttribute("menus", menuService.getAllMenus());
        return "menu-list";
    }

    @GetMapping("/new")
    public String createMenuForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("allMenus", menuService.getAllMenus());
        return "menu-form";
    }

    @PostMapping("/save")
    public String saveMenu(@ModelAttribute("menu") Menu menu, RedirectAttributes redirectAttributes) {
        if (menu.getParent() != null) {
            if (menu.getParent().getId() == null) {
                menu.setParent(null); // Fix empty select mapping
            } else {
                Menu parent = menuService.getMenuById(menu.getParent().getId());
                menu.setParent(parent);
            }
        }
        menuService.saveMenu(menu);
        redirectAttributes.addFlashAttribute("successMessage", "Menu saved successfully!");
        return "redirect:/menus";
    }

    @GetMapping("/edit/{id}")
    public String editMenuForm(@PathVariable Long id, Model model) {
        Menu menu = menuService.getMenuById(id);
        model.addAttribute("menu", menu);
        model.addAttribute("allMenus", menuService.getAllMenus());
        return "menu-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            menuService.deleteMenu(id);
            redirectAttributes.addFlashAttribute("successMessage", "Menu deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/menus";
    }
}
