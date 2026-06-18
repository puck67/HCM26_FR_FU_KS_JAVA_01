package com.example.Ex2.controller;

import com.example.Ex2.model.Menu;
import com.example.Ex2.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public String listMenus(HttpSession session, Model model) {
        if (session.getAttribute("currentRole") == null) {
            session.setAttribute("currentRole", "ADMIN");
        }
        
        model.addAttribute("sidebarMenus", menuService.getTopLevelMenus());
        
        // Generic page parameters
        model.addAttribute("entityName", "menus");
        model.addAttribute("pageTitle", "Menu Management");
        model.addAttribute("createButtonText", "Create Menu");
        
        model.addAttribute("menus", menuService.getFlattenedMenus());
        
        return "menu/list";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session, Model model) {
        if (session.getAttribute("currentRole") == null) {
            session.setAttribute("currentRole", "ADMIN");
        }
        model.addAttribute("sidebarMenus", menuService.getTopLevelMenus());
        model.addAttribute("entityName", "menus");
        model.addAttribute("pageTitle", "Create Menu");
        model.addAttribute("createButtonText", "Create Menu");
        
        model.addAttribute("menu", new Menu());
        model.addAttribute("parents", menuService.getFlattenedMenus());
        return "menu/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("currentRole") == null) {
            session.setAttribute("currentRole", "ADMIN");
        }
        Menu menu = menuService.getMenuById(id).orElse(null);
        if (menu == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Menu not found!");
            return "redirect:/menus";
        }
        
        model.addAttribute("sidebarMenus", menuService.getTopLevelMenus());
        model.addAttribute("entityName", "menus");
        model.addAttribute("pageTitle", "Edit Menu: " + menu.getName());
        model.addAttribute("createButtonText", "Create Menu");
        
        model.addAttribute("menu", menu);
        // Exclude this menu and its descendants from the potential parent list
        model.addAttribute("parents", menuService.getAvailableParentsFor(id));
        return "menu/form";
    }

    @PostMapping("/save")
    public String saveMenu(@ModelAttribute("menu") Menu menu, 
                           @RequestParam(value = "parentId", required = false) Long parentId,
                           RedirectAttributes redirectAttributes) {
        try {
            if (parentId != null) {
                Menu parent = menuService.getMenuById(parentId).orElse(null);
                menu.setParent(parent);
            } else {
                menu.setParent(null);
            }
            
            menuService.saveMenu(menu);
            redirectAttributes.addFlashAttribute("successMessage", "Menu saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving menu: " + e.getMessage());
            if (menu.getId() != null) {
                return "redirect:/menus/edit/" + menu.getId();
            } else {
                return "redirect:/menus/create";
            }
        }
        return "redirect:/menus";
    }

    @PostMapping("/delete/{id}")
    public String deleteMenu(@PathVariable("id") Long id, 
                             @RequestParam(value = "deleteOption", defaultValue = "RESTRICT") String deleteOption,
                             RedirectAttributes redirectAttributes) {
        try {
            menuService.deleteMenu(id, deleteOption);
            redirectAttributes.addFlashAttribute("successMessage", "Menu deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting menu: " + e.getMessage());
        }
        return "redirect:/menus";
    }
}
