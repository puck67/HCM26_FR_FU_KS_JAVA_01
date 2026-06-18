package com.assessment.service;

import com.assessment.model.AssessmentMaterial;

import java.util.List;

public interface AssessmentService {
    List<AssessmentMaterial> getAllMaterials();
    void addMaterial(AssessmentMaterial material);
    void deleteMaterial(long id);
}
