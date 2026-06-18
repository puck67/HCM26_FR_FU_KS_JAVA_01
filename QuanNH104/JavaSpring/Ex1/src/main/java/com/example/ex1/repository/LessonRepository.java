package com.example.ex1.repository;

import com.example.ex1.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    
    // Tìm các bài học thuộc về một khóa học xác định bằng khóa chính hỗn hợp của khóa học đó
    List<Lesson> findByCourseIdCourseCodeAndCourseIdStartDate(String courseCode, LocalDate startDate);
}
