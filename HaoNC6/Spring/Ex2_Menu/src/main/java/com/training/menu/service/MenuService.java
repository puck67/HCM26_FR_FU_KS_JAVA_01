package com.training.menu.service;

import com.training.menu.entity.Menu;

import java.util.List;

public interface MenuService {

    /**
     * Get active root menus with children for sidebar rendering.
     */
    List<Menu> getActiveRootMenus();

    /**
     * Get all root menus for management view.
     */
    List<Menu> getAllRootMenus();

    /**
     * Get all menus as a flat list.
     */
    List<Menu> getAllMenus();

    /**
     * Get a menu by its ID.
     */
    Menu getMenuById(Long id);

    /**
     * Save or update a menu.
     */
    Menu saveMenu(Menu menu);

    /**
     * Delete a menu by ID (cascade deletes children).
     */
    void deleteMenu(Long id);

    /**
     * Get total number of menus.
     */
    long getTotalMenus();

    /**
     * Get total number of parent (root) menus.
     */
    long getTotalParentMenus();

    /**
     * Get total number of sub menus.
     */
    long getTotalSubMenus();

    /**
     * Get potential parent menus for a given menu (excludes self).
     */
    List<Menu> getPotentialParents(Long menuId);
}
