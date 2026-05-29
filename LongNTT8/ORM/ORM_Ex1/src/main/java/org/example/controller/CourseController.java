package org.example.controller;

import org.example.dao.CourseDAO;
import org.example.entity.Course;
import org.example.util.InputUtil;

import java.util.Optional;
import java.util.Scanner;

public class CourseController {
    private final CourseDAO courseDAO = new CourseDAO();

    public void createCourse(Scanner scanner) {
        String title = InputUtil.getString(scanner, "Title: ");
        int credit = InputUtil.getPositiveInt(scanner, "Credit: ");
        Course course = Course.builder().title(title).credit(credit).build();
        courseDAO.save(course);
        System.out.println("Saved: " + course);
    }

    public void updateCourse(Scanner scanner) {
        int id = InputUtil.getPositiveInt(scanner, "Course ID: ");
        Optional<Course> opt = courseDAO.findById(id);
        if (opt.isPresent()) {
            Course course = opt.get();
            System.out.print("New title (" + course.getTitle() + ") - leave blank to skip: ");
            String title = scanner.nextLine().trim();
            if (!title.isEmpty()) course.setTitle(title);

            Integer credit = InputUtil.getOptionalInt(scanner, "New credit (" + course.getCredit() + ") - leave blank to skip: ");
            if (credit != null) course.setCredit(credit);

            courseDAO.update(course);
            System.out.println("Updated.");
        } else {
            System.out.println("Not found.");
        }
    }

    public void deleteCourse(Scanner scanner) {
        int id = InputUtil.getPositiveInt(scanner, "Course ID: ");
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
