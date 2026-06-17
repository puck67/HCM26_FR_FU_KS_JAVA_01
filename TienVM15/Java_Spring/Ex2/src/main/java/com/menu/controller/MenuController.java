package com.menu.controller;

import com.menu.entity.Menu;
import com.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("allMenus", menuService.findAll());
        return "menu-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("potentialParents", menuService.findAll());
        return "menu-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Optional<Menu> menuOpt = menuService.findById(id);
        if (menuOpt.isPresent()) {
            model.addAttribute("menu", menuOpt.get());
            // Exclude itself from potential parents
            List<Menu> potentialParents = menuService.findAll().stream()
                    .filter(m -> !m.getId().equals(id))
                    .toList();
            model.addAttribute("potentialParents", potentialParents);
            return "menu-form";
        }
        return "redirect:/menus";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("menu") Menu menu,
                       BindingResult result,
                       @RequestParam(value = "parentId", required = false) Long parentId,
                       Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("potentialParents", menuService.findAll());
            return "menu-form";
        }

        // Cycle check
        if (parentId != null && menuService.isCyclic(menu, parentId)) {
            model.addAttribute("errorMessage", "Cannot select self or a child menu as parent (Circular Reference detected)!");
            model.addAttribute("potentialParents", menuService.findAll().stream()
                    .filter(m -> menu.getId() == null || !m.getId().equals(menu.getId()))
                    .toList());
            return "menu-form";
        }

        // Set parent
        if (parentId != null) {
            menuService.findById(parentId).ifPresent(menu::setParent);
        } else {
            menu.setParent(null);
        }

        menuService.save(menu);
        return "redirect:/menus";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        menuService.deleteById(id);
        return "redirect:/menus";
    }
}
