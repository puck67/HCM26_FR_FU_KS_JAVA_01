package com.training.menu.service;

import com.training.menu.entity.Menu;
import com.training.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getActiveRootMenus() {
        return menuRepository.findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getAllRootMenus() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getAllMenus() {
        return menuRepository.findAllByOrderByDisplayOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + id));
    }

    @Override
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Long id) {
        Menu menu = getMenuById(id);
        // Cascade delete: children are automatically deleted via CascadeType.ALL
        menuRepository.delete(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalMenus() {
        return menuRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalParentMenus() {
        return menuRepository.countByParentIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalSubMenus() {
        return menuRepository.countByParentIsNotNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> getPotentialParents(Long menuId) {
        if (menuId == null) {
            return menuRepository.findAllByOrderByDisplayOrderAsc();
        }
        return menuRepository.findPotentialParents(menuId);
    }
}
