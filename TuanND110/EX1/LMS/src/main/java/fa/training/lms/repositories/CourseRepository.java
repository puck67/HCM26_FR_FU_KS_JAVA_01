package fa.training.lms.repositories;

import fa.training.lms.entities.Course;
import fa.training.lms.entities.CourseId;
import fa.training.lms.repositories.base.GenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepository extends GenericRepository<Course, CourseId> {
    public CourseRepository() {
        super(Course.class);
    }

    public java.util.List<Course> findByCategory(String category) {
        return entityManager.createQuery("SELECT c FROM Course c WHERE c.category = :category", Course.class)
                .setParameter("category", category)
                .getResultList();
    }
}
