package fa.training.TrainingMaterialManagementSystem.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fa.training.TrainingMaterialManagementSystem.entity.Material;
import fa.training.TrainingMaterialManagementSystem.entity.Subject;
import fa.training.TrainingMaterialManagementSystem.repository.MaterialRepository;
import fa.training.TrainingMaterialManagementSystem.repository.SubjectRepository;
import fa.training.TrainingMaterialManagementSystem.service.base.GenericServiceImpl;
import fa.training.TrainingMaterialManagementSystem.utils.StorageUtil;
import fa.training.TrainingMaterialManagementSystem.utils.validator.FileValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl
        extends GenericServiceImpl<Material, Long>
        implements MaterialService {

    private final MaterialRepository materialRepository;
    private final SubjectRepository subjectRepository;
    private final StorageUtil storageUtil;
    private final FileValidator validator;

    @Override
    protected JpaRepository<Material, Long> getRepository() {
        return materialRepository;
    }

    @Override
    public Material upload(Long subjectId, MultipartFile file, String description) {

        validator.validate(file);

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + subjectId));

        String storedName;
        try {
            storedName = storageUtil.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        Material material = Material.builder()
                .fileName(file.getOriginalFilename())
                .storedFileName(storedName)
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .description(description)
                .uploadDate(LocalDateTime.now())
                .subject(subject)
                .build();

        return materialRepository.save(material);
    }

    @Override
    public Resource download(Long materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found: " + materialId));
        try {
            return storageUtil.load(material.getStoredFileName());
        } catch (Exception e) {
            throw new RuntimeException("Could not load file", e);
        }
    }

    @Override
    public void deleteMaterial(Long materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found: " + materialId));
        try {
            storageUtil.delete(material.getStoredFileName());
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file", e);
        }
        materialRepository.deleteById(materialId);
    }

    @Override
    public Material update(Long id, Material entity) {
        Material existing = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found: " + id));
        existing.setFileName(entity.getFileName());
        existing.setDescription(entity.getDescription());
        return materialRepository.save(existing);
    }
}