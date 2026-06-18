package com.example.EX1.service;

import com.example.EX1.model.Course;
import com.example.EX1.model.CourseId;
import com.example.EX1.repository.CourseRepository;
import com.example.EX1.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

/**
 * CourseService - kế thừa GenericServiceImpl với Entity=Course, ID=CourseId.
 * Các thao tác CRUD cơ bản đã được xử lý bởi lớp cha.
 * Có thể bổ sung các hàm nghiệp vụ riêng của Course ở đây nếu cần.
 */
@Service
public class CourseService extends GenericServiceImpl<Course, CourseId> {

    public CourseService(CourseRepository repository) {
        super(repository);
    }
}
