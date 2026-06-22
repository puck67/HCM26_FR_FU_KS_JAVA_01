package com.example.ASM6.service;

import com.example.ASM6.model.Category;
import com.example.ASM6.model.Course;
import com.example.ASM6.repository.CategoryRepository;
import com.example.ASM6.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final CourseRepository courseRepo;

    public CategoryService(CategoryRepository categoryRepo, CourseRepository courseRepo) {
        this.categoryRepo = categoryRepo;
        this.courseRepo = courseRepo;
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAllByOrderByFrequencyDesc();
    }

    @Transactional
    public void rebuildCategoryFrequencies() {
        List<Course> allCourses = courseRepo.findAll();
        Map<String, Integer> freqMap = new HashMap<>();

        for (Course c : allCourses) {
            if (c.getStatus() == 2 && c.getCategory() != null) {
                String[] parts = c.getCategory().split(",");
                for (String p : parts) {
                    String cleanTag = p.trim();
                    if (!"".equals(cleanTag)) {
                        Integer count = freqMap.get(cleanTag);
                        if (count == null) {
                            freqMap.put(cleanTag, 1);
                        } else {
                            freqMap.put(cleanTag, count + 1);
                        }
                    }
                }
            }
        }

        categoryRepo.deleteAllInBatch();

        for (Map.Entry<String, Integer> item : freqMap.entrySet()) {
            categoryRepo.save(new Category(item.getKey(), item.getValue()));
        }
    }
}
