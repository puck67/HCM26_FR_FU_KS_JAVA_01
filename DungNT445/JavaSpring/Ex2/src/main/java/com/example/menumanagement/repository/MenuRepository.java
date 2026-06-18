package com.example.menumanagement.repository;

import com.example.menumanagement.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();
    long countByParentIsNull();
    long countByParentIsNotNull();
}
