package com.example.asm6.service.impl;

import com.example.asm6.model.Category;
import com.example.asm6.model.Course;
import com.example.asm6.repository.CategoryRepository;
import com.example.asm6.repository.CourseRepository;
import com.example.asm6.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAllByOrderByFrequencyDesc();
    }

    @Override
    public List<Category> getActiveCategories() {
        return categoryRepository.findByFrequencyGreaterThanOrderByFrequencyDesc(0);
    }

    @Override
    @Transactional
    public void recalculateCategoryFrequencies() {
        // Lấy tất cả các khóa học
        List<Course> courses = courseRepository.findAll();
        Map<String, Integer> frequencyMap = new HashMap<>();

        // Chỉ tính toán tần suất từ các khóa học ở trạng thái PUBLISHED (status = 2)
        // hoặc tất cả khóa học? Đề bài ghi: "the number of courses associated with this category".
        // Thường là tính trên các khóa học đã PUBLISHED vì đây là Category Cloud cho học viên xem.
        // Hãy tính trên các khóa học có status = 2 (PUBLISHED).
        for (Course course : courses) {
            if (course.getStatus() == 2 && course.getCategory() != null && !course.getCategory().trim().isEmpty()) {
                String[] tags = course.getCategory().split(",");
                for (String tag : tags) {
                    String cleanTag = tag.trim();
                    if (!cleanTag.isEmpty()) {
                        // Tìm kiếm case-insensitive trong map để gộp chung, ví dụ "java" và "Java"
                        String matchedKey = null;
                        for (String key : frequencyMap.keySet()) {
                            if (key.equalsIgnoreCase(cleanTag)) {
                                matchedKey = key;
                                break;
                            }
                        }
                        if (matchedKey != null) {
                            frequencyMap.put(matchedKey, frequencyMap.get(matchedKey) + 1);
                        } else {
                            frequencyMap.put(cleanTag, 1);
                        }
                    }
                }
            }
        }

        // Xóa các category cũ
        categoryRepository.deleteAll();

        // Lưu các category mới có tần suất > 0
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            Category category = Category.builder()
                    .name(entry.getKey())
                    .frequency(entry.getValue())
                    .build();
            categoryRepository.save(category);
        }
    }
}
