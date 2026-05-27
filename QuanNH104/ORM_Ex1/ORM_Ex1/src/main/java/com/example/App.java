package com.example;

import com.example.controller.CourseController;
import com.example.controller.EnrollmentController;
import com.example.controller.StudentController;
import com.example.model.Course;
import com.example.model.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.util.HibernateUtil;
import com.example.view.CourseView;
import com.example.view.Menu;
import com.example.view.StudentView;

import java.util.Scanner;

public final class App {
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();

    private static final StudentView studentView = new StudentView();
    private static final CourseView courseView = new CourseView();

    private static final StudentController studentController = new StudentController(studentService, studentView);
    private static final CourseController courseController = new CourseController(courseService, courseView);
    private static final EnrollmentController enrollmentController = new EnrollmentController(studentService, courseService, studentView, courseView);

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
        System.setProperty("org.slf4j.simpleLogger.log.org.hibernate", "error");

        insertSampleData();

        var scanner = new Scanner(System.in);

        Menu studentMenu = new Menu("Student Management");
        studentMenu.addItem("Add new Student", () -> studentController.addStudent(scanner));
        studentMenu.addItem("Display all Students", studentController::listStudents);
        studentMenu.addItem("Update Student", () -> studentController.updateStudent(scanner));
        studentMenu.addItem("Delete Student", () -> studentController.deleteStudent(scanner));
        studentMenu.addItem("Search Student by ID", () -> studentController.searchStudent(scanner));
        studentMenu.addItem("Find Students Older Than", () -> studentController.findStudentsOlderThan(scanner));
        studentMenu.addItem("Find Students by Name (Named Query)", () -> studentController.findStudentsByName(scanner));
        studentMenu.addItem("Back to Main Menu", studentMenu::exitMenu);

        Menu courseMenu = new Menu("Course Management");
        courseMenu.addItem("Add new Course", () -> courseController.addCourse(scanner));
        courseMenu.addItem("Display all Courses", courseController::listCourses);
        courseMenu.addItem("Update Course", () -> courseController.updateCourse(scanner));
        courseMenu.addItem("Delete Course", () -> courseController.deleteCourse(scanner));
        courseMenu.addItem("Search Course by ID", () -> courseController.searchCourse(scanner));
        courseMenu.addItem("Find Courses by Credit Greater Than (Criteria)", () -> courseController.findCoursesWithCreditGreaterThan(scanner));
        courseMenu.addItem("Display Student Count per Course (Aggregation)", courseController::displayStudentCountPerCourse);
        courseMenu.addItem("Back to Main Menu", courseMenu::exitMenu);

        Menu enrollmentMenu = new Menu("Enrollment Management");
        enrollmentMenu.addItem("Enroll Student in a Course", () -> enrollmentController.enroll(scanner));
        enrollmentMenu.addItem("Unenroll Student from a Course", () -> enrollmentController.unenroll(scanner));
        enrollmentMenu.addItem("Display all Courses of a Student", () -> enrollmentController.displayCoursesOfStudent(scanner));
        enrollmentMenu.addItem("Display all Students of a Course", () -> enrollmentController.displayStudentsOfCourse(scanner));
        enrollmentMenu.addItem("Display Students and Enrolled Courses (HQL Join)", enrollmentController::listStudentsAndCourses);
        enrollmentMenu.addItem("Back to Main Menu", enrollmentMenu::exitMenu);

        Menu mainMenu = new Menu("Main Menu");
        mainMenu.addItem("Students Management", () -> studentMenu.show(scanner));
        mainMenu.addItem("Courses Management", () -> courseMenu.show(scanner));
        mainMenu.addItem("Enrollments Management", () -> enrollmentMenu.show(scanner));
        mainMenu.addItem("Exit", () -> {
            System.out.println("Thank you for using the system. Goodbye!");
            scanner.close();
            HibernateUtil.shutdown();
            System.exit(0);
        });

        mainMenu.show(scanner);
    }

    private static void insertSampleData() {
        try {
            if (studentService.getAllStudents().isEmpty()) {
                Student john = new Student("John", 21);
                Student anna = new Student("Anna", 22);
                Student mike = new Student("Mike", 19);
                Student sarah = new Student("Sarah", 20);

                studentService.saveStudent(john);
                studentService.saveStudent(anna);
                studentService.saveStudent(mike);
                studentService.saveStudent(sarah);

                Course javaCourse = new Course("Java Programming", 4);
                Course dbCourse = new Course("Database Systems", 3);
                Course webCourse = new Course("Web Development", 2);

                courseService.saveCourse(javaCourse);
                courseService.saveCourse(dbCourse);
                courseService.saveCourse(webCourse);

                studentService.enrollStudentInCourse(john.getId(), javaCourse.getId());
                studentService.enrollStudentInCourse(john.getId(), dbCourse.getId());
                studentService.enrollStudentInCourse(anna.getId(), javaCourse.getId());
                studentService.enrollStudentInCourse(anna.getId(), dbCourse.getId());
                studentService.enrollStudentInCourse(anna.getId(), webCourse.getId());
                studentService.enrollStudentInCourse(mike.getId(), webCourse.getId());
            }
        } catch (Exception e) {
            System.out.println("Failed to insert sample data: " + e.getMessage());
        }
    }
}
