package fa.training.ex2dynamicmenumanagementsystem.controllers;

import fa.training.ex2dynamicmenumanagementsystem.dto.request.CreateMenuRequest;
import fa.training.ex2dynamicmenumanagementsystem.dto.request.UpdateMenuRequest;
import fa.training.ex2dynamicmenumanagementsystem.entities.Menu;
import fa.training.ex2dynamicmenumanagementsystem.services.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @ModelAttribute("sidebarMenus")
    public List<Menu> sidebarMenus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ArrayList<>();
        }
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return menuService.getMenuTreeForUser(roles);
    }

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("currentUser")
    public String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "Anonymous";
    }

    @ModelAttribute("currentRoles")
    public List<String> currentRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return List.of();
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    @GetMapping({ "/", "/dashboard" })
    public String dashboard(Model model) {
        model.addAttribute("totalMenus", menuService.countTotalMenus());
        model.addAttribute("totalParentMenus", menuService.countTotalParentMenus());
        model.addAttribute("totalSubMenus", menuService.countTotalSubMenus());
        return "dashboard";
    }

    @GetMapping({ "/students", "/lecturers", "/subjects", "/courses", "/courses/online", "/courses/offline", "/roles",
            "/users", "/my-courses" })
    public String genericPage(HttpServletRequest request, Model model) {
        model.addAttribute("pageTitle", request.getRequestURI());
        return "page";
    }

    @GetMapping("/admin/menus")
    public String listMenus(Model model) {
        model.addAttribute("menus", menuService.getAllMenus());
        return "menu-list";
    }

    @GetMapping("/admin/menus/new")
    public String newMenuForm(Model model) {
        model.addAttribute("menuRequest", new UpdateMenuRequest());
        model.addAttribute("allMenus", menuService.getAllMenus());
        return "menu-form";
    }

    @PostMapping("/admin/menus/new")
    public String saveMenu(@ModelAttribute("menuRequest") UpdateMenuRequest request) {
        CreateMenuRequest createReq = new CreateMenuRequest();
        createReq.setName(request.getName());
        createReq.setUrl(request.getUrl());
        createReq.setIcon(request.getIcon());
        createReq.setParentId(request.getParentId());
        createReq.setDisplayOrder(request.getDisplayOrder());
        createReq.setStatus(request.getStatus());
        createReq.setRoles(request.getRoles());
        menuService.createMenu(createReq);
        return "redirect:/admin/menus";
    }

    @GetMapping("/admin/menus/edit/{id}")
    public String editMenuForm(@PathVariable("id") Long id, Model model) {
        Menu menu = menuService.getMenuById(id);
        if (menu == null) {
            return "redirect:/admin/menus";
        }

        UpdateMenuRequest request = new UpdateMenuRequest();
        request.setId(menu.getId());
        request.setName(menu.getName());
        request.setUrl(menu.getUrl());
        request.setIcon(menu.getIcon());
        request.setParentId(menu.getParent() != null ? menu.getParent().getId() : null);
        request.setDisplayOrder(menu.getDisplayOrder());
        request.setStatus(menu.getStatus());
        request.setRoles(menu.getRoles());

        model.addAttribute("menuRequest", request);
        model.addAttribute("allMenus", menuService.getAllMenus());
        return "menu-form";
    }

    @PostMapping("/admin/menus/edit")
    public String updateMenu(@ModelAttribute("menuRequest") UpdateMenuRequest request) {
        menuService.updateMenu(request);
        return "redirect:/admin/menus";
    }

    @GetMapping("/admin/menus/delete/{id}")
    public String deleteMenu(@PathVariable("id") Long id) {
        menuService.deleteMenu(id);
        return "redirect:/admin/menus";
    }
}
