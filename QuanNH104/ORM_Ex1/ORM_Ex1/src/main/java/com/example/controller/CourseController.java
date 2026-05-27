package com.example.controller;

import com.example.model.Course;
import com.example.model.Student;
import com.example.service.CourseService;
import com.example.view.CourseView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CourseController {
    private final CourseService courseService;
    private final CourseView courseView;

    public CourseController(CourseService courseService, CourseView courseView) {
        this.courseService = courseService;
        this.courseView = courseView;
    }

    public void addCourse(Scanner scanner) {
        System.out.println("\n--- ADD NEW COURSE ---");
        try {
            System.out.print("Enter Course Title: ");
            var title = scanner.nextLine().trim();

            System.out.print("Enter Course Credit: ");
            int credit = Integer.parseInt(scanner.nextLine().trim());

            var course = new Course(title, credit);
            courseService.saveCourse(course);
            System.out.println("Successfully added new course with ID: " + course.getId());
        } catch (NumberFormatException e) {
            System.out.println("Error: Credit must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();

            Map<Course, List<Student>> courseStudentsMap = new LinkedHashMap<>();
            for (Course c : courses) {
                courseStudentsMap.put(c, courseService.getStudentsOfCourse(c.getId()));
            }
            
            courseView.printCourseList(courseStudentsMap);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateCourse(Scanner scanner) {
        System.out.println("\n--- UPDATE COURSE ---");
        System.out.print("Enter Course ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Course existing = courseService.getCourse(id);

            System.out.println("Current details: " + existing);
            System.out.print("Enter new Title (leave empty to keep unchanged): ");
            var title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                title = existing.getTitle();
            }

            System.out.print("Enter new Credit (leave empty to keep unchanged): ");
            var creditInput = scanner.nextLine().trim();
            int credit = creditInput.isEmpty() ? existing.getCredit() : Integer.parseInt(creditInput);

            existing.setTitle(title);
            existing.setCredit(credit);
            courseService.updateCourse(existing);
            System.out.println("Successfully updated course details!");
        } catch (NumberFormatException e) {
            System.out.println("Error: ID and Credit must be integers!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteCourse(Scanner scanner) {
        System.out.println("\n--- DELETE COURSE ---");
        System.out.print("Enter Course ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Course existing = courseService.getCourse(id);

            System.out.println("Course found: " + existing.getTitle());
            System.out.print("Are you sure you want to delete this course? (Y/N): ");
            var confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("Y")) {
                courseService.deleteCourse(id);
                System.out.println("Successfully deleted course!");
            } else {
                System.out.println("Delete operation canceled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchCourse(Scanner scanner) {
        System.out.println("\n--- SEARCH COURSE BY ID ---");
        System.out.print("Enter Course ID to search: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Course course = courseService.getCourse(id);
            var students = courseService.getStudentsOfCourse(id);
            courseView.printCourseDetails(course, students);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
