package fa.training.ex4.repositories;

import fa.training.ex4.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsBySubjectCode(String subject_code);
}
