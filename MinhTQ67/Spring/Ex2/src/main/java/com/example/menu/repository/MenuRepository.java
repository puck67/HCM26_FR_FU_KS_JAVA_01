package com.example.menu.repository;

import com.example.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    
    // Find all root menus (menus with no parent)
    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();
    
    // Count total parent menus
    long countByParentIsNull();
    
    // Count total sub menus
    long countByParentIsNotNull();
    
    // Validation methods for display order
    boolean existsByParentIsNullAndDisplayOrderAndIdNot(Integer displayOrder, Integer id);
    boolean existsByParentIsNullAndDisplayOrder(Integer displayOrder);
    boolean existsByParentIdAndDisplayOrderAndIdNot(Integer parentId, Integer displayOrder, Integer id);
    boolean existsByParentIdAndDisplayOrder(Integer parentId, Integer displayOrder);
    
    // Fetch conflicting menu
    Optional<Menu> findByParentIsNullAndDisplayOrder(Integer displayOrder);
    Optional<Menu> findByParentIdAndDisplayOrder(Integer parentId, Integer displayOrder);
    
    // Get max display order
    @Query("SELECT COALESCE(MAX(m.displayOrder), 0) FROM Menu m WHERE m.parent IS NULL")
    Integer findMaxDisplayOrderByParentIsNull();
    
    @Query("SELECT COALESCE(MAX(m.displayOrder), 0) FROM Menu m WHERE m.parent.id = :parentId")
    Integer findMaxDisplayOrderByParentId(@Param("parentId") Integer parentId);
}
