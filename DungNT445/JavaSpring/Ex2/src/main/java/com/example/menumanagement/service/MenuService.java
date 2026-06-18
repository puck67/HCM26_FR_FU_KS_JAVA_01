package com.example.menumanagement.service;

import com.example.menumanagement.entity.Menu;
import java.util.List;

public interface MenuService {
    List<Menu> getAllRootMenus();
    List<Menu> getAllMenus();
    Menu getMenuById(Long id);
    Menu saveMenu(Menu menu);
    void deleteMenu(Long id) throws Exception;
    
    long getTotalMenus();
    long getTotalParentMenus();
    long getTotalSubMenus();
}
