package com.example.app;

import com.example.entity.Course;
import com.example.service.CourseService;
import com.example.service.impl.CourseServiceImpl;
import com.example.util.InputUtil;

import java.util.List;
import java.util.Scanner;

public class CourseHandler {
    private final CourseService courseService = new CourseServiceImpl();
    private final Scanner scanner;

    public CourseHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void createCourse() {
        System.out.print("Enter course title: ");
        String title = scanner.nextLine();
        int credit = InputUtil.readInt(scanner, "Enter course credit: ");
        courseService.createCourse(title, credit);
        System.out.println("Course created.");
    }

    public void updateCourse() {
        int id = InputUtil.readInt(scanner, "Enter course ID to update: ");
        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        int credit = InputUtil.readInt(scanner, "Enter new credit: ");
        courseService.updateCourse(id, title, credit);
        System.out.println("Course updated.");
    }

    public void deleteCourse() {
        int id = InputUtil.readInt(scanner, "Enter course ID to delete: ");
        courseService.deleteCourse(id);
        System.out.println("Course deleted.");
    }

    public void viewCourseById() {
        int id = InputUtil.readInt(scanner, "Enter course ID: ");
        Course c = courseService.getCourseById(id);
        if (c != null) {
            System.out.println("--------------------------------------------------");
            System.out.printf("%-10s | %-20s | %-10s%n", "ID", "Title", "Credit");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-10d | %-20s | %-10d%n", c.getId(), c.getTitle(), c.getCredit());
            System.out.println("--------------------------------------------------");
        } else {
            System.out.println("Not found.");
        }
    }

    public void listAllCourses() {
        List<Course> list = courseService.getAllCourses();
        System.out.println("--------------------------------------------------");
        System.out.printf("%-10s | %-20s | %-10s%n", "ID", "Title", "Credit");
        System.out.println("--------------------------------------------------");
        list.forEach(c -> System.out.printf("%-10d | %-20s | %-10d%n", c.getId(), c.getTitle(), c.getCredit()));
        System.out.println("--------------------------------------------------");
    }
}
