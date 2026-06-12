package com.example.demo.repository;

import com.example.demo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();

    List<Menu> findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();

    List<Menu> findByParentIdOrderByDisplayOrderAsc(Long parentId);

    boolean existsByParentId(Long parentId);

    long countByParentIsNull();

    long countByParentIsNotNull();

    @Query("SELECT DISTINCT m FROM Menu m LEFT JOIN FETCH m.parent LEFT JOIN FETCH m.roles ORDER BY m.displayOrder ASC")
    List<Menu> findAllWithDetails();

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.parent LEFT JOIN FETCH m.roles WHERE m.id = :id")
    Optional<Menu> findByIdWithDetails(Long id);

    @Modifying
    @Query(value = "DELETE FROM menu_role WHERE menu_id = :menuId", nativeQuery = true)
    void deleteRoleLinks(@Param("menuId") Long menuId);
}
