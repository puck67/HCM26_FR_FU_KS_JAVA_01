package fa.training.ex4.services;

import fa.training.ex4.dto.response.MaterialResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MaterialService {
    List<MaterialResponse> findBySubjectId(Long subject_id);

    void uploadMaterial(Long subject_id, String description, MultipartFile file);

    Resource downloadMaterial(Long material_id, StringBuilder originalFileName);

    void deleteMaterial(Long material_id);
}
