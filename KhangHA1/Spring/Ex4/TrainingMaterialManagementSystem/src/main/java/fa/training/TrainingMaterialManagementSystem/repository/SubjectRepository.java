package fa.training.TrainingMaterialManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fa.training.TrainingMaterialManagementSystem.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
