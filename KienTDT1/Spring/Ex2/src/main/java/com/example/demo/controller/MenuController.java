package com.example.demo.controller;

import com.example.demo.entity.Menu;
import com.example.demo.exception.MenuHasChildrenException;
import com.example.demo.service.MenuService;
import com.example.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;
    private final RoleService roleService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("menus", menuService.getAllMenus());
        return "menu-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        prepareFormModel(model, new Menu(), null);
        return "menu-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Menu menu = menuService.findById(id);
        if (menu == null) {
            return "redirect:/menus";
        }
        prepareFormModel(model, menu, id);
        return "menu-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Menu menu,
                       @RequestParam(required = false) Long parentId,
                       @RequestParam(required = false) Long[] roleIds) {
        List<Long> roleIdList = roleIds == null
                ? List.of()
                : Arrays.stream(roleIds).collect(Collectors.toList());
        menuService.save(menu, parentId, roleIdList);
        return "redirect:/menus";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            menuService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Menu deleted successfully.");
        } catch (MenuHasChildrenException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/menus";
    }

    private void prepareFormModel(Model model, Menu menu, Long excludeId) {
        model.addAttribute("menu", menu);
        model.addAttribute("parents", menuService.getAvailableParents(excludeId));
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("isEdit", menu.getId() != null);
    }
}
