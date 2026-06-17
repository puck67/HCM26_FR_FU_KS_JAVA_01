package fa.training.ex4.services.impl;

import fa.training.ex4.dto.response.MaterialResponse;
import fa.training.ex4.entities.Material;
import fa.training.ex4.entities.Subject;
import fa.training.ex4.mappers.MaterialMapper;
import fa.training.ex4.repositories.MaterialRepository;
import fa.training.ex4.repositories.SubjectRepository;
import fa.training.ex4.services.MaterialService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final SubjectRepository subjectRepository;
    private final MaterialMapper materialMapper;

    private final Path rootLocation = Paths.get("uploads/materials");

    public MaterialServiceImpl(MaterialRepository materialRepository, SubjectRepository subjectRepository, MaterialMapper materialMapper) {
        this.materialRepository = materialRepository;
        this.subjectRepository = subjectRepository;
        this.materialMapper = materialMapper;

        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Override
    public List<MaterialResponse> findBySubjectId(Long subject_id) {
        return materialRepository.findBySubjectId(subject_id).stream()
                .map(materialMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void uploadMaterial(Long subject_id, String description, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty, please select a file.");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("Maximum file size is 10MB.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) throw new IllegalArgumentException("Invalid file name.");

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        List<String> allowedExtensions = Arrays.asList(".pdf", ".docx", ".pptx", ".zip", ".txt");
        List<String> rejectedExtensions = Arrays.asList(".exe", ".bat", ".cmd", ".sh");

        if (rejectedExtensions.contains(fileExtension) || !allowedExtensions.contains(fileExtension)) {
            throw new RuntimeException("Unsupported file format.");
        }

        String storedFileName = UUID.randomUUID().toString() + "-" + originalFilename;

        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(storedFileName), StandardCopyOption.REPLACE_EXISTING);

            Subject subject = subjectRepository.findById(subject_id)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));

            Material material = Material.builder()
                    .file_name(originalFilename)
                    .stored_file_name(storedFileName)
                    .file_size(file.getSize())
                    .file_type(file.getContentType())
                    .upload_date(LocalDateTime.now())
                    .description(description)
                    .subject(subject)
                    .build();

            materialRepository.save(material);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file physically", ex);
        }
    }

    @Override
    public Resource downloadMaterial(Long material_id, StringBuilder originalFileName) {
        Material material = materialRepository.findById(material_id)
                .orElseThrow(() -> new RuntimeException("Requested file does not exist."));

        try {
            Path file = rootLocation.resolve(material.getStored_file_name());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                originalFileName.append(material.getFile_name());
                return resource;
            } else {
                throw new RuntimeException("Requested file does not exist.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Requested file does not exist.", e);
        }
    }

    @Override
    @Transactional
    public void deleteMaterial(Long materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Requested file does not exist."));

        try {
            Path file = rootLocation.resolve(material.getStored_file_name());
            Files.deleteIfExists(file);

            materialRepository.delete(material);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete physical file from disk", e);
        }
    }
}
