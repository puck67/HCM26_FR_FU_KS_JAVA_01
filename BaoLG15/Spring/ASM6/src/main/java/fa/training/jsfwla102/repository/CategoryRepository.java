package fa.training.jsfwla102.repository;

import fa.training.jsfwla102.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByFrequencyDesc();
}
