package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();

    public static void main(String[] args) {
        try {
            boolean exit = false;
            while (!exit) {
                displayMainMenu();
                int choice = readInt("Select an option: ");
                switch (choice) {
                    case 1:
                        studentManagementMenu();
                        break;
                    case 2:
                        courseManagementMenu();
                        break;
                    case 3:
                        enrollmentManagementMenu();
                        break;
                    case 4:
                        queriesAndReportsMenu();
                        break;
                    case 5:
                        exit = true;
                        System.out.println("Exiting... Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
        }
    }

    private static void displayMainMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Main Menu ===\n")
          .append("1. Student Management\n")
          .append("2. Course Management\n")
          .append("3. Enrollment Management\n")
          .append("4. Queries and Reports\n")
          .append("5. Exit\n");
        System.out.print(sb.toString());
    }

    private static void studentManagementMenu() {
        boolean back = false;
        while (!back) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Student Management ---\n")
              .append("1. Create a new student\n")
              .append("2. Update student information\n")
              .append("3. Delete a student\n")
              .append("4. View student by ID\n")
              .append("5. List all students\n")
              .append("6. List students paginated (Bonus)\n")
              .append("7. List students not enrolled (Bonus)\n")
              .append("8. Back to main menu\n");
            System.out.print(sb.toString());

            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    String name = readString("Enter name: ");
                    int age = readInt("Enter age: ");
                    studentService.createStudent(name, age);
                    break;
                case 2:
                    int id = readInt("Enter student ID to update: ");
                    Student studentToUpdate = studentService.getStudentById(id);
                    if (studentToUpdate == null) {
                        System.out.println("Student not found with ID: " + id);
                        break;
                    }
                    String newName = readString("Enter new name (" + studentToUpdate.getName() + "): ");
                    int newAge = readInt("Enter new age (" + studentToUpdate.getAge() + "): ");
                    studentService.updateStudent(id, newName, newAge);
                    break;
                case 3:
                    int delId = readInt("Enter student ID to delete: ");
                    if (studentService.getStudentById(delId) == null) {
                        System.out.println("Student not found with ID: " + delId);
                        break;
                    }
                    studentService.deleteStudent(delId);
                    break;
                case 4:
                    int viewId = readInt("Enter student ID: ");
                    Student s = studentService.getStudentById(viewId);
                    System.out.println(s != null ? s : "Student not found.");
                    break;
                case 5:
                    List<Student> students = studentService.getAllStudents();
                    students.forEach(System.out::println);
                    break;
                case 6:
                    int pageNum = readInt("Enter page number: ");
                    int pageSize = readInt("Enter page size: ");
                    studentService.getAllStudentsPaginated(pageNum, pageSize).forEach(System.out::println);
                    break;
                case 7:
                    System.out.println("--- Students Not Enrolled in Any Course ---");
                    studentService.getStudentsNotEnrolledInAnyCourse().forEach(System.out::println);
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void courseManagementMenu() {
        boolean back = false;
        while (!back) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Course Management ---\n")
              .append("1. Create a new course\n")
              .append("2. Update course information\n")
              .append("3. Delete a course\n")
              .append("4. View course by ID\n")
              .append("5. List all courses\n")
              .append("6. Back to main menu\n");
            System.out.print(sb.toString());

            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    String title = readString("Enter title: ");
                    int credit = readInt("Enter credit: ");
                    courseService.createCourse(title, credit);
                    break;
                case 2:
                    int id = readInt("Enter course ID to update: ");
                    Course courseToUpdate = courseService.getCourseById(id);
                    if (courseToUpdate == null) {
                        System.out.println("Course not found with ID: " + id);
                        break;
                    }
                    String newTitle = readString("Enter new title (" + courseToUpdate.getTitle() + "): ");
                    int newCredit = readInt("Enter new credit (" + courseToUpdate.getCredit() + "): ");
                    courseService.updateCourse(id, newTitle, newCredit);
                    break;
                case 3:
                    int delId = readInt("Enter course ID to delete: ");
                    if (courseService.getCourseById(delId) == null) {
                        System.out.println("Course not found with ID: " + delId);
                        break;
                    }
                    courseService.deleteCourse(delId);
                    break;
                case 4:
                    int viewId = readInt("Enter course ID: ");
                    Course c = courseService.getCourseById(viewId);
                    System.out.println(c != null ? c : "Course not found.");
                    break;
                case 5:
                    List<Course> courses = courseService.getAllCourses();
                    courses.forEach(System.out::println);
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void enrollmentManagementMenu() {
        boolean back = false;
        while (!back) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Enrollment Management ---\n")
              .append("1. Enroll a student in a course\n")
              .append("2. Remove a student from a course\n")
              .append("3. View courses of a student\n")
              .append("4. View students of a course\n")
              .append("5. Back to main menu\n");
            System.out.print(sb.toString());

            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    int sId = readInt("Enter Student ID: ");
                    if (studentService.getStudentById(sId) == null) {
                        System.out.println("Student not found.");
                        break;
                    }
                    int cId = readInt("Enter Course ID: ");
                    if (courseService.getCourseById(cId) == null) {
                        System.out.println("Course not found.");
                        break;
                    }
                    studentService.enrollStudentInCourse(sId, cId);
                    break;
                case 2:
                    int rsId = readInt("Enter Student ID: ");
                    if (studentService.getStudentById(rsId) == null) {
                        System.out.println("Student not found.");
                        break;
                    }
                    int rcId = readInt("Enter Course ID: ");
                    if (courseService.getCourseById(rcId) == null) {
                        System.out.println("Course not found.");
                        break;
                    }
                    studentService.removeStudentFromCourse(rsId, rcId);
                    break;
                case 3:
                    int getSId = readInt("Enter Student ID: ");
                    Set<Course> studentCourses = studentService.getCoursesOfStudent(getSId);
                    if (studentCourses != null) {
                        studentCourses.forEach(System.out::println);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 4:
                    int getCId = readInt("Enter Course ID: ");
                    Set<Student> courseStudents = courseService.getStudentsOfCourse(getCId);
                    if (courseStudents != null) {
                        courseStudents.forEach(System.out::println);
                    } else {
                        System.out.println("Course not found.");
                    }
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void queriesAndReportsMenu() {
        boolean back = false;
        while (!back) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Queries and Reports ---\n")
              .append("1. Find students older than age (HQL)\n")
              .append("2. Find students by name (Named Query)\n")
              .append("3. List students and their courses (HQL Join)\n")
              .append("4. Find courses with credit greater than value (Criteria API)\n")
              .append("5. Count number of students in each course (Aggregation Query)\n")
              .append("6. Find students enrolled in a specific course\n")
              .append("7. Back to main menu\n");
            System.out.print(sb.toString());

            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    int age = readInt("Enter age: ");
                    studentService.getStudentsOlderThan(age).forEach(System.out::println);
                    break;
                case 2:
                    String name = readString("Enter name: ");
                    studentService.getStudentsByName(name).forEach(System.out::println);
                    break;
                case 3:
                    studentService.displayStudentsWithCourseTitles();
                    break;
                case 4:
                    int credit = readInt("Enter credit: ");
                    courseService.getCoursesWithCreditGreaterThan(credit).forEach(System.out::println);
                    break;
                case 5:
                    courseService.displayStudentCountPerCourse();
                    break;
                case 6:
                    int courseId = readInt("Enter Course ID: ");
                    if (courseService.getCourseById(courseId) == null) {
                        System.out.println("Course not found.");
                        break;
                    }
                    studentService.getStudentsByCourse(courseId).forEach(System.out::println);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            System.out.print(prompt);
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return val;
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
