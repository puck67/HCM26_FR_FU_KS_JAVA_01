package com.menu.controller;

import com.menu.model.Menu;
import com.menu.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public String listMenus(Model model) {
        model.addAttribute("menus", menuService.getHierarchicalMenus());
        model.addAttribute("activePage", "menus");
        return "menu/list";
    }

    @GetMapping("/new")
    public String newMenuForm(Model model) {
        Menu menu = new Menu();
        model.addAttribute("menu", menu);
        model.addAttribute("possibleParents", menuService.getAllMenus());
        model.addAttribute("activePage", "menus");
        return "menu/form";
    }

    @GetMapping("/edit/{id}")
    public String editMenuForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Menu menu = menuService.getMenuById(id).orElse(null);
        if (menu == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Menu not found.");
            return "redirect:/admin/menus";
        }
        
        // Filter possible parents to prevent cycles (cannot select self or descendants)
        List<Menu> possibleParents = menuService.getAllMenus().stream()
                .filter(m -> !isDescendantOrSelf(m, id))
                .collect(Collectors.toList());

        model.addAttribute("menu", menu);
        model.addAttribute("possibleParents", possibleParents);
        model.addAttribute("activePage", "menus");
        return "menu/form";
    }

    @PostMapping("/save")
    public String saveMenu(@Valid @ModelAttribute("menu") Menu menu, 
                           BindingResult result, 
                           @RequestParam(value = "roleList", required = false) List<String> roleList,
                           Model model, 
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<Menu> possibleParents = menuService.getAllMenus().stream()
                    .filter(m -> menu.getId() == null || !isDescendantOrSelf(m, menu.getId()))
                    .collect(Collectors.toList());
            model.addAttribute("possibleParents", possibleParents);
            model.addAttribute("activePage", "menus");
            return "menu/form";
        }

        try {
            // Convert list of roles to comma-separated string
            if (roleList != null && !roleList.isEmpty()) {
                menu.setRoles(String.join(",", roleList));
            } else {
                menu.setRoles(""); // Empty string means public
            }

            // If parent id is null or empty, ensure parent object is null
            if (menu.getParent() != null && menu.getParent().getId() == null) {
                menu.setParent(null);
            }
            
            menuService.saveMenu(menu);
            redirectAttributes.addFlashAttribute("successMessage", "Menu saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving menu: " + e.getMessage());
            return "redirect:/admin/menus";
        }

        return "redirect:/admin/menus";
    }

    @PostMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id, 
                             @RequestParam(required = false, defaultValue = "false") boolean cascade, 
                             RedirectAttributes redirectAttributes) {
        try {
            menuService.deleteMenu(id, cascade);
            redirectAttributes.addFlashAttribute("successMessage", "Menu deleted successfully!");
        } catch (IllegalStateException e) {
            // Thrown if cascade=false and menu has children
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("showCascadeOptionId", id);
            
            // Fetch name of menu for the modal display
            menuService.getMenuById(id).ifPresent(m -> {
                redirectAttributes.addFlashAttribute("cascadeMenuName", m.getName());
            });
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/menus";
    }

    private boolean isDescendantOrSelf(Menu menu, Long targetId) {
        if (menu.getId().equals(targetId)) {
            return true;
        }
        Menu parent = menu.getParent();
        while (parent != null) {
            if (parent.getId().equals(targetId)) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }
}
