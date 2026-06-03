package com.example.app;

import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.util.InputUtil;

import java.util.List;
import java.util.Scanner;

public class ReportHandler {
    private final StudentDAOImpl studentDAOForQueries = new StudentDAOImpl();
    private final CourseDAOImpl courseDAOForQueries = new CourseDAOImpl();
    private final Scanner scanner;

    public ReportHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void queryOlderThan() {
        int age = InputUtil.readInt(scanner, "Enter age: ");
        List<Student> list = studentDAOForQueries.findStudentsOlderThan(age);
        if (list != null) {
            list.forEach(s -> System.out.println(s.getName() + " - " + s.getAge()));
        }
    }

    public void queryByName() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        List<Student> list = studentDAOForQueries.findStudentsByName(name);
        if (list != null) {
            list.forEach(s -> System.out.println(s.getName() + " - " + s.getAge()));
        }
    }

    public void queryStudentsWithCourses() {
        List<Object[]> results = studentDAOForQueries.findStudentsWithCourses();
        if (results != null) {
            results.forEach(arr -> {
                String sName = (String) arr[0];
                String cTitle = (String) arr[1];
                System.out.println("Student: " + sName + " | Course: " + (cTitle == null ? "None" : cTitle));
            });
        }
    }

    public void queryCourseCredit() {
        int credit = InputUtil.readInt(scanner, "Enter minimum credit: ");
        List<Course> list = courseDAOForQueries.findCoursesWithCreditGreaterThan(credit);
        if (list != null) {
            list.forEach(c -> System.out.println(c.getTitle() + " - " + c.getCredit()));
        }
    }

    public void queryCountStudents() {
        List<Object[]> results = courseDAOForQueries.countStudentsPerCourse();
        if (results != null) {
            results.forEach(arr -> {
                String title = (String) arr[0];
                Long count = (Long) arr[1];
                System.out.println("Course: " + title + " | Students enrolled: " + count);
            });
        }
    }

    public void queryStudentsByCourseId() {
        int id = InputUtil.readInt(scanner, "Enter course ID: ");
        List<Student> list = studentDAOForQueries.findStudentsByCourseId(id);
        if (list != null) {
            list.forEach(s -> System.out.println(s.getName()));
        }
    }
}
