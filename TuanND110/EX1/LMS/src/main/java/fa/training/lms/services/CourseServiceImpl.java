package fa.training.lms.services;

import fa.training.lms.entities.Course;
import fa.training.lms.entities.CourseId;
import fa.training.lms.interfaces.CourseService;
import fa.training.lms.repositories.CourseRepository;
import fa.training.lms.services.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends GenericServiceImpl<Course, CourseId> implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        super(courseRepository);
        this.courseRepository = courseRepository;
    }

    @Override
    public java.util.List<Course> getCoursesByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category must not be empty");
        }
        return courseRepository.findByCategory(category);
    }

    private void validate(Course entity) {
        if (entity.getCourseName() == null || entity.getCourseName().trim().isEmpty()) {
            throw new IllegalArgumentException("Course name must not be empty");
        }
    }

    @Override
    public Course save(Course entity) {
        validate(entity);
        return super.save(entity);
    }

    @Override
    public Course update(CourseId id, Course entity) {
        validate(entity);
        return super.update(id, entity);
    }

    @Override
    public Course patch(CourseId id, java.util.Map<String, Object> updates) {
        Course existing = findById(id);
        if (existing == null) {
            throw new RuntimeException("Cannot patch. Course not found for ID: " + id);
        }

        if (updates.containsKey("courseName")) {
            String courseName = (String) updates.get("courseName");
            if (courseName == null || courseName.trim().isEmpty()) {
                throw new IllegalArgumentException("Course name must not be empty");
            }
            existing.setCourseName(courseName);
        }

        if (updates.containsKey("category")) {
            String category = (String) updates.get("category");
            existing.setCategory(category);
        }

        if (updates.containsKey("instructor")) {
            String instructor = (String) updates.get("instructor");
            existing.setInstructor(instructor);
        }

        return repository.update(existing);
    }
}
