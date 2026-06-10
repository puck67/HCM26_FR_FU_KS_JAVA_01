package com.lms.coursemanager.repository;

import com.lms.coursemanager.entity.CourseId;
import com.lms.coursemanager.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseId(CourseId courseId);
}
