package fa.training.lms.repository;

import fa.training.lms.model.LmsUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LmsUserRepository extends JpaRepository<LmsUser, Long> {
}
