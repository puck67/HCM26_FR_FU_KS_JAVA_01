package com.example.lms_backend.repository;

import com.example.lms_backend.entity.Lesson;
import com.example.lms_backend.entity.CourseId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LessonRepository extends BaseRepository<Lesson, Long> {

    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId")
    List<Lesson> findByCourseId(@Param("courseId") CourseId courseId);
}
