package com.example.app;

import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.ui.CourseUI;
import com.example.ui.EnrollmentUI;
import com.example.ui.QueryUI;
import com.example.ui.StudentUI;
import com.example.util.InputUtil;
import com.example.util.HibernateUtil;

import java.util.Scanner;

public class Main {
    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("            Main Menu          ");
            System.out.println("=========================================");
            System.out.println("1. Student Management");
            System.out.println("2. Course Management");
            System.out.println("3. Enrollment Management");
            System.out.println("4. Queries and Reports");
            System.out.println("5. Exit");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-5): ", 1, 5,
                    "Invalid choice! Please choose a number between 1 and 5.");

            switch (choice) {
                case 1 -> StudentUI.handleMenu(scanner, studentService);
                case 2 -> CourseUI.handleMenu(scanner, courseService);
                case 3 -> EnrollmentUI.handleMenu(scanner, studentService, courseService);
                case 4 -> QueryUI.handleMenu(scanner, studentService, courseService);
                case 5 -> System.out.println("Exiting... Goodbye!");
            }
        } while (choice != 5);

        scanner.close();
        HibernateUtil.shutdown();
    }
}
