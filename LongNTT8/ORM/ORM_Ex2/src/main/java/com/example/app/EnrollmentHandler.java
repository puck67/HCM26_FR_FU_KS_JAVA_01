package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.InputUtil;

import java.util.Scanner;
import java.util.Set;

public class EnrollmentHandler {
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final Scanner scanner;

    public EnrollmentHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void enrollStudent() {
        int sId = InputUtil.readInt(scanner, "Enter student ID: ");
        int cId = InputUtil.readInt(scanner, "Enter course ID: ");
        studentService.enrollStudentInCourse(sId, cId);
        System.out.println("Enrolled.");
    }

    public void removeStudentFromCourse() {
        int sId = InputUtil.readInt(scanner, "Enter student ID: ");
        int cId = InputUtil.readInt(scanner, "Enter course ID: ");
        studentService.removeStudentFromCourse(sId, cId);
        System.out.println("Removed.");
    }

    public void viewCoursesOfStudent() {
        int id = InputUtil.readInt(scanner, "Enter student ID: ");
        Set<Course> courses = studentService.getCoursesOfStudent(id);
        if (courses != null) {
            courses.forEach(c -> System.out.println(c.getTitle()));
        } else {
            System.out.println("No courses or student not found.");
        }
    }

    public void viewStudentsOfCourse() {
        int id = InputUtil.readInt(scanner, "Enter course ID: ");
        Set<Student> students = courseService.getStudentsOfCourse(id);
        if (students != null) {
            students.forEach(s -> System.out.println(s.getName()));
        } else {
            System.out.println("No students or course not found.");
        }
    }
}
