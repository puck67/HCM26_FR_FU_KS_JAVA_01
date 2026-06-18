package com.example.menumanagement.service.impl;

import com.example.menumanagement.entity.Menu;
import com.example.menumanagement.repository.MenuRepository;
import com.example.menumanagement.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAllRootMenus() {
        return menuRepository.findByParentIsNullOrderByDisplayOrderAsc();
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    @Override
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Long id) throws Exception {
        Menu menu = getMenuById(id);
        if (menu != null) {
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                throw new Exception("Cannot delete menu with active children. Please delete children first.");
            }
            menuRepository.delete(menu);
        }
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
}
