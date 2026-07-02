package com.lms.menumanager.service;

import com.lms.menumanager.entity.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> getAllMenus();

    List<Menu> getRootMenus();

    List<Menu> getMenuTreeForRole(String role);

    Menu getMenuById(Long id);

    Menu saveMenu(Menu menu);

    void deleteMenu(Long id, boolean cascade);

    long getTotalMenus();

    long getTotalParentMenus();

    long getTotalSubMenus();
}
