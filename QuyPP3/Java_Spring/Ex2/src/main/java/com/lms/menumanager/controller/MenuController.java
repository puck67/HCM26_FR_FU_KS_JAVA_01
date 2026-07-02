package com.lms.menumanager.controller;

import com.lms.menumanager.entity.Menu;
import com.lms.menumanager.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @ModelAttribute
    public void addCommonAttributes(Model model, HttpSession session) {
        String currentRole = (String) session.getAttribute("currentRole");
        if (currentRole == null) {
            currentRole = "ADMIN";
            session.setAttribute("currentRole", currentRole);
        }
        model.addAttribute("currentRole", currentRole);
        model.addAttribute("sidebarMenus", menuService.getMenuTreeForRole(currentRole));
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalMenus", menuService.getTotalMenus());
        model.addAttribute("totalParentMenus", menuService.getTotalParentMenus());
        model.addAttribute("totalSubMenus", menuService.getTotalSubMenus());
        model.addAttribute("allMenus", menuService.getAllMenus());
        return "dashboard";
    }

    @GetMapping("/switch-role")
    public String switchRole(@RequestParam String role, HttpServletRequest request) {
        request.getSession().setAttribute("currentRole", role.toUpperCase());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    // Menu Management List
    @GetMapping("/menus")
    public String listMenus(Model model) {
        model.addAttribute("menus", menuService.getAllMenus());
        return "menus/menu-list";
    }

    // New Menu Form
    @GetMapping("/menus/new")
    public String showCreateForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("potentialParents", menuService.getAllMenus());
        return "menus/menu-form";
    }

    // Save/Update Menu
    @PostMapping("/menus/save")
    public String saveMenu(@Valid @ModelAttribute("menu") Menu menu, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("potentialParents", menuService.getAllMenus());
            return "menus/menu-form";
        }
        
        // Prevent circular reference: a menu cannot be its own parent
        if (menu.getId() != null && menu.getParent() != null && menu.getId().equals(menu.getParent().getId())) {
            result.rejectValue("parent", "error.menu", "A menu cannot be its own parent.");
            model.addAttribute("potentialParents", menuService.getAllMenus());
            return "menus/menu-form";
        }

        // Prevent indirect circular reference
        if (menu.getId() != null && menu.getParent() != null && isDescendantOrSelf(menu.getParent(), menu.getId())) {
            result.rejectValue("parent", "error.menu", "Circular hierarchy detected! This parent is a child of the current menu.");
            model.addAttribute("potentialParents", menuService.getAllMenus());
            return "menus/menu-form";
        }

        menuService.saveMenu(menu);
        redirectAttributes.addFlashAttribute("successMessage", "Menu saved successfully!");
        return "redirect:/menus";
    }

    // Edit Menu Form
    @GetMapping("/menus/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Menu menu = menuService.getMenuById(id);
        if (menu == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Menu not found.");
            return "redirect:/menus";
        }
        model.addAttribute("menu", menu);
        
        // Exclude current menu and its descendants from potential parents to avoid circular loops
        List<Menu> potentialParents = menuService.getAllMenus().stream()
                .filter(m -> !isDescendantOrSelf(m, id))
                .collect(Collectors.toList());
                
        model.addAttribute("potentialParents", potentialParents);
        return "menus/menu-form";
    }

    // Check if menu is a descendant of the target parent id
    private boolean isDescendantOrSelf(Menu m, Long targetId) {
        if (m == null) {
            return false;
        }
        if (m.getId().equals(targetId)) {
            return true;
        }
        return m.getParent() != null && isDescendantOrSelf(m.getParent(), targetId);
    }

    // Delete Menu
    @PostMapping("/menus/delete/{id}")
    public String deleteMenu(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean cascade, RedirectAttributes redirectAttributes) {
        try {
            menuService.deleteMenu(id, cascade);
            redirectAttributes.addFlashAttribute("successMessage", "Menu deleted successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("showCascadePrompt", id); // Signal to UI to show cascade prompt
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting menu: " + e.getMessage());
        }
        return "redirect:/menus";
    }

    // Mock Endpoints for Sidebar Links
    @GetMapping({
        "/students", "/lecturers", "/subjects", "/courses", 
        "/courses/online", "/courses/offline", "/my-courses",
        "/questions", "/exams", "/results", "/roles", "/users"
    })
    public String mockPage(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        String title = uri.substring(1).replace("/", " ── ");
        title = Character.toUpperCase(title.charAt(0)) + title.substring(1);
        
        model.addAttribute("pageTitle", title);
        model.addAttribute("pageUri", uri);
        return "mock-page";
    }
}
