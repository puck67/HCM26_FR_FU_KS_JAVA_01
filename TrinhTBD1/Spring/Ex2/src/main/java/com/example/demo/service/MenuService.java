package com.example.demo.service;

import com.example.demo.service.base.GenericService;
import com.example.demo.model.Menu;

import java.util.List;

public interface MenuService extends GenericService<Menu, Long> {
    List<Menu> getRootMenus();
    List<Menu> getActiveRootMenus();
    List<Menu> getSidebarMenus(String role);
    void validateMenu(Menu menu);
    long countTotalMenus();
    long countParentMenus();
    long countSubMenus();
    List<Menu> getValidParentsFor(Long menuId);
}
