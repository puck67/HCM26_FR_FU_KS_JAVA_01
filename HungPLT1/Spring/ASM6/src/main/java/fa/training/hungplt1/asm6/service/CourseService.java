package fa.training.hungplt1.asm6.service;

import fa.training.hungplt1.asm6.entity.Category;
import fa.training.hungplt1.asm6.entity.Course;
import fa.training.hungplt1.asm6.repository.CategoryRepository;
import fa.training.hungplt1.asm6.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        List<String> oldTags = new ArrayList<>();
        if (course.getId() != null) {
            courseRepository.findById(course.getId()).ifPresent(oldCourse -> {
                oldTags.addAll(parseTags(oldCourse.getCategory()));
            });
        }

        Course savedCourse = courseRepository.save(course);
        List<String> newTags = parseTags(course.getCategory());

        updateCategoryFrequencies(oldTags, newTags);

        return savedCourse;
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.findById(id).ifPresent(course -> {
            List<String> tags = parseTags(course.getCategory());
            updateCategoryFrequencies(tags, Collections.emptyList());
            courseRepository.delete(course);
        });
    }

    private List<String> parseTags(String categoryStr) {
        if (categoryStr == null || categoryStr.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(categoryStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private void updateCategoryFrequencies(List<String> oldTags, List<String> newTags) {
        Map<String, Integer> diffMap = new HashMap<>();
        for (String tag : oldTags) {
            String lowerTag = tag.toLowerCase();
            diffMap.put(lowerTag, diffMap.getOrDefault(lowerTag, 0) - 1);
        }
        for (String tag : newTags) {
            String lowerTag = tag.toLowerCase();
            diffMap.put(lowerTag, diffMap.getOrDefault(lowerTag, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : diffMap.entrySet()) {
            String tagLower = entry.getKey();
            int diff = entry.getValue();
            if (diff == 0) continue;

            String originalName = findOriginalName(oldTags, newTags, tagLower);

            Category category = categoryRepository.findByNameIgnoreCase(tagLower)
                    .orElse(null);

            if (category == null) {
                if (diff > 0) {
                    Category newCat = new Category();
                    newCat.setName(originalName);
                    newCat.setFrequency(diff);
                    categoryRepository.save(newCat);
                }
            } else {
                int newFreq = category.getFrequency() + diff;
                if (newFreq <= 0) {
                    categoryRepository.delete(category);
                } else {
                    category.setFrequency(newFreq);
                    categoryRepository.save(category);
                }
            }
        }
    }

    private String findOriginalName(List<String> oldTags, List<String> newTags, String tagLower) {
        for (String t : newTags) {
            if (t.equalsIgnoreCase(tagLower)) return t;
        }
        for (String t : oldTags) {
            if (t.equalsIgnoreCase(tagLower)) return t;
        }
        return tagLower;
    }
}
