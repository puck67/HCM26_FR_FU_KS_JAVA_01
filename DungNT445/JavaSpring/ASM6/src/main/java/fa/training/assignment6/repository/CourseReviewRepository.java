package fa.training.assignment6.repository;

import fa.training.assignment6.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    List<CourseReview> findTop5ByStatusOrderByIdDesc(Integer status);

    List<CourseReview> findByStatusOrderByIdDesc(Integer status);

    long countByStatus(Integer status);
}
