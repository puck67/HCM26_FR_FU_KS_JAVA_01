package com.example.demo.controller;

import com.example.demo.controller.base.GenericController;
import com.example.demo.model.Menu;
import com.example.demo.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/menus")
public class MenuController extends GenericController<Menu, Long, MenuService> {

    public MenuController(MenuService menuService) {
        super(menuService);
    }

    @GetMapping
    public String list(Model model) {
        List<Menu> allMenus = service.findAll();
        model.addAttribute("menus", allMenus);
        model.addAttribute("entityName", "menus");
        model.addAttribute("pageTitle", "Menu Management");
        model.addAttribute("createButtonText", "Add New Menu");
        return "menu/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Menu menu = new Menu();
        menu.setStatus(true);
        menu.setDisplayOrder(0);
        prepareFormModel(model, menu);
        return "menu/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Menu ID cannot be null");
            return "redirect:/menus";
        }
        Optional<Menu> menuOpt = service.findById(id);
        if (menuOpt.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Menu with ID ").append(id).append(" not found.");
            redirectAttributes.addFlashAttribute("errorMessage", sb.toString());
            return "redirect:/menus";
        }
        prepareFormModel(model, menuOpt.get());
        return "menu/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("menu") Menu menu, 
                       BindingResult bindingResult, 
                       @RequestParam(value = "selectedRoles", required = false) List<String> selectedRoles,
                       Model model, 
                       RedirectAttributes redirectAttributes) {
        
        if (selectedRoles != null && !selectedRoles.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            selectedRoles.forEach(role -> {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(role);
            });
            menu.setRoles(sb.toString());
        } else {
            menu.setRoles("");
        }

        if (menu.getParent() != null && menu.getParent().getId() == null) {
            menu.setParent(null);
        }

        if (bindingResult.hasErrors()) {
            prepareFormModel(model, menu);
            return "menu/form";
        }

        try {
            if (menu.getParent() != null) {
                Menu parentEntity = service.findById(menu.getParent().getId()).orElse(null);
                menu.setParent(parentEntity);
            }
            service.validateMenu(menu);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("name", "error.menu", e.getMessage());
            prepareFormModel(model, menu);
            return "menu/form";
        }

        service.save(menu);
        StringBuilder sb = new StringBuilder();
        sb.append("Menu '").append(menu.getName()).append("' saved successfully.");
        redirectAttributes.addFlashAttribute("successMessage", sb.toString());
        return "redirect:/menus";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Menu ID cannot be null");
            return "redirect:/menus";
        }
        Optional<Menu> menuOpt = service.findById(id);
        if (menuOpt.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Menu with ID ").append(id).append(" not found.");
            redirectAttributes.addFlashAttribute("errorMessage", sb.toString());
            return "redirect:/menus";
        }
        
        Menu menu = menuOpt.get();
        service.deleteById(id);
        StringBuilder sb = new StringBuilder();
        sb.append("Menu '").append(menu.getName()).append("' deleted successfully.");
        redirectAttributes.addFlashAttribute("successMessage", sb.toString());
        return "redirect:/menus";
    }

    private void prepareFormModel(Model model, Menu menu) {
        model.addAttribute("menu", menu);
        List<Menu> parents = service.getValidParentsFor(menu.getId());
        model.addAttribute("parents", parents);
    }
}
