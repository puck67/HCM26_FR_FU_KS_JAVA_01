package com.example.ex4.repository;

import com.example.ex4.model.AssessmentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentMaterialRepository extends JpaRepository<AssessmentMaterial, Long> {

    @Query("SELECT a FROM AssessmentMaterial a WHERE " +
           "LOWER(a.originalName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.subject) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<AssessmentMaterial> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(DISTINCT LOWER(a.subject)) FROM AssessmentMaterial a")
    long countDistinctSubjects();

    @Query("SELECT COALESCE(SUM(a.size), 0) FROM AssessmentMaterial a")
    long sumStorageUsed();
}
