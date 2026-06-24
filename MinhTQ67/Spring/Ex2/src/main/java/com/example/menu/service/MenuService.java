package com.example.menu.service;

import com.example.menu.entity.Menu;
import com.example.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> getSidebarMenus() {
        // Fetch only root menus; JPA will eagerly fetch children based on @OneToMany configuration
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }
    
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Optional<Menu> getMenuById(Integer id) {
        return menuRepository.findById(id);
    }

    public Menu saveMenu(Menu menu, Integer swapWithId) {
        // If parent ID is not provided (or empty form submit), ensure it's set to null
        if (menu.getParent() != null && menu.getParent().getId() == null) {
            menu.setParent(null);
        }
        
        if (swapWithId != null) {
            Optional<Menu> swapOpt = menuRepository.findById(swapWithId);
            if (swapOpt.isPresent()) {
                Menu swapMenu = swapOpt.get();
                if (menu.getId() != null) {
                    // Update: swap their orders
                    Optional<Menu> currentDbMenu = menuRepository.findById(menu.getId());
                    if (currentDbMenu.isPresent()) {
                        Integer oldOrder = currentDbMenu.get().getDisplayOrder();
                        swapMenu.setDisplayOrder(oldOrder);
                        menuRepository.save(swapMenu);
                    }
                } else {
                    // Create: push conflicting menu to the end
                    Integer parentId = menu.getParent() != null ? menu.getParent().getId() : null;
                    Integer maxOrder = parentId != null 
                        ? menuRepository.findMaxDisplayOrderByParentId(parentId) 
                        : menuRepository.findMaxDisplayOrderByParentIsNull();
                    swapMenu.setDisplayOrder(maxOrder + 1);
                    menuRepository.save(swapMenu);
                }
            }
        }
        
        return menuRepository.save(menu);
    }

    public void deleteMenu(Integer id) {
        Optional<Menu> menuOpt = menuRepository.findById(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                throw new IllegalStateException("Cannot delete menu because it has sub-menus. Please delete or reassign them first.");
            }
            menuRepository.delete(menu);
        }
    }

    // Dashboard Statistics
    public long getTotalMenus() {
        return menuRepository.count();
    }

    public long getTotalParentMenus() {
        return menuRepository.countByParentIsNull();
    }

    public long getTotalSubMenus() {
        return menuRepository.countByParentIsNotNull();
    }
    
    // Validate if display order is unique within the same parent level
    public boolean isOrderValid(Integer id, Integer parentId, Integer displayOrder) {
        if (id != null) {
            if (parentId != null) {
                return !menuRepository.existsByParentIdAndDisplayOrderAndIdNot(parentId, displayOrder, id);
            } else {
                return !menuRepository.existsByParentIsNullAndDisplayOrderAndIdNot(displayOrder, id);
            }
        } else {
            if (parentId != null) {
                return !menuRepository.existsByParentIdAndDisplayOrder(parentId, displayOrder);
            } else {
                return !menuRepository.existsByParentIsNullAndDisplayOrder(displayOrder);
            }
        }
    }
    
    public Optional<Menu> getConflictingMenu(Integer id, Integer parentId, Integer displayOrder) {
        Optional<Menu> conflict;
        if (parentId != null) {
            conflict = menuRepository.findByParentIdAndDisplayOrder(parentId, displayOrder);
        } else {
            conflict = menuRepository.findByParentIsNullAndDisplayOrder(displayOrder);
        }
        
        if (conflict.isPresent() && id != null && conflict.get().getId().equals(id)) {
            return Optional.empty(); // Same menu
        }
        return conflict;
    }
}
