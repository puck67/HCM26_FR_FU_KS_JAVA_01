package com.example.demo.repository;

import com.example.demo.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();
    List<Menu> findByParentIsNullAndStatusTrueOrderByDisplayOrderAsc();
    boolean existsByNameIgnoreCaseAndParent(String name, Menu parent);
    boolean existsByNameIgnoreCaseAndParentAndIdNot(String name, Menu parent, Long id);
    boolean existsByNameIgnoreCaseAndParentIsNull(String name);
    boolean existsByNameIgnoreCaseAndParentIsNullAndIdNot(String name, Long id);
}
