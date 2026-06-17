package com.material.repository;

import com.material.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
    List<Material> findBySubjectId(Long subjectId);

    @Query("SELECT m FROM Material m WHERE LOWER(m.fileName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(m.subject.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(m.subject.code) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Material> searchMaterials(@Param("query") String query);

    @Query("SELECT SUM(m.fileSize) FROM Material m")
    Long getTotalStorageUsed();
}
