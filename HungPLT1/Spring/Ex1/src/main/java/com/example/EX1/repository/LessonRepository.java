package com.example.EX1.repository;

import com.example.EX1.model.CourseId;
import com.example.EX1.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByCourseId(CourseId courseId);
}
