package org.example;

import org.example.controller.CourseController;
import org.example.controller.ReportController;
import org.example.controller.StudentController;
import org.example.dao.CourseDAO;
import org.example.dao.StudentDAO;
import org.example.entity.Course;
import org.example.entity.Student;
import org.example.util.HibernateUtil;
import org.example.util.Menu;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentController studentController = new StudentController();
    private static final CourseController courseController = new CourseController();
    private static final ReportController reportController = new ReportController();

    public static void main(String[] args) {
        initializeData();

        Menu studentMenu = new Menu("Student Management");
        studentMenu.addOption("1", "Create Student", () -> studentController.createStudent(scanner));
        studentMenu.addOption("2", "Update Student", () -> studentController.updateStudent(scanner));
        studentMenu.addOption("3", "Delete Student", () -> studentController.deleteStudent(scanner));
        studentMenu.addOption("4", "List All Students", studentController::listAllStudents);
        studentMenu.addOption("5", "Enroll in Course", () -> studentController.enrollInCourse(scanner));
        studentMenu.addOption("6", "Remove from Course", () -> studentController.removeFromCourse(scanner));

        Menu courseMenu = new Menu("Course Management");
        courseMenu.addOption("1", "Create Course", () -> courseController.createCourse(scanner));
        courseMenu.addOption("2", "Update Course", () -> courseController.updateCourse(scanner));
        courseMenu.addOption("3", "Delete Course", () -> courseController.deleteCourse(scanner));
        courseMenu.addOption("4", "List All Courses", courseController::listAllCourses);

        Menu reportMenu = new Menu("Queries & Reports");
        reportMenu.addOption("1", "Find students older than...", () -> reportController.findStudentsOlderThan(scanner));
        reportMenu.addOption("2", "List students and their courses", reportController::findStudentsWithCourses);
        reportMenu.addOption("3", "Find students by name", () -> reportController.findStudentsByName(scanner));
        reportMenu.addOption("4", "Find courses with credit greater than...", () -> reportController.findCoursesWithCreditGreaterThan(scanner));
        reportMenu.addOption("5", "Count students per course", reportController::countStudentsPerCourse);
        reportMenu.addOption("6", "List students paginated", () -> reportController.findAllPaginated(scanner));
        reportMenu.addOption("7", "List students without courses", reportController::findStudentsWithoutCourses);
        Menu mainMenu = new Menu("Main Menu");
        mainMenu.addOption("1", "Student Management", () -> studentMenu.run(scanner));
        mainMenu.addOption("2", "Course Management", () -> courseMenu.run(scanner));
        mainMenu.addOption("3", "Queries & Reports", () -> reportMenu.run(scanner));

        mainMenu.run(scanner);

        System.out.println("Exiting...");
        HibernateUtil.shutdown();
        System.exit(0);
    }

    private static void initializeData() {
        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        
        if (!studentDAO.findAll().isEmpty()) return;

        System.out.println("Insert data...");
        Student s1 = Student.builder().name("John").age(25).build();
        Student s2 = Student.builder().name("Anna").age(22).build();
        Student s3 = Student.builder().name("Peter").age(19).build();

        studentDAO.save(s1);
        studentDAO.save(s2);
        studentDAO.save(s3);

        Course c1 = Course.builder().title("Java").credit(4).build();
        Course c2 = Course.builder().title("DB").credit(3).build();
        Course c3 = Course.builder().title("Web").credit(3).build();

        courseDAO.save(c1);
        courseDAO.save(c2);
        courseDAO.save(c3);

        s1.addCourse(c1);
        s1.addCourse(c2);
        s2.addCourse(c1);
        s3.addCourse(c3);

        studentDAO.update(s1);
        studentDAO.update(s2);
        studentDAO.update(s3);
    }
}
