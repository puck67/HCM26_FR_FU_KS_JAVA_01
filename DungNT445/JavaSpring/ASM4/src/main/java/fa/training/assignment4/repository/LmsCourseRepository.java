package fa.training.assignment4.repository;

import fa.training.assignment4.entity.LmsCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LmsCourseRepository extends JpaRepository<LmsCourse, Long> {
}
