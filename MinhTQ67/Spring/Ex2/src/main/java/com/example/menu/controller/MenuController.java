package com.example.menu.controller;

import com.example.menu.entity.Menu;
import com.example.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@Controller
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public String listMenus(Model model) {
        model.addAttribute("menus", menuService.getAllMenus());
        return "menu/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("parentMenus", menuService.getAllMenus()); // Using all menus as potential parents
        return "menu/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Menu> menuOpt = menuService.getMenuById(id);
        if (menuOpt.isPresent()) {
            model.addAttribute("menu", menuOpt.get());
            model.addAttribute("parentMenus", menuService.getAllMenus());
            return "menu/form";
        }
        redirectAttributes.addFlashAttribute("error", "Menu not found");
        return "redirect:/menus";
    }

    @PostMapping("/save")
    public String saveMenu(@ModelAttribute Menu menu, 
                           @RequestParam(required = false) Integer swapWithId,
                           RedirectAttributes redirectAttributes) {
        menuService.saveMenu(menu, swapWithId);
        redirectAttributes.addFlashAttribute("success", "Menu saved successfully");
        return "redirect:/menus";
    }

    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            menuService.deleteMenu(id);
            redirectAttributes.addFlashAttribute("success", "Menu deleted successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/menus";
    }

    @GetMapping("/check-order")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkOrder(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer parentId,
            @RequestParam Integer displayOrder) {
        boolean isValid = menuService.isOrderValid(id, parentId, displayOrder);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", isValid);
        if (!isValid) {
            Optional<Menu> conflict = menuService.getConflictingMenu(id, parentId, displayOrder);
            if (conflict.isPresent()) {
                response.put("conflictMenuId", conflict.get().getId());
                response.put("conflictMenuName", conflict.get().getName());
            }
        }
        return ResponseEntity.ok(response);
    }
}
