package com.example.lms.controller;

import com.example.lms.entity.Menu;
import com.example.lms.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    private final MenuService menuService;

    @GetMapping
    public String listAllMenus(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        log.info("Request to list all menus: page={}, size={}", page, size);
        Page<Menu> menuPage = menuService.getAllMenus(PageRequest.of(page, size));
        model.addAttribute("menus", menuPage.getContent());
        model.addAttribute("page", menuPage);
        return "menu_list";
    }

    @PostMapping("/new")
    public String addNewMenu(@ModelAttribute Menu menu) {
        log.info("Request to save new menu: title={}, url={}", menu.getTitle(), menu.getUrl());
        menuService.saveMenu(menu);
        return "redirect:/menus";
    }

    @PostMapping("/edit/{id}")
    public String updateExistingMenu(@PathVariable Long id, @ModelAttribute Menu menu) {
        log.info("Request to edit menu: id={}", id);
        menu.setId(id);
        menuService.saveMenu(menu);
        return "redirect:/menus";
    }

    @PostMapping("/delete/{id}")
    public String removeMenuById(@PathVariable Long id) {
        log.info("Request to delete menu: id={}", id);
        menuService.deleteMenu(id);
        return "redirect:/menus";
    }
}
