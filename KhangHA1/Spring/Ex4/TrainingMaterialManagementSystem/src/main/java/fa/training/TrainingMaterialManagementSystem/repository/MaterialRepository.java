package fa.training.TrainingMaterialManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fa.training.TrainingMaterialManagementSystem.entity.Material;

public interface MaterialRepository
        extends JpaRepository<Material, Long> {

    @Query("""
            SELECT COALESCE(SUM(m.fileSize),0)
            FROM Material m
            """)
    Long totalStorage();
}
