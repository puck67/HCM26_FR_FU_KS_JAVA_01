package com.example.demo.service.impl;

import com.example.demo.service.base.GenericServiceImpl;
import com.example.demo.model.Menu;
import com.example.demo.repository.MenuRepository;
import com.example.demo.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuServiceImpl extends GenericServiceImpl<Menu, Long, MenuRepository> implements MenuService {

    public MenuServiceImpl(MenuRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getRootMenus() {
        return repository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getActiveRootMenus() {
        return repository.findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getSidebarMenus(String role) {
        List<Menu> activeRoots = repository.findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();
        return activeRoots.stream()
                .filter(menu -> hasRole(menu, role))
                .map(menu -> filterMenuForRole(menu, role))
                .collect(Collectors.toList());
    }

    private boolean hasRole(Menu menu, String role) {
        if (menu.getRoles() == null || menu.getRoles().trim().isEmpty()) {
            return true;
        }
        String upperRole = role.trim().toUpperCase();
        return Arrays.stream(menu.getRoles().split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .anyMatch(r -> r.equals(upperRole));
    }

    private Menu filterMenuForRole(Menu menu, String role) {
        Menu copy = new Menu();
        copy.setId(menu.getId());
        copy.setName(menu.getName());
        copy.setUrl(menu.getUrl());
        copy.setIcon(menu.getIcon());
        copy.setDisplayOrder(menu.getDisplayOrder());
        copy.setStatus(menu.getStatus());
        copy.setRoles(menu.getRoles());

        List<Menu> filteredChildren = menu.getChildren().stream()
                .filter(child -> child.getStatus() && hasRole(child, role))
                .map(child -> filterMenuForRole(child, role))
                .collect(Collectors.toList());
        copy.setChildren(filteredChildren);
        return copy;
    }

    @Override
    public void validateMenu(Menu menu) {
        if (menu.getName() == null || menu.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Menu name cannot be empty");
        }

        boolean nameExists;
        if (menu.getId() == null) {
            if (menu.getParent() == null) {
                nameExists = repository.existsByNameIgnoreCaseAndParentIsNull(menu.getName().trim());
            } else {
                nameExists = repository.existsByNameIgnoreCaseAndParent(menu.getName().trim(), menu.getParent());
            }
        } else {
            if (menu.getParent() == null) {
                nameExists = repository.existsByNameIgnoreCaseAndParentIsNullAndIdNot(menu.getName().trim(), menu.getId());
            } else {
                nameExists = repository.existsByNameIgnoreCaseAndParentAndIdNot(menu.getName().trim(), menu.getParent(), menu.getId());
            }
        }

        if (nameExists) {
            StringBuilder sb = new StringBuilder();
            sb.append("A menu with the name '")
              .append(menu.getName().trim())
              .append("' already exists under the ")
              .append(menu.getParent() == null ? "root level" : "same parent menu");
            throw new IllegalArgumentException(sb.toString());
        }

        if (menu.getId() != null && menu.getParent() != null) {
            if (Objects.equals(menu.getId(), menu.getParent().getId())) {
                throw new IllegalArgumentException("A menu cannot be its own parent.");
            }

            if (isDescendant(menu.getParent(), menu)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot set parent to '")
                  .append(menu.getParent().getName())
                  .append("' because it would introduce a cycle dependency (the parent is a child/descendant of this menu).");
                throw new IllegalArgumentException(sb.toString());
            }
        }
    }

    private boolean isDescendant(Menu parent, Menu child) {
        if (parent == null || child == null) {
            return false;
        }
        Menu current = parent;
        while (current != null) {
            if (current.getId() != null && Objects.equals(current.getId(), child.getId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalMenus() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countParentMenus() {
        return repository.findAll().stream()
                .filter(menu -> menu.getParent() == null)
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countSubMenus() {
        return repository.findAll().stream()
                .filter(menu -> menu.getParent() != null)
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getValidParentsFor(Long menuId) {
        List<Menu> allMenus = repository.findAll();
        if (menuId == null) {
            return allMenus;
        }
        Menu currentMenu = repository.findById(menuId).orElse(null);
        if (currentMenu == null) {
            return allMenus;
        }
        return allMenus.stream()
                .filter(menu -> !Objects.equals(menu.getId(), menuId) && !isDescendant(menu, currentMenu))
                .collect(Collectors.toList());
    }
}
