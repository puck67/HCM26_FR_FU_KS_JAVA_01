package com.example.ex2.repository;

import com.example.ex2.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // Tìm tất cả các menu cha cấp 1, được sắp xếp theo thứ tự hiển thị
    List<Menu> findByParentIsNullOrderByDisplayOrderAsc();

    // Đếm tổng số menu cha (parent_id IS NULL)
    long countByParentIsNull();

    // Đếm tổng số menu con (parent_id IS NOT NULL)
    long countByParentIsNotNull();

    // Truy vấn tất cả menu cha mà có chứa role chỉ định
    @Query("SELECT m FROM Menu m WHERE m.parent IS NULL AND (m.roles LIKE %:role% OR m.roles IS NULL) ORDER BY m.displayOrder ASC")
    List<Menu> findRootMenusForRole(@Param("role") String role);
}
