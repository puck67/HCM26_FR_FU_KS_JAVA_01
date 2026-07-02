package com.fpt.lms.repository;

import com.fpt.lms.model.AssessmentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentMaterialRepository extends JpaRepository<AssessmentMaterial, Long> {
}
