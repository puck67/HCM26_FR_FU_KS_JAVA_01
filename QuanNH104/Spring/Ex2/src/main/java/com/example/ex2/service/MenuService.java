package com.example.ex2.service;

import com.example.ex2.entity.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> getAllMenus();

    List<Menu> getMenusForRole(String role);

    Menu saveMenu(Menu menu);

    long getTotalMenusCount();

    long getTotalParentMenusCount();

    long getTotalSubMenusCount();
}
