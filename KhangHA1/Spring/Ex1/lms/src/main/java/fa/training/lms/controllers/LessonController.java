package fa.training.lms.controllers;

import fa.training.lms.controllers.base.GenericController;
import fa.training.lms.entities.Lesson;
import fa.training.lms.services.LessonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lessons")
public class LessonController extends GenericController<Lesson, Integer> {
    public LessonController(LessonService lessonService) {
        super(lessonService, "lessons", "lesson");
    }

    @Override
    protected Class<Lesson> getEntityClass() {
        return Lesson.class;
    }
}
