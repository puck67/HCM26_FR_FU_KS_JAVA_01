package com.example.lms.controller;

import com.example.lms.entity.Menu;
import com.example.lms.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menus")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public String listMenus(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Page<Menu> menuPage = menuService.getAllMenus(PageRequest.of(page, size));
        model.addAttribute("menus", menuPage.getContent());
        model.addAttribute("page", menuPage);
        return "menu_list";
    }

    @PostMapping("/new")
    public String saveMenu(@ModelAttribute Menu menu) {
        menuService.saveMenu(menu);
        return "redirect:/menus";
    }

    @PostMapping("/edit/{id}")
    public String saveEditMenu(@PathVariable Long id, @ModelAttribute Menu menu) {
        menu.setId(id);
        menuService.saveMenu(menu);
        return "redirect:/menus";
    }

    @PostMapping("/delete/{id}")
    public String deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return "redirect:/menus";
    }
}
