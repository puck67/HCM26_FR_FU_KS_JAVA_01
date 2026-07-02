package com.example.lms.config;

import com.example.lms.entity.Menu;
import com.example.lms.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    private final MenuService menuService;

    public GlobalControllerAdvice(MenuService menuService) {
        this.menuService = menuService;
    }

    @ModelAttribute("dynamicMenus")
    public List<Menu> populateMenus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                String role = authority.getAuthority();
                if (role.startsWith("ROLE_")) {
                    role = role.substring(5); // Remove ROLE_ prefix
                }
                log.debug("Fetching dynamic menus for authenticated role: {}", role);
                return menuService.getMenusByRole(role);
            }
        }
        return Collections.emptyList();
    }
}
