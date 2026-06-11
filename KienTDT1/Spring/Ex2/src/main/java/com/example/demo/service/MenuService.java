package com.example.demo.service;

import com.example.demo.dto.MenuView;
import com.example.demo.entity.Menu;

import java.util.List;

public interface MenuService {

    List<MenuView> getSidebarMenus(String roleName);

    List<Menu> getAllMenus();

    List<Menu> getParentMenus();

    List<Menu> getAvailableParents(Long excludeMenuId);

    Menu save(Menu menu, Long parentId, List<Long> roleIds);

    Menu findById(Long id);

    void delete(Long id);

    long totalMenus();

    long totalParentMenus();

    long totalSubMenus();
}
