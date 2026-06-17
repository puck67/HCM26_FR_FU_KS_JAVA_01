package com.menu.repository;

import com.menu.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    
    // Find all root menus (where parent_id is null) ordered by display order for management
    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();
    
    // Find active root menus for the sidebar
    List<Menu> findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();
}
