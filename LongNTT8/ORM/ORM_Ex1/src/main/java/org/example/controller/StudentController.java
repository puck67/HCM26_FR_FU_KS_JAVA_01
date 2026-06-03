package org.example.controller;

import org.example.dao.StudentDAO;
import org.example.entity.Student;
import org.example.util.InputUtil;

import java.util.Optional;
import java.util.Scanner;

public class StudentController {
    private final StudentDAO studentDAO = new StudentDAO();

    public void createStudent(Scanner scanner) {
        String name = InputUtil.getString(scanner, "Name: ");
        int age = InputUtil.getPositiveInt(scanner, "Age: ");
        Student student = Student.builder().name(name).age(age).build();
        studentDAO.save(student);
        System.out.println("Saved: " + student);
    }

    public void updateStudent(Scanner scanner) {
        int id = InputUtil.getPositiveInt(scanner, "Student ID: ");
        Optional<Student> opt = studentDAO.findById(id);
        if (opt.isPresent()) {
            Student student = opt.get();
            System.out.print("New name (" + student.getName() + ") - leave blank to skip: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) student.setName(name);

            Integer age = InputUtil.getOptionalInt(scanner, "New age (" + student.getAge() + ") - leave blank to skip: ");
            if (age != null) student.setAge(age);

            studentDAO.update(student);
            System.out.println("Updated.");
        } else {
            System.out.println("Not found.");
        }
    }

    public void deleteStudent(Scanner scanner) {
        int id = InputUtil.getPositiveInt(scanner, "Student ID: ");
        Optional<Student> opt = studentDAO.findById(id);
        if (opt.isPresent()) {
            studentDAO.delete(opt.get());
            System.out.println("Deleted.");
        } else {
            System.out.println("Not found.");
        }
    }

    public void listAllStudents() {
        studentDAO.findAll().forEach(System.out::println);
    }

    public void enrollInCourse(Scanner scanner) {
        int studentId = InputUtil.getPositiveInt(scanner, "Student ID: ");
        int courseId = InputUtil.getPositiveInt(scanner, "Course ID: ");
        String message = studentDAO.enrollInCourse(studentId, courseId);
        System.out.println(message != null ? message : "An error occurred.");
    }

    public void removeFromCourse(Scanner scanner) {
        int studentId = InputUtil.getPositiveInt(scanner, "Student ID: ");
        int courseId = InputUtil.getPositiveInt(scanner, "Course ID: ");
        String message = studentDAO.removeFromCourse(studentId, courseId);
        System.out.println(message != null ? message : "An error occurred.");
    }
}
