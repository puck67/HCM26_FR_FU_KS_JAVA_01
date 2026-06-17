package fa.training.jsfwla102.repository;

import fa.training.jsfwla102.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByUsername(String username);
}
