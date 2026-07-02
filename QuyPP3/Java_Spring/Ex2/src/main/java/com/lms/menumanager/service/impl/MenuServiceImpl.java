package com.lms.menumanager.service.impl;

import com.lms.menumanager.entity.Menu;
import com.lms.menumanager.repository.MenuRepository;
import com.lms.menumanager.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAllByOrderByParentIdAscDisplayOrderAsc();
    }

    @Override
    public List<Menu> getRootMenus() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    @Override
    public List<Menu> getMenuTreeForRole(String role) {
        List<Menu> rootMenus = getRootMenus();
        List<Menu> filteredTree = new ArrayList<>();
        
        for (Menu root : rootMenus) {
            Menu copy = copyAndFilterMenu(root, role);
            if (copy != null) {
                filteredTree.add(copy);
            }
        }
        return filteredTree;
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Menu saveMenu(Menu menu) {
        // Clear children association if parent is being modified to prevent cyclic references
        if (menu.getParent() != null && menu.getParent().getId() != null) {
            Menu parent = menuRepository.findById(menu.getParent().getId()).orElse(null);
            menu.setParent(parent);
        } else {
            menu.setParent(null);
        }
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void deleteMenu(Long id, boolean cascade) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found with ID: " + id));

        if (!cascade && menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot delete menu because it has active submenus.");
        }

        // Deassociate from parent before deleting to prevent constraint issues
        if (menu.getParent() != null) {
            menu.getParent().getChildren().remove(menu);
            menu.setParent(null);
        }

        menuRepository.delete(menu);
    }

    @Override
    public long getTotalMenus() {
        return menuRepository.count();
    }

    @Override
    public long getTotalParentMenus() {
        return menuRepository.countByParentIsNull();
    }

    @Override
    public long getTotalSubMenus() {
        return menuRepository.countByParentIsNotNull();
    }

    // Helper method to recursively filter and duplicate nodes to prevent Hibernate Session pollution
    private Menu copyAndFilterMenu(Menu menu, String role) {
        if (!menu.getStatus() || !isRoleAllowed(menu.getRoles(), role)) {
            return null;
        }

        Menu copy = new Menu();
        copy.setId(menu.getId());
        copy.setName(menu.getName());
        copy.setUrl(menu.getUrl());
        copy.setIcon(menu.getIcon());
        copy.setDisplayOrder(menu.getDisplayOrder());
        copy.setStatus(menu.getStatus());
        copy.setRoles(menu.getRoles());

        List<Menu> filteredChildren = new ArrayList<>();
        if (menu.getChildren() != null) {
            for (Menu child : menu.getChildren()) {
                Menu childCopy = copyAndFilterMenu(child, role);
                if (childCopy != null) {
                    childCopy.setParent(copy);
                    filteredChildren.add(childCopy);
                }
            }
        }
        copy.setChildren(filteredChildren);
        return copy;
    }

    private boolean isRoleAllowed(String rolesStr, String role) {
        if (role == null || role.trim().isEmpty()) {
            return true; // if no active role context, allow all
        }
        if (rolesStr == null || rolesStr.trim().isEmpty()) {
            return true; // if menu has no role restriction, it is public
        }
        String[] allowedRoles = rolesStr.split(",");
        for (String r : allowedRoles) {
            if (r.trim().equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }
}
