package fa.training.jsfwla102.service;

import fa.training.jsfwla102.entity.Category;
import fa.training.jsfwla102.entity.Course;
import fa.training.jsfwla102.repository.CategoryRepository;
import fa.training.jsfwla102.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Course saveCourse(Course course) {
        Course savedCourse = courseRepository.save(course);

        if (course.getCategory() != null && !course.getCategory().isEmpty()) {
            String[] tags = course.getCategory().split(",");
            for (String tag : tags) {
                String cleanTag = tag.trim();
                if (!cleanTag.isEmpty()) {
                    categoryRepository.findAll().stream()
                            .filter(c -> c.getName().equalsIgnoreCase(cleanTag))
                            .findFirst()
                            .ifPresentOrElse(
                                    existingCat -> {
                                        existingCat.setFrequency(existingCat.getFrequency() + 1);
                                        categoryRepository.save(existingCat);
                                    },
                                    () -> {
                                        Category newCat = new Category();
                                        newCat.setName(cleanTag);
                                        newCat.setFrequency(1);
                                        categoryRepository.save(newCat);
                                    }
                            );
                }
            }
        }
        return savedCourse;
    }
}
