package com.menu.controller;

import com.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final MenuService menuService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalMenus", menuService.countAll());
        model.addAttribute("parentMenus", menuService.countParents());
        model.addAttribute("subMenus", menuService.countSubs());
        return "dashboard";
    }

    @GetMapping("/switch-role")
    public String switchRole(@RequestParam("role") String role, HttpSession session, HttpServletRequest request) {
        session.setAttribute("currentRole", role);
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }
}
