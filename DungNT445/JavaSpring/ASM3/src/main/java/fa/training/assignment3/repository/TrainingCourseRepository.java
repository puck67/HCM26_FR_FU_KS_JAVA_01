package fa.training.assignment3.repository;

import fa.training.assignment3.entity.TrainingCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingCourseRepository extends JpaRepository<TrainingCourse, Long> {

    List<TrainingCourse> findByInstructorFullNameContainingIgnoreCase(String instructorName);

    List<TrainingCourse> findByCourseNameContainingIgnoreCase(String keyword);
}
