package com.lms.repository;

import com.lms.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    List<Lesson> findByCourse_CourseCodeAndCourse_StartDate(String courseCode, LocalDate startDate);
}
