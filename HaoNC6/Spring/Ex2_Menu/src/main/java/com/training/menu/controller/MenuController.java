package com.training.menu.controller;

import com.training.menu.entity.Menu;
import com.training.menu.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Display the menu management list page.
     */
    @GetMapping
    public String listMenus(Model model) {
        List<Menu> rootMenus = menuService.getAllRootMenus();
        model.addAttribute("rootMenus", rootMenus);
        model.addAttribute("pageTitle", "Menu Management");
        return "menu/list";
    }

    /**
     * Show the Add Menu form.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        Menu menu = new Menu();
        menu.setStatus(true);
        menu.setDisplayOrder(0);
        model.addAttribute("menu", menu);
        model.addAttribute("parentMenus", menuService.getPotentialParents(null));
        model.addAttribute("pageTitle", "Add Menu");
        model.addAttribute("formAction", "/menus/add");
        return "menu/form";
    }

    /**
     * Process the Add Menu form.
     */
    @PostMapping("/add")
    public String addMenu(@Valid @ModelAttribute("menu") Menu menu,
                          BindingResult result,
                          @RequestParam(value = "parentId", required = false) Long parentId,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("parentMenus", menuService.getPotentialParents(null));
            model.addAttribute("pageTitle", "Add Menu");
            model.addAttribute("formAction", "/menus/add");
            return "menu/form";
        }

        // Set parent if provided
        if (parentId != null) {
            Menu parent = menuService.getMenuById(parentId);
            menu.setParent(parent);
        }

        menuService.saveMenu(menu);
        redirectAttributes.addFlashAttribute("successMessage", "Menu '" + menu.getName() + "' has been created successfully!");
        return "redirect:/menus";
    }

    /**
     * Show the Edit Menu form.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Menu menu = menuService.getMenuById(id);
        model.addAttribute("menu", menu);
        model.addAttribute("parentMenus", menuService.getPotentialParents(id));
        model.addAttribute("pageTitle", "Edit Menu");
        model.addAttribute("formAction", "/menus/edit/" + id);
        return "menu/form";
    }

    /**
     * Process the Edit Menu form.
     */
    @PostMapping("/edit/{id}")
    public String editMenu(@PathVariable Long id,
                           @Valid @ModelAttribute("menu") Menu menu,
                           BindingResult result,
                           @RequestParam(value = "parentId", required = false) Long parentId,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("parentMenus", menuService.getPotentialParents(id));
            model.addAttribute("pageTitle", "Edit Menu");
            model.addAttribute("formAction", "/menus/edit/" + id);
            return "menu/form";
        }

        Menu existingMenu = menuService.getMenuById(id);
        existingMenu.setName(menu.getName());
        existingMenu.setUrl(menu.getUrl());
        existingMenu.setIcon(menu.getIcon());
        existingMenu.setDisplayOrder(menu.getDisplayOrder());
        existingMenu.setStatus(menu.getStatus());

        // Update parent
        if (parentId != null) {
            Menu parent = menuService.getMenuById(parentId);
            existingMenu.setParent(parent);
        } else {
            existingMenu.setParent(null);
        }

        menuService.saveMenu(existingMenu);
        redirectAttributes.addFlashAttribute("successMessage", "Menu '" + existingMenu.getName() + "' has been updated successfully!");
        return "redirect:/menus";
    }

    /**
     * Delete a menu (cascade deletes children).
     */
    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Menu menu = menuService.getMenuById(id);
        String menuName = menu.getName();
        menuService.deleteMenu(id);
        redirectAttributes.addFlashAttribute("successMessage", "Menu '" + menuName + "' and its sub-menus have been deleted successfully!");
        return "redirect:/menus";
    }
}
