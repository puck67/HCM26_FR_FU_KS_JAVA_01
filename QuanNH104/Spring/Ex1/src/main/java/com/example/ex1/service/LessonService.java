package com.example.ex1.service;

import com.example.ex1.entity.Lesson;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LessonService {
    List<Lesson> findByCourse(String courseCode, LocalDate startDate);
    Optional<Lesson> findById(Long id);
    Lesson save(Lesson lesson);
    void deleteById(Long id);
}
