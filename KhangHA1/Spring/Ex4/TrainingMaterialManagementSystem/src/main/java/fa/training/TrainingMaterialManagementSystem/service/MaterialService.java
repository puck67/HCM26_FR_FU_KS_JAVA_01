package fa.training.TrainingMaterialManagementSystem.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import fa.training.TrainingMaterialManagementSystem.entity.Material;
import fa.training.TrainingMaterialManagementSystem.service.base.GenericService;

public interface MaterialService extends GenericService<Material, Long> {

    Material upload(Long subjectId, MultipartFile file, String description);

    Resource download(Long materialId);

    void deleteMaterial(Long materialId);
}
