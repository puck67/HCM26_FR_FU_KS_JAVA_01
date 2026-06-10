package com.lms;

import com.lms.model.Course;
import com.lms.model.CourseId;
import com.lms.model.Lesson;
import com.lms.enums.ContentType;
import com.lms.enums.LessonStatus;
import com.lms.service.CourseService;
import com.lms.service.LessonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(CourseService courseService, LessonService lessonService) {
        return args -> {
            // Seed a sample course
            CourseId courseId = new CourseId("CRS101", LocalDate.of(2023, 10, 15));
            Course course = new Course();
            course.setId(courseId);
            course.setCourseName("Java Web Development");
            course.setCategory("Technology");
            course.setInstructor("Dr. Nguyen Van A");
            courseService.saveCourse(course);

            // Seed lessons for this course
            Lesson lesson1 = new Lesson(null, "Intro to Java", 60, ContentType.VIDEO, LessonStatus.ACTIVE, course);
            Lesson lesson2 = new Lesson(null, "Spring MVC", 90, ContentType.VIDEO, LessonStatus.ACTIVE, course);
            Lesson lesson3 = new Lesson(null, "JPA Concepts", 75, ContentType.VIDEO, LessonStatus.INACTIVE, course);
            
            lessonService.saveLesson(lesson1);
            lessonService.saveLesson(lesson2);
            lessonService.saveLesson(lesson3);

            // Seed another course for management demonstration
            CourseId courseId2 = new CourseId("CRS102", LocalDate.of(2023, 11, 1));
            Course course2 = new Course();
            course2.setId(courseId2);
            course2.setCourseName("Intro to ReactJS");
            course2.setCategory("Technology");
            course2.setInstructor("Prof. Le Van B");
            courseService.saveCourse(course2);

            Lesson lessonReact = new Lesson(null, "React Hooks", 45, ContentType.VIDEO, LessonStatus.ACTIVE, course2);
            lessonService.saveLesson(lessonReact);
        };
    }
}
