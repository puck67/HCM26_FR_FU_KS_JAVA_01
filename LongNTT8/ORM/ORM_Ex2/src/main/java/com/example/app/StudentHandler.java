package com.example.app;

import com.example.entity.Student;
import com.example.service.StudentService;
import com.example.service.impl.StudentServiceImpl;

import com.example.util.InputUtil;

import java.util.List;
import java.util.Scanner;

public class StudentHandler {
    private final StudentService studentService = new StudentServiceImpl();
    private final Scanner scanner;

    public StudentHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void createStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        int age = InputUtil.readInt(scanner, "Enter student age: ");
        studentService.createStudent(name, age);
        System.out.println("Student created.");
    }

    public void updateStudent() {
        int id = InputUtil.readInt(scanner, "Enter student ID to update: ");
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        int age = InputUtil.readInt(scanner, "Enter new age: ");
        studentService.updateStudent(id, name, age);
        System.out.println("Student updated.");
    }

    public void deleteStudent() {
        int id = InputUtil.readInt(scanner, "Enter student ID to delete: ");
        studentService.deleteStudent(id);
        System.out.println("Student deleted.");
    }

    public void viewStudentById() {
        int id = InputUtil.readInt(scanner, "Enter student ID: ");
        Student s = studentService.getStudentById(id);
        if (s != null) {
            System.out.println("--------------------------------------------------");
            System.out.printf("%-10s | %-20s | %-10s%n", "ID", "Name", "Age");
            System.out.println("--------------------------------------------------");
            System.out.printf("%-10d | %-20s | %-10d%n", s.getId(), s.getName(), s.getAge());
            System.out.println("--------------------------------------------------");
        } else {
            System.out.println("Not found.");
        }
    }

    public void listAllStudents() {
        List<Student> list = studentService.getAllStudents();
        System.out.println("--------------------------------------------------");
        System.out.printf("%-10s | %-20s | %-10s%n", "ID", "Name", "Age");
        System.out.println("--------------------------------------------------");
        list.forEach(s -> System.out.printf("%-10d | %-20s | %-10d%n", s.getId(), s.getName(), s.getAge()));
        System.out.println("--------------------------------------------------");
    }
}
