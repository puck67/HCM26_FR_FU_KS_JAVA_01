package com.training.menu.repository;

import com.training.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * Find all active root menus (no parent) ordered by display_order.
     * Used for rendering the sidebar.
     */
    List<Menu> findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();

    /**
     * Find all root menus (no parent) ordered by display_order.
     * Used for menu management list.
     */
    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();

    /**
     * Find all menus ordered by display_order.
     */
    List<Menu> findAllByOrderByDisplayOrderAsc();

    /**
     * Count root menus (parent menus).
     */
    long countByParentIsNull();

    /**
     * Count sub menus (menus that have a parent).
     */
    long countByParentIsNotNull();

    /**
     * Find potential parent menus (exclude self and descendants to prevent circular reference).
     */
    @Query("SELECT m FROM Menu m WHERE m.id <> :menuId ORDER BY m.displayOrder ASC")
    List<Menu> findPotentialParents(Long menuId);
}
