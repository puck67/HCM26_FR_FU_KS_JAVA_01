package com.example.ui;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import com.example.util.InputUtil;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class StudentUI {
    public static void handleMenu(Scanner scanner, StudentService studentService) {
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("            STUDENT MANAGEMENT             ");
            System.out.println("=========================================");
            System.out.println("1. Create a new student");
            System.out.println("2. Update student information");
            System.out.println("3. Delete a student");
            System.out.println("4. View student by ID");
            System.out.println("5. List all students");
            System.out.println("6. Back to main menu");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-6): ", 1, 6,
                    "Invalid choice! Please choose a number between 1 and 6.");

            switch (choice) {
                case 1 -> addNewStudent(scanner, studentService);
                case 2 -> updateStudent(scanner, studentService);
                case 3 -> deleteStudent(scanner, studentService);
                case 4 -> searchStudentById(scanner, studentService);
                case 5 -> displayAllStudents(studentService);
                case 6 -> System.out.println("Returning to Main Menu...");
            }
        } while (choice != 6);
    }

    private static void addNewStudent(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Add New Student ---");
        String studentName = InputUtil.readNonEmptyString(scanner, "Enter Student Name: ",
                "Student Name cannot be empty.");
        int studentAge = InputUtil.readIntInRange(scanner, "Enter Student Age: ", 1, 120,
                "Student Age must be between 1 and 120.");

        studentService.createStudent(studentName, studentAge);
        System.out.println("Student added successfully!");
    }

    private static void displayAllStudents(StudentService studentService) {
        System.out.println("\n--- Display All Students ---");
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
                Set<Course> courses = s.getCourses();
                if (courses != null && !courses.isEmpty()) {
                    System.out.print("  Enrolled Courses: ");
                    for (Course c : courses) {
                        System.out.print(c.getTitle() + " (ID: " + c.getId() + ") | ");
                    }
                    System.out.println();
                }
            }
        }
    }

    private static void searchStudentById(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Search Student by ID ---");
        int id = InputUtil.readInt(scanner, "Enter Student ID to search: ", "ID must be an integer.");
        Student student = studentService.getStudentById(id);
        if (student != null) {
            System.out.println("Student found: " + student);
            Set<Course> courses = student.getCourses();
            if (courses != null && !courses.isEmpty()) {
                System.out.println("Enrolled Courses:");
                for (Course c : courses) {
                    System.out.println("  - " + c);
                }
            } else {
                System.out.println("No courses enrolled.");
            }
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    private static void updateStudent(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Update Student ---");
        int id = InputUtil.readInt(scanner, "Enter Student ID to update: ", "ID must be an integer.");
        Student student = studentService.getStudentById(id);
        if (student != null) {
            System.out.println("Current details: " + student);
            String newName = InputUtil.readStringOrKeep(scanner, "Enter new Name", student.getName());

            int newAge = student.getAge();
            while (true) {
                System.out.print("Enter new Age (leave empty to keep '" + student.getAge() + "'): ");
                String ageInput = scanner.nextLine().trim();
                if (ageInput.isEmpty()) {
                    break;
                }
                try {
                    int ageVal = Integer.parseInt(ageInput);
                    if (ageVal >= 1 && ageVal <= 120) {
                        newAge = ageVal;
                        break;
                    } else {
                        System.out.println("Age must be between 1 and 120.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Age must be an integer.");
                }
            }

            studentService.updateStudent(id, newName, newAge);
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    private static void deleteStudent(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Delete Student ---");
        int id = InputUtil.readInt(scanner, "Enter Student ID to delete: ", "ID must be an integer.");
        Student student = studentService.getStudentById(id);
        if (student != null) {
            studentService.deleteStudent(id);
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }
}
