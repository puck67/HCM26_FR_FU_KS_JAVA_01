package com.lms.materialmanager.service.impl;

import com.lms.materialmanager.entity.Material;
import com.lms.materialmanager.entity.Subject;
import com.lms.materialmanager.repository.MaterialRepository;
import com.lms.materialmanager.repository.SubjectRepository;
import com.lms.materialmanager.service.MaterialService;
import com.lms.materialmanager.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final SubjectRepository subjectRepository;
    private final StorageService storageService;

    public MaterialServiceImpl(MaterialRepository materialRepository, 
                               SubjectRepository subjectRepository, 
                               StorageService storageService) {
        this.materialRepository = materialRepository;
        this.subjectRepository = subjectRepository;
        this.storageService = storageService;
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public List<Material> getMaterialsBySubject(Long subjectId) {
        return materialRepository.findBySubjectSubjectId(subjectId);
    }

    @Override
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Requested file does not exist."));
    }

    @Override
    public Material uploadMaterial(MultipartFile file, String description, String category, Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with ID: " + subjectId));

        // Store physical file
        String storedFileName = storageService.storeFile(file);

        // Save metadata using fluent setter chain
        Material material = new Material()
                .setFileName(file.getOriginalFilename())
                .setStoredFileName(storedFileName)
                .setFileSize(file.getSize())
                .setFileType(file.getContentType())
                .setUploadDate(LocalDateTime.now())
                .setDescription(description)
                .setCategory(category)
                .setSubject(subject);
        subject.getMaterials().add(material);

        return materialRepository.save(material);
    }

    @Override
    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Requested file does not exist."));
        
        // Delete physical file first
        storageService.deleteFile(material.getStoredFileName());
        
        // Remove from subject materials list
        if (material.getSubject() != null) {
            material.getSubject().getMaterials().remove(material);
        }
        
        // Delete DB record
        materialRepository.delete(material);
    }

    @Override
    public List<Material> searchMaterials(String query) {
        if (query == null || query.trim().isEmpty()) {
            return materialRepository.findAll();
        }
        return materialRepository.searchMaterials(query.trim());
    }

    @Override
    public long getTotalStorageSize() {
        return materialRepository.sumAllFileSizes();
    }

    @Override
    public long getTotalMaterialsCount() {
        return materialRepository.count();
    }
}
