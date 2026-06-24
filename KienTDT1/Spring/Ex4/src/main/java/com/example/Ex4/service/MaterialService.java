package com.example.Ex4.service;

import com.example.Ex4.entity.Material;
import com.example.Ex4.repository.MaterialRepository;
import com.example.Ex4.repository.SubjectRepository;
import com.example.Ex4.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class MaterialService extends GenericServiceImpl<Material> {

    private final FileStorageService storage;
    private final SubjectRepository subjectRepository;

    private final List<String> allowed = Arrays.asList(
            "pdf",
            "docx",
            "pptx",
            "zip",
            "txt"
    );

    public MaterialService(MaterialRepository repository, FileStorageService storage, SubjectRepository subjectRepository) {
        super(repository);
        this.storage = storage;
        this.subjectRepository = subjectRepository;
    }

    public Material upload(Material material, MultipartFile file) throws Exception {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("Maximum file size is 10MB.");
        }

        String original = file.getOriginalFilename();
        if (original == null || !original.contains(".")) {
            throw new RuntimeException("Invalid file name or extension missing.");
        }

        String extension = original.substring(original.lastIndexOf(".") + 1).toLowerCase();
        if (!allowed.contains(extension)) {
            throw new RuntimeException("Unsupported file format.");
        }

        String stored = storage.save(file);

        material.setFileName(original);
        material.setStoredFileName(stored);
        material.setFileSize(file.getSize());
        material.setFileType(file.getContentType());
        material.setUploadDate(LocalDateTime.now());

        if (material.getSubject() != null && material.getSubject().getId() != null) {
            var subj = subjectRepository.findById(material.getSubject().getId()).orElse(null);
            material.setSubject(subj);
        }

        return repo.save(material);
    }

    public void deleteMaterial(Material material) throws Exception {
        if (material.getStoredFileName() != null) {
            storage.delete(material.getStoredFileName());
        }
        repo.delete(material);
    }

    @Override
    public void delete(Long id) {
        try {
            Material material = findById(id);
            if (material != null) {
                deleteMaterial(material);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete material: " + e.getMessage(), e);
        }
    }

    public Path loadFile(String filename) {
        return storage.load(filename);
    }
}