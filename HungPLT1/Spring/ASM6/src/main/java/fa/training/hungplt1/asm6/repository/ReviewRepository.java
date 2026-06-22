package fa.training.hungplt1.asm6.repository;

import fa.training.hungplt1.asm6.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findTop5ByStatusOrderByIdDesc(Integer status);

    List<Review> findByStatusOrderByIdDesc(Integer status);

    long countByStatus(Integer status);
}
