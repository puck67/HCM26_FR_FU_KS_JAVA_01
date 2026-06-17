package fa.training.ex2dynamicmenumanagementsystem.services.impl;

import fa.training.ex2dynamicmenumanagementsystem.dto.request.CreateMenuRequest;
import fa.training.ex2dynamicmenumanagementsystem.dto.request.UpdateMenuRequest;
import fa.training.ex2dynamicmenumanagementsystem.entities.Menu;
import fa.training.ex2dynamicmenumanagementsystem.repositories.MenuRepository;
import fa.training.ex2dynamicmenumanagementsystem.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public List<Menu> getMenuTreeForUser(List<String> userRoles) {
        List<Menu> roots = menuRepository.findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();
        return filterMenusByRoles(roots, userRoles);
    }

    private List<Menu> filterMenusByRoles(List<Menu> menus, List<String> userRoles) {
        List<Menu> filtered = new ArrayList<>();
        for (Menu m : menus) {
            if (hasAccess(m, userRoles)) {
                Menu copy = Menu.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .url(m.getUrl())
                        .icon(m.getIcon())
                        .displayOrder(m.getDisplayOrder())
                        .status(m.getStatus())
                        .roles(m.getRoles())
                        .parent(m.getParent())
                        .build();
                
                if (m.getChildren() != null && !m.getChildren().isEmpty()) {
                    List<Menu> filteredChildren = filterMenusByRoles(m.getChildren(), userRoles);
                    copy.setChildren(filteredChildren);
                } else {
                    copy.setChildren(new ArrayList<>());
                }
                filtered.add(copy);
            }
        }
        return filtered;
    }

    private boolean hasAccess(Menu menu, List<String> userRoles) {
        if (menu.getRoles() == null || menu.getRoles().trim().isEmpty()) {
            return true;
        }
        String[] allowedRoles = menu.getRoles().split(",");
        for (String r : allowedRoles) {
            String cleanRole = r.trim().toUpperCase();
            for (String userRole : userRoles) {
                String cleanUserRole = userRole.trim().toUpperCase();
                if (cleanUserRole.equals(cleanRole) || 
                    cleanUserRole.equals("ROLE_" + cleanRole) || 
                    ("ROLE_" + cleanUserRole).equals(cleanRole)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public List<Menu> getAllRootMenus() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public Menu createMenu(CreateMenuRequest request) {
        Menu parent = null;
        if (request.getParentId() != null) {
            parent = menuRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent menu not found"));
        }
        Menu menu = Menu.builder()
                .name(request.getName())
                .url(request.getUrl())
                .icon(request.getIcon())
                .parent(parent)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .status(request.getStatus() != null ? request.getStatus() : true)
                .roles(request.getRoles())
                .build();
        return menuRepository.save(menu);
    }

    @Override
    public Menu updateMenu(UpdateMenuRequest request) {
        Menu menu = menuRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));
        
        Menu parent = null;
        if (request.getParentId() != null) {
            if (request.getParentId().equals(menu.getId())) {
                throw new IllegalArgumentException("A menu cannot be its own parent");
            }
            parent = menuRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent menu not found"));
        }
        
        menu.setName(request.getName());
        menu.setUrl(request.getUrl());
        menu.setIcon(request.getIcon());
        menu.setParent(parent);
        menu.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        menu.setStatus(request.getStatus() != null ? request.getStatus() : true);
        menu.setRoles(request.getRoles());
        
        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public long countTotalMenus() {
        return menuRepository.count();
    }

    @Override
    public long countTotalParentMenus() {
        return menuRepository.countByParentIsNull();
    }

    @Override
    public long countTotalSubMenus() {
        return menuRepository.countByParentIsNotNull();
    }
}
