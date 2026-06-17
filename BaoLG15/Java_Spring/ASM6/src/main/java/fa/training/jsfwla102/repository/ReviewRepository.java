package fa.training.jsfwla102.repository;

import fa.training.jsfwla102.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findTop5ByStatusOrderByIdDesc(Integer status);

    List<Review> findByStatusOrderByIdDesc(Integer status);

    long countByStatus(Integer status);
}
