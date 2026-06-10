package com.example.demo.repository;

import com.example.demo.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseIdCourseCodeAndCourseIdStartDateOrderByIdAsc(String courseCode, LocalDate startDate);
}
