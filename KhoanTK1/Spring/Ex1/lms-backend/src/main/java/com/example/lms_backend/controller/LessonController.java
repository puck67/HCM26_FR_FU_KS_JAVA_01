package com.example.lms_backend.controller;

import com.example.lms_backend.entity.Lesson;
import com.example.lms_backend.service.LessonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lessons")
public class LessonController extends BaseController<Lesson, Long> {

    public LessonController(LessonService service) {
        super(service);
    }
}
