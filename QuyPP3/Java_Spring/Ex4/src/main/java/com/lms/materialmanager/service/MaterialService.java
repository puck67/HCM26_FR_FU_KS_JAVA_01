package com.lms.materialmanager.service;

import com.lms.materialmanager.entity.Material;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MaterialService {
    List<Material> getAllMaterials();
    List<Material> getMaterialsBySubject(Long subjectId);
    Material getMaterialById(Long id);
    Material uploadMaterial(MultipartFile file, String description, String category, Long subjectId);
    void deleteMaterial(Long id);
    List<Material> searchMaterials(String query);
    long getTotalStorageSize();
    long getTotalMaterialsCount();
}
