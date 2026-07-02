package com.lms.menumanager.repository;

import com.lms.menumanager.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // Retrieve all root menus ordered by display order
    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();

    // Count all root menus (parent is null)
    long countByParentIsNull();

    // Count all submenus (parent is not null)
    long countByParentIsNotNull();

    // Find all menus ordered by parent ID and display order
    List<Menu> findAllByOrderByParentIdAscDisplayOrderAsc();
}
