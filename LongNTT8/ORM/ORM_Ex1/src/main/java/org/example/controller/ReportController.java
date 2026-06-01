package org.example.controller;

import org.example.dao.CourseDAO;
import org.example.dao.StudentDAO;
import org.example.entity.Course;
import org.example.entity.Student;
import org.example.util.InputUtil;

import java.util.List;
import java.util.Scanner;

public class ReportController {
    private final StudentDAO studentDAO = new StudentDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    public void findStudentsOlderThan(Scanner scanner) {
        int age = InputUtil.getInt(scanner, "Enter age: ");
        System.out.println("--- Students older than " + age + " ---");
        List<Student> list = studentDAO.findStudentsOlderThan(age);
        if (list.isEmpty()) System.out.println("No records found.");
        else list.forEach(System.out::println);
    }

    public void findStudentsWithCourses() {
        System.out.println("--- Students & Courses ---");
        List<Student> list = studentDAO.findStudentsWithCourses();
        if (list.isEmpty()) System.out.println("No records found.");
        else list.forEach(s -> System.out.println(s + " -> " + s.getCourses()));
    }

    public void findStudentsByName(Scanner scanner) {
        String name = InputUtil.getString(scanner, "Enter name: ");
        System.out.println("--- Students named '" + name + "' ---");
        List<Student> list = studentDAO.findStudentsByName(name);
        if (list.isEmpty()) System.out.println("No records found.");
        else list.forEach(System.out::println);
    }

    public void findCoursesWithCreditGreaterThan(Scanner scanner) {
        int credit = InputUtil.getInt(scanner, "Enter credit limit: ");
        System.out.println("--- Courses with credit > " + credit + " ---");
        List<Course> list = courseDAO.findCoursesWithCreditGreaterThan(credit);
        if (list.isEmpty()) System.out.println("No records found.");
        else list.forEach(System.out::println);
    }

    public void countStudentsPerCourse() {
        System.out.println("--- Student count per course ---");
        List<Object[]> list = courseDAO.countStudentsPerCourse();
        if (list.isEmpty()) System.out.println("No records found.");
        else list.forEach(row -> System.out.println(row[0] + ": " + row[1] + " students"));
    }

    public void findAllPaginated(Scanner scanner) {
        int pageNumber = InputUtil.getPositiveInt(scanner, "Enter page number (1-based): ");
        int pageSize = InputUtil.getPositiveInt(scanner, "Enter page size: ");
        System.out.println("--- Paging (Page " + pageNumber + ", Size " + pageSize + ") ---");
        List<Student> list = studentDAO.findAllPaginated(pageNumber, pageSize);
        if (list.isEmpty()) System.out.println("No records found.");
        else list.forEach(System.out::println);
    }

    public void findStudentsWithoutCourses() {
        System.out.println("--- Students without courses ---");
        List<Student> list = studentDAO.findStudentsWithoutCourses();
        if (list.isEmpty()) System.out.println("No records found.");
        else list.forEach(System.out::println);
    }
}
