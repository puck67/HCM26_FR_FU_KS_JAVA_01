package com.example.Ex2.service;

import com.example.Ex2.model.Menu;
import com.example.Ex2.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public List<Menu> getTopLevelMenus() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    public List<Menu> getActiveTopLevelMenus() {
        return menuRepository.findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();
    }

    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    public Menu saveMenu(Menu menu) {
        // Validation to prevent circular relationship
        if (menu.getId() != null && menu.getParent() != null) {
            if (!isValidParent(menu.getId(), menu.getParent().getId())) {
                throw new IllegalArgumentException("Cannot set parent that causes circular dependency!");
            }
        }
        return menuRepository.save(menu);
    }

    public void deleteMenu(Long id, String deleteOption) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found with id: " + id));

        if ("RESTRICT".equalsIgnoreCase(deleteOption)) {
            if (!menu.getChildren().isEmpty()) {
                throw new IllegalStateException("Cannot delete menu. It contains " 
                        + menu.getChildren().size() + " sub-menus. Choose Cascade Delete to remove them all.");
            }
        }

        // JPA CascadeType.ALL handles the cascade delete automatically when we delete the parent menu
        menuRepository.delete(menu);
    }

    /**
     * Checks if parentId is a valid parent for menuId to avoid circular dependency.
     */
    public boolean isValidParent(Long menuId, Long parentId) {
        if (menuId == null) {
            return true; // New menu has no circular dependencies
        }
        if (parentId == null) {
            return true;
        }
        if (menuId.equals(parentId)) {
            return false;
        }

        // Trace parentId's ancestors to see if menuId is one of them
        Menu current = menuRepository.findById(parentId).orElse(null);
        while (current != null) {
            if (current.getId().equals(menuId)) {
                return false; // Loop detected
            }
            current = current.getParent();
        }
        return true;
    }

    /**
     * Checks if a menu item should be visible based on its status and roles.
     */
    public boolean isMenuVisible(Menu menu, String role) {
        if (!menu.isStatus()) {
            return false;
        }
        if (role == null || role.isEmpty()) {
            return true;
        }
        if (menu.getRoles() != null && !menu.getRoles().trim().isEmpty()) {
            String[] roles = menu.getRoles().split(",");
            for (String r : roles) {
                if (r.trim().equalsIgnoreCase(role)) {
                    return true;
                }
            }
            return false; // Not in allowed roles
        }
        return true; // No roles defined means public
    }

    // Dashboard calculations
    public long getTotalMenusCount() {
        return menuRepository.count();
    }

    public long getParentMenusCount() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc().size();
    }

    public long getSubMenusCount() {
        return menuRepository.count() - getParentMenusCount();
    }

    public List<Menu> getFlattenedMenus() {
        List<Menu> result = new ArrayList<>();
        List<Menu> topMenus = menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
        flattenMenus(topMenus, "", result);
        return result;
    }

    private void flattenMenus(List<Menu> menus, String indent, List<Menu> result) {
        for (Menu m : menus) {
            m.setDisplayName(indent + (indent.isEmpty() ? "" : "└── ") + m.getName());
            result.add(m);
            flattenMenus(m.getChildren(), indent + "    ", result);
        }
    }

    public List<Menu> getAvailableParentsFor(Long menuId) {
        List<Menu> allFlat = getFlattenedMenus();
        if (menuId == null) {
            return allFlat;
        }
        allFlat.removeIf(m -> isDescendantOfOrSelf(m, menuId));
        return allFlat;
    }

    private boolean isDescendantOfOrSelf(Menu m, Long ancestorId) {
        if (m.getId().equals(ancestorId)) {
            return true;
        }
        Menu parent = m.getParent();
        while (parent != null) {
            if (parent.getId().equals(ancestorId)) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }
}
