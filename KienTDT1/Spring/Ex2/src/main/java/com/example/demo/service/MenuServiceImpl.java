package com.example.demo.service;

import com.example.demo.dto.MenuView;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Role;
import com.example.demo.exception.MenuHasChildrenException;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MenuView> getSidebarMenus(String roleName) {
        List<Menu> roots = menuRepository.findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();
        roots.forEach(this::initializeChildren);

        return roots.stream()
                .map(menu -> toMenuView(menu, roleName))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(MenuView::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getAllMenus() {
        return menuRepository.findAllWithDetails();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getParentMenus() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getAvailableParents(Long excludeMenuId) {
        if (excludeMenuId == null) {
            return menuRepository.findAll().stream()
                    .sorted(Comparator.comparing(Menu::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                    .collect(Collectors.toList());
        }

        Set<Long> excludedIds = collectDescendantIds(excludeMenuId);
        excludedIds.add(excludeMenuId);

        return menuRepository.findAll().stream()
                .filter(menu -> !excludedIds.contains(menu.getId()))
                .sorted(Comparator.comparing(Menu::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Menu save(Menu menu, Long parentId, List<Long> roleIds) {
        if (parentId != null) {
            Menu parent = menuRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Parent menu not found"));
            menu.setParent(parent);
        } else {
            menu.setParent(null);
        }

        if (menu.getStatus() == null) {
            menu.setStatus(true);
        }

        if (menu.getDisplayOrder() == null) {
            menu.setDisplayOrder(0);
        }

        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = roleIds.stream()
                    .map(roleRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            menu.setRoles(roles);
        } else {
            menu.setRoles(new HashSet<>());
        }

        return menuRepository.save(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public Menu findById(Long id) {
        return menuRepository.findByIdWithDetails(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (menuRepository.existsByParentId(id)) {
            throw new MenuHasChildrenException(
                    "Cannot delete menu because it still has sub menus. Delete child menus first.");
        }
        menuRepository.deleteRoleLinks(id);
        menuRepository.deleteById(id);
    }

    @Override
    public long totalMenus() {
        return menuRepository.count();
    }

    @Override
    public long totalParentMenus() {
        return menuRepository.countByParentIsNull();
    }

    @Override
    public long totalSubMenus() {
        return menuRepository.countByParentIsNotNull();
    }

    private void initializeChildren(Menu menu) {
        menu.getChildren().size();
        if (menu.getRoles() != null) {
            menu.getRoles().size();
        }
        menu.getChildren().forEach(this::initializeChildren);
    }

    private MenuView toMenuView(Menu menu, String roleName) {
        List<MenuView> childViews = menu.getChildren().stream()
                .filter(child -> Boolean.TRUE.equals(child.getStatus()))
                .map(child -> toMenuView(child, roleName))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(MenuView::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());

        boolean accessible = hasRoleAccess(menu, roleName);

        if (!accessible && childViews.isEmpty()) {
            return null;
        }

        MenuView view = new MenuView();
        view.setId(menu.getId());
        view.setName(menu.getName());
        view.setUrl(normalizeUrl(menu.getUrl()));
        view.setIcon(menu.getIcon());
        view.setDisplayOrder(menu.getDisplayOrder());
        view.setChildren(childViews);
        return view;
    }

    private String normalizeUrl(String url) {
        if (url == null || url.isBlank() || "#".equals(url.trim())) {
            return null;
        }
        return url;
    }

    private boolean hasRoleAccess(Menu menu, String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return true;
        }
        if (menu.getRoles() == null || menu.getRoles().isEmpty()) {
            return true;
        }
        return menu.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    private Set<Long> collectDescendantIds(Long menuId) {
        Set<Long> ids = new HashSet<>();
        collectDescendantIdsRecursive(menuId, ids);
        return ids;
    }

    private void collectDescendantIdsRecursive(Long menuId, Set<Long> ids) {
        List<Menu> children = menuRepository.findByParentIdOrderByDisplayOrderAsc(menuId);
        for (Menu child : children) {
            ids.add(child.getId());
            collectDescendantIdsRecursive(child.getId(), ids);
        }
    }
}
