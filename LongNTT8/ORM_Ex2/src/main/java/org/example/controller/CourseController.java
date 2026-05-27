package org.example.controller;

import org.example.dao.CourseDAO;
import org.example.entity.Course;

import java.util.Optional;
import java.util.Scanner;

public class CourseController {
    private final CourseDAO courseDAO = new CourseDAO();

    public void createCourse(Scanner scanner) {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Credit: ");
        int credit = Integer.parseInt(scanner.nextLine());
        Course course = Course.builder().title(title).credit(credit).build();
        courseDAO.save(course);
        System.out.println("Saved: " + course);
    }

    public void updateCourse(Scanner scanner) {
        System.out.print("Course ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Course> opt = courseDAO.findById(id);
        if (opt.isPresent()) {
            Course course = opt.get();
            System.out.print("New title (" + course.getTitle() + "): ");
            String title = scanner.nextLine().trim();
            if (!title.isEmpty()) course.setTitle(title);

            System.out.print("New credit (" + course.getCredit() + "): ");
            String creditStr = scanner.nextLine().trim();
            if (!creditStr.isEmpty()) course.setCredit(Integer.parseInt(creditStr));

            courseDAO.update(course);
            System.out.println("Updated.");
        } else {
            System.out.println("Not found.");
        }
    }

    public void deleteCourse(Scanner scanner) {
        System.out.print("Course ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Course> opt = courseDAO.findById(id);
        if (opt.isPresent()) {
            courseDAO.delete(opt.get());
            System.out.println("Deleted.");
        } else {
            System.out.println("Not found.");
        }
    }

    public void listAllCourses() {
        courseDAO.findAll().forEach(System.out::println);
    }
}
