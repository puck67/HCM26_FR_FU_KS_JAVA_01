package fa.training.lms.config;

import fa.training.lms.entities.Course;
import fa.training.lms.entities.CourseId;
import fa.training.lms.entities.Lesson;
import fa.training.lms.repositories.CourseRepository;
import fa.training.lms.repositories.LessonRepository;
import fa.training.lms.entities.ContentType;
import fa.training.lms.entities.LessonStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public DataInitializer(CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Chỉ khởi tạo dữ liệu mẫu nếu DB trống
        if (courseRepository.findAll().isEmpty()) {
            System.out.println("====== [LMS] Bắt đầu khởi tạo dữ liệu mẫu... ======");

            // 1. Tạo khóa học: Java Core
            CourseId courseId1 = new CourseId("JAVA101", LocalDate.of(2026, 6, 1));
            Course course1 = new Course(courseId1, "Java Core Programming", "IT", "Dr. James Gosling", new ArrayList<>());
            courseRepository.save(course1);

            // Tạo các bài học cho khóa học Java Core
            Lesson lesson1_1 = new Lesson(null, "Introduction to Java Basics", 45, ContentType.VIDEO, LessonStatus.ACTIVE, course1);
            Lesson lesson1_2 = new Lesson(null, "Object-Oriented Programming (OOP) in Java", 60, ContentType.THEORY, LessonStatus.ACTIVE, course1);
            lessonRepository.save(lesson1_1);
            lessonRepository.save(lesson1_2);

            // 2. Tạo khóa học: React JS
            CourseId courseId2 = new CourseId("REACT201", LocalDate.of(2026, 6, 15));
            Course course2 = new Course(courseId2, "Modern React with Hooks", "Frontend Development", "Dan Abramov", new ArrayList<>());
            courseRepository.save(course2);

            // Tạo bài học cho khóa học React
            Lesson lesson2_1 = new Lesson(null, "React Hooks & Context API Deep Dive", 90, ContentType.PRACTICE, LessonStatus.ACTIVE, course2);
            lessonRepository.save(lesson2_1);

            System.out.println("====== [LMS] Khởi tạo dữ liệu mẫu thành công! ======");
        }
    }
}
