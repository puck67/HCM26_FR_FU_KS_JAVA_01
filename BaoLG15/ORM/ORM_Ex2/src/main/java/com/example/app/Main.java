package com.example.app;

import com.example.util.ConsoleUtil;
import com.example.util.HibernateUtil;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMainMenu();
                int choice = ConsoleUtil.readInt(scanner, "Enter your choice: ");
                switch (choice) {
                    case 1 -> MenuController.handleStudentManagement(scanner);
                    case 2 -> MenuController.handleCourseManagement(scanner);
                    case 3 -> MenuController.handleEnrollmentManagement(scanner);
                    case 4 -> MenuController.handleQueriesAndReports(scanner);
                    case 5 -> {
                        System.out.println("Exiting the application...");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please choose 1-5.");
                }
            }
        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi hệ thống: " + e.getMessage());
        } finally {
            HibernateUtil.shutdown();
        }
    }

    private static void printMainMenu() {
        System.out.println("\n========== Main Menu ==========");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Queries and Reports");
        System.out.println("5. Exit");
        System.out.println("===============================");
    }
}