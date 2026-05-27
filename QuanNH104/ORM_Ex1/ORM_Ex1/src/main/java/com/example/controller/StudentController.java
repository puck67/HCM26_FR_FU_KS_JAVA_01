package com.example.controller;

import com.example.model.Course;
import com.example.model.Student;
import com.example.service.StudentService;
import com.example.view.StudentView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StudentController {
    private final StudentService studentService;
    private final StudentView studentView;

    public StudentController(StudentService studentService, StudentView studentView) {
        this.studentService = studentService;
        this.studentView = studentView;
    }

    public void addStudent(Scanner scanner) {
        System.out.println("\n--- ADD NEW STUDENT ---");
        try {
            System.out.print("Enter Student Name: ");
            var name = scanner.nextLine().trim();

            System.out.print("Enter Student Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            var student = new Student(name, age);
            studentService.saveStudent(student);
            System.out.println("Successfully added new student with ID: " + student.getId());
        } catch (NumberFormatException e) {
            System.out.println("Error: Age must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listStudents() {
        try {
            List<Student> students = studentService.getAllStudents();

            Map<Student, List<Course>> studentCoursesMap = new LinkedHashMap<>();
            for (Student s : students) {
                studentCoursesMap.put(s, studentService.getCoursesOfStudent(s.getId()));
            }
            
            studentView.printStudentList(studentCoursesMap);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateStudent(Scanner scanner) {
        System.out.println("\n--- UPDATE STUDENT ---");
        System.out.print("Enter Student ID to update: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Student existing = studentService.getStudent(id);

            System.out.println("Current details: " + existing);
            System.out.print("Enter new Name (leave empty to keep unchanged): ");
            var name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = existing.getName();
            }

            System.out.print("Enter new Age (leave empty to keep unchanged): ");
            var ageInput = scanner.nextLine().trim();
            int age = ageInput.isEmpty() ? existing.getAge() : Integer.parseInt(ageInput);

            existing.setName(name);
            existing.setAge(age);
            studentService.updateStudent(existing);
            System.out.println("Successfully updated student details!");
        } catch (NumberFormatException e) {
            System.out.println("Error: ID and Age must be integers!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteStudent(Scanner scanner) {
        System.out.println("\n--- DELETE STUDENT ---");
        System.out.print("Enter Student ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Student existing = studentService.getStudent(id);

            System.out.println("Student found: " + existing.getName());
            System.out.print("Are you sure you want to delete this student? (Y/N): ");
            var confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("Y")) {
                studentService.deleteStudent(id);
                System.out.println("Successfully deleted student!");
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

    public void searchStudent(Scanner scanner) {
        System.out.println("\n--- SEARCH STUDENT BY ID ---");
        System.out.print("Enter Student ID to search: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Student student = studentService.getStudent(id);
            var courses = studentService.getCoursesOfStudent(id);
            studentView.printStudentDetails(student, courses);
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void findStudentsOlderThan(Scanner scanner) {
        System.out.println("\n--- FIND STUDENTS OLDER THAN ---");
        System.out.print("Enter Age threshold: ");
        try {
            int ageThreshold = Integer.parseInt(scanner.nextLine().trim());
            List<Student> students = studentService.findStudentsOlderThan(ageThreshold);

            Map<Student, List<Course>> studentCoursesMap = new LinkedHashMap<>();
            for (Student s : students) {
                studentCoursesMap.put(s, studentService.getCoursesOfStudent(s.getId()));
            }
            
            studentView.printStudentList(studentCoursesMap);
        } catch (NumberFormatException e) {
            System.out.println("Error: Age threshold must be an integer!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void findStudentsByName(Scanner scanner) {
        System.out.println("\n--- FIND STUDENTS BY NAME ---");
        System.out.print("Enter Student Name: ");
        try {
            String name = scanner.nextLine().trim();
            List<Student> students = studentService.findStudentsByName(name);

            Map<Student, List<Course>> studentCoursesMap = new LinkedHashMap<>();
            for (Student s : students) {
                studentCoursesMap.put(s, studentService.getCoursesOfStudent(s.getId()));
            }
            
            studentView.printStudentList(studentCoursesMap);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
