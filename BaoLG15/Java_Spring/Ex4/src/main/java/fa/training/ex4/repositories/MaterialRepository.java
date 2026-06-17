package fa.training.ex4.repositories;

import fa.training.ex4.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findBySubjectId(Long subject_id);

    @Query("SELECT SUM(m.file_size) FROM Material m")
    Long sumTotalFileSize();
}
