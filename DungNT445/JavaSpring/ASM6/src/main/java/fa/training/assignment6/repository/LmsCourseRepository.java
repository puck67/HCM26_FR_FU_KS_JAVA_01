package fa.training.assignment6.repository;

import fa.training.assignment6.entity.LmsCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LmsCourseRepository extends JpaRepository<LmsCourse, Long> {

    Page<LmsCourse> findByStatusOrderByIdDesc(Integer status, Pageable pageable);

    @Query("SELECT c FROM LmsCourse c WHERE c.status = 2 AND LOWER(c.category) LIKE LOWER(concat('%', :category, '%'))")
    Page<LmsCourse> findPublishedCoursesByCategory(@Param("category") String category, Pageable pageable);

    long countByStatus(Integer status);
}