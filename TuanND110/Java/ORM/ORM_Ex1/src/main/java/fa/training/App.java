package fa.training;

import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.dao.QueryDAO;
import fa.training.daoImpl.CourseImpl;
import fa.training.daoImpl.QueryImpl;
import fa.training.daoImpl.StudentImpl;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.menu.ConsoleMenu;

import java.util.List;
import java.util.Scanner;

public class App {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final StudentDAO STUDENT_DAO = new StudentImpl();
    private static final CourseDAO COURSE_DAO = new CourseImpl();
    private static final QueryDAO QUERY_DAO = new QueryImpl();

    public static void main(String[] args) {
        buildMenu().run();
    }

    private static ConsoleMenu buildMenu() {
        return new ConsoleMenu("Student Management System", SCANNER)
                .addOption(1, "Create Student", App::createStudent)
                .addOption(2, "Update Student", App::updateStudent)
                .addOption(3, "Delete Student", App::deleteStudent)
                .addOption(4, "Get Student by ID", App::getStudentById)
                .addOption(5, "Show All Students", App::showAllStudents)
                .addOption(6, "Create Course", App::createCourse)
                .addOption(7, "Update Course", App::updateCourse)
                .addOption(8, "Delete Course", App::deleteCourse)
                .addOption(9, "Show All Courses", App::showAllCourses)
                .addOption(10, "Find students older than age", App::findStudentsOlderThan)
                .addOption(11, "List students and enrolled courses", App::listStudentsAndCourses)
                .addOption(12, "Find students by name", App::findStudentsByName)
                .addOption(13, "Find courses by credit", App::findCoursesByCreditGreaterThan)
                .addOption(14, "Count students per course", App::countStudentsPerCourse)
                .addOption(15, "Find students not enrolled in any course", App::findStudentsNotEnrolledInAnyCourse);
    }

    private static void createStudent() {
        String name = readString("Student name: ");
        int age = readInt("Student age: ");
        boolean saved = STUDENT_DAO.saveStudent(new Student(name, age));
        System.out.println(saved ? "Student created successfully." : "Failed to create student.");
    }

    private static void updateStudent() {
        int id = readInt("Student ID: ");
        String name = readString("New student name: ");
        int age = readInt("New student age: ");
        boolean updated = STUDENT_DAO.updateStudent(new Student(id, name, age));
        System.out.println(updated ? "Student updated successfully." : "Student not found.");
    }

    private static void deleteStudent() {
        int id = readInt("Student ID to delete: ");
        boolean deleted = STUDENT_DAO.deleteStudent(id);
        System.out.println(deleted ? "Student deleted successfully." : "Student not found.");
    }

    private static void getStudentById() {
        int id = readInt("Student ID: ");
        Student student = STUDENT_DAO.getStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }

    private static void showAllStudents() {
        int pageNumber = readInt("Page number (start from 1): ");
        int pageSize = readInt("Page size: ");
        List<Student> students = QUERY_DAO.findStudentsPaged(pageNumber, pageSize);
        if (students.isEmpty()) {
            System.out.println("No students found on this page.");
            return;
        }
        students.forEach(System.out::println);
    }

    private static void createCourse() {
        String title = readString("Course title: ");
        int credit = readInt("Course credit: ");
        boolean saved = COURSE_DAO.saveCourse(new Course(title, credit));
        System.out.println(saved ? "Course created successfully." : "Failed to create course.");
    }

    private static void updateCourse() {
        int id = readInt("Course ID: ");
        String title = readString("New course title: ");
        int credit = readInt("New course credit: ");
        Course course = new Course(title, credit);
        course.setId(id);
        boolean updated = COURSE_DAO.updateCourse(course);
        System.out.println(updated ? "Course updated successfully." : "Course not found.");
    }

    private static void deleteCourse() {
        int id = readInt("Course ID to delete: ");
        boolean deleted = COURSE_DAO.deleteCourse(id);
        System.out.println(deleted ? "Course deleted successfully." : "Course not found.");
    }

    private static void showAllCourses() {
        List<Course> courses = COURSE_DAO.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        courses.forEach(System.out::println);
    }

    private static void findStudentsOlderThan() {
        int age = readInt("Find students older than age: ");
        List<Student> students = QUERY_DAO.findStudentsOlderThan(age);
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        students.forEach(System.out::println);
    }

    private static void listStudentsAndCourses() {
        List<Object[]> rows = QUERY_DAO.listStudentsAndCourses();
        if (rows.isEmpty()) {
            System.out.println("No student-course enrollments found.");
            return;
        }
        rows.forEach(row -> System.out.println(row[0] + " -> " + row[1]));
    }

    private static void findStudentsByName() {
        String name = readString("Find students by name: ");
        List<Student> students = QUERY_DAO.findStudentsByName(name);
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        students.forEach(System.out::println);
    }

    private static void findCoursesByCreditGreaterThan() {
        int credit = readInt("Find courses with credit greater than: ");
        List<Course> courses = QUERY_DAO.findCoursesWithCreditGreaterThan(credit);
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        courses.forEach(System.out::println);
    }

    private static void countStudentsPerCourse() {
        List<Object[]> rows = QUERY_DAO.countStudentsPerCourse();
        if (rows.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        rows.forEach(row -> System.out.println(row[0] + " -> " + row[1] + " students"));
    }

    private static void findStudentsNotEnrolledInAnyCourse() {
        List<Student> students = QUERY_DAO.findStudentsNotEnrolledInAnyCourse();
        if (students.isEmpty()) {
            System.out.println("All students are enrolled in at least one course.");
            return;
        }
        students.forEach(System.out::println);
    }

    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Value cannot be empty.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
}
