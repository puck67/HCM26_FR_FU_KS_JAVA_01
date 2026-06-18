package com.lms.materialmanager.repository;

import com.lms.materialmanager.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    List<Material> findBySubjectSubjectId(Long subjectId);

    // Bonus 2: Search Materials by File Name, Description, or Subject Name
    @Query("SELECT m FROM Material m WHERE LOWER(m.fileName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(m.subject.subjectName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Material> searchMaterials(@Param("query") String query);

    // Sum of all file sizes in bytes
    @Query("SELECT COALESCE(SUM(m.fileSize), 0) FROM Material m")
    long sumAllFileSizes();
}
