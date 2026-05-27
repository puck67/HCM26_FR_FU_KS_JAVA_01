package org.example.controller;

import org.example.dao.StudentDAO;
import org.example.entity.Student;

import java.util.Optional;
import java.util.Scanner;

public class StudentController {
    private final StudentDAO studentDAO = new StudentDAO();

    public void createStudent(Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        Student student = Student.builder().name(name).age(age).build();
        studentDAO.save(student);
        System.out.println("Saved: " + student);
    }

    public void updateStudent(Scanner scanner) {
        System.out.print("Student ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Student> opt = studentDAO.findById(id);
        if (opt.isPresent()) {
            Student student = opt.get();
            System.out.print("New name (" + student.getName() + "): ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) student.setName(name);

            System.out.print("New age (" + student.getAge() + "): ");
            String ageStr = scanner.nextLine().trim();
            if (!ageStr.isEmpty()) student.setAge(Integer.parseInt(ageStr));

            studentDAO.update(student);
            System.out.println("Updated.");
        } else {
            System.out.println("Not found.");
        }
    }

    public void deleteStudent(Scanner scanner) {
        System.out.print("Student ID: ");
        int id = Integer.parseInt(scanner.nextLine());
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
        System.out.print("Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        System.out.print("Course ID: ");
        int courseId = Integer.parseInt(scanner.nextLine());
        studentDAO.enrollInCourse(studentId, courseId);
        System.out.println("Enrolled (if IDs valid).");
    }

    public void removeFromCourse(Scanner scanner) {
        System.out.print("Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());
        System.out.print("Course ID: ");
        int courseId = Integer.parseInt(scanner.nextLine());
        studentDAO.removeFromCourse(studentId, courseId);
        System.out.println("Removed (if IDs valid).");
    }
}
