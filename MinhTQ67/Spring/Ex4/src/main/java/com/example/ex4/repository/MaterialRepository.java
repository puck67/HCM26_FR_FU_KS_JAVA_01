package com.example.ex4.repository;

import com.example.ex4.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findBySubject_SubjectId(Long subjectId);
    
    @Query("SELECT m FROM Material m WHERE " +
           "LOWER(m.fileName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.subject.subjectName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Material> searchMaterials(@Param("keyword") String keyword);
    
    @Query("SELECT SUM(m.fileSize) FROM Material m")
    Long getTotalStorageUsed();
}
