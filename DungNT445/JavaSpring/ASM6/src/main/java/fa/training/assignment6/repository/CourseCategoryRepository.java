package fa.training.assignment6.repository;

import fa.training.assignment6.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {

    List<CourseCategory> findAllByOrderByFrequencyDesc();
}
