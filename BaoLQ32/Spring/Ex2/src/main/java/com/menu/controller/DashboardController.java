package com.menu.controller;

import com.menu.model.Menu;
import com.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Menu> allMenus = menuService.getAllMenus();
        
        long totalMenus = allMenus.size();
        long totalParentMenus = allMenus.stream().filter(m -> m.getParent() == null).count();
        long totalSubMenus = totalMenus - totalParentMenus;

        model.addAttribute("totalMenus", totalMenus);
        model.addAttribute("totalParentMenus", totalParentMenus);
        model.addAttribute("totalSubMenus", totalSubMenus);
        model.addAttribute("activePage", "dashboard");

        return "dashboard";
    }

    @GetMapping("/change-role")
    public String changeRole(@RequestParam String role, HttpSession session, HttpServletRequest request) {
        session.setAttribute("currentRole", role.toUpperCase());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/dashboard");
    }

    // Mock page mappings to demonstrate menu navigation links
    @GetMapping({"/students", "/lecturers", "/subjects", "/courses", "/courses/online", "/courses/offline", "/questions", "/exams", "/results", "/roles", "/users", "/my-courses"})
    public String mockPage(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("pageUri", uri);
        
        // Find menu corresponding to this URI to get its name
        String pageName = menuService.getAllMenus().stream()
                .filter(m -> uri.equals(m.getUrl()))
                .map(Menu::getName)
                .findFirst()
                .orElse("Mock Page");
                
        model.addAttribute("pageName", pageName);
        model.addAttribute("activePage", "mock");
        return "mock";
    }
}
