package fa.training.assignment6.service;

import fa.training.assignment6.entity.CourseCategory;
import fa.training.assignment6.entity.LmsCourse;
import fa.training.assignment6.repository.CourseCategoryRepository;
import fa.training.assignment6.repository.LmsCourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LmsCourseService {
    private final LmsCourseRepository courseRepository;
    private final CourseCategoryRepository categoryRepository;

    public LmsCourseService(LmsCourseRepository courseRepository, CourseCategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public LmsCourse saveCourse(LmsCourse course) {
        LmsCourse savedCourse = courseRepository.save(course);

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
                                        CourseCategory newCat = new CourseCategory();
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
