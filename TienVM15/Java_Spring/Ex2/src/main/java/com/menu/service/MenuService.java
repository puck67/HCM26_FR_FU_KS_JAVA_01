package com.menu.service;

import com.menu.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> findAll();
    List<Menu> getRootMenus();
    Optional<Menu> findById(Long id);
    Menu save(Menu menu);
    void deleteById(Long id);
    long countAll();
    long countParents();
    long countSubs();
    List<Menu> getFilteredRootMenus(String role);
    boolean isCyclic(Menu menu, Long targetParentId);
}
