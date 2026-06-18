package fa.training.exercise1.repositories;

import fa.training.exercise1.entities.LmsCourse;
import fa.training.exercise1.entities.LmsCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LmsCourseRepository extends JpaRepository<LmsCourse, LmsCourseId> {
}
