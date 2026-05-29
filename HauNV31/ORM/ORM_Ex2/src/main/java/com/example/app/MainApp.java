package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();
    private static boolean running = true;

    public static void main(String[] args) {
        try {
            HibernateUtil.getSessionFactory();

            Map<Integer, Runnable> mainMenu = new HashMap<>();
            mainMenu.put(1, MainApp::studentMenu);
            mainMenu.put(2, MainApp::courseMenu);
            mainMenu.put(3, MainApp::enrollmentMenu);
            mainMenu.put(4, MainApp::queryMenu);
            mainMenu.put(5, () -> {
                System.out.println("Goodbye!");
                running = false;
            });

            while (running) {
                printMainMenu();
                int choice = readInt("Enter your choice: ");
                Runnable action = mainMenu.get(choice);
                if (action != null) {
                    action.run();
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }

    private static void printMainMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== MAIN MENU ==========\n")
                .append("1. Student Management\n")
                .append("2. Course Management\n")
                .append("3. Enrollment Management\n")
                .append("4. Queries and Reports\n")
                .append("5. Exit\n")
                .append("===============================");
        System.out.println(sb.toString());
    }

    private static void studentMenu() {
        Map<Integer, Runnable> menu = new HashMap<>();
        final boolean[] back = {false};

        menu.put(1, () -> {
            String name = readString("Enter student name: ");
            int age = readInt("Enter student age: ");
            Student s = studentService.createStudent(name, age);
            System.out.println("Created: " + s);
        });
        menu.put(2, () -> {
            int id = readInt("Enter student id to update: ");
            String name = readString("Enter new name: ");
            int age = readInt("Enter new age: ");
            try {
                Student s = studentService.updateStudent(id, name, age);
                System.out.println("Updated: " + s);
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        });
        menu.put(3, () -> {
            int id = readInt("Enter student id to delete: ");
            try {
                studentService.deleteStudent(id);
                System.out.println("Deleted student id " + id);
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        });
        menu.put(4, () -> {
            int id = readInt("Enter student id: ");
            Student s = studentService.getStudentById(id);
            System.out.println(s == null ? "Not found" : s.toString());
        });
        menu.put(5, () -> {
            List<Student> students = studentService.getAllStudents();
            printList("All Students", students);
        });
        menu.put(6, () -> back[0] = true);

        while (!back[0]) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n----- Student Management -----\n")
                    .append("1. Create a new student\n")
                    .append("2. Update student information\n")
                    .append("3. Delete a student\n")
                    .append("4. View student by ID\n")
                    .append("5. List all students\n")
                    .append("6. Back to main menu");
            System.out.println(sb.toString());
            int choice = readInt("Enter your choice: ");
            Runnable action = menu.get(choice);
            if (action != null) action.run();
            else System.out.println("Invalid choice.");
        }
    }

    private static void courseMenu() {
        Map<Integer, Runnable> menu = new HashMap<>();
        final boolean[] back = {false};

        menu.put(1, () -> {
            String title = readString("Enter course title: ");
            int credit = readInt("Enter credit: ");
            Course c = courseService.createCourse(title, credit);
            System.out.println("Created: " + c);
        });
        menu.put(2, () -> {
            int id = readInt("Enter course id to update: ");
            String title = readString("Enter new title: ");
            int credit = readInt("Enter new credit: ");
            try {
                Course c = courseService.updateCourse(id, title, credit);
                System.out.println("Updated: " + c);
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        });
        menu.put(3, () -> {
            int id = readInt("Enter course id to delete: ");
            try {
                courseService.deleteCourse(id);
                System.out.println("Deleted course id " + id);
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        });
        menu.put(4, () -> {
            int id = readInt("Enter course id: ");
            Course c = courseService.getCourseById(id);
            System.out.println(c == null ? "Not found" : c.toString());
        });
        menu.put(5, () -> {
            List<Course> courses = courseService.getAllCourses();
            printList("All Courses", courses);
        });
        menu.put(6, () -> back[0] = true);

        while (!back[0]) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n----- Course Management -----\n")
                    .append("1. Create a new course\n")
                    .append("2. Update course information\n")
                    .append("3. Delete a course\n")
                    .append("4. View course by ID\n")
                    .append("5. List all courses\n")
                    .append("6. Back to main menu");
            System.out.println(sb.toString());
            int choice = readInt("Enter your choice: ");
            Runnable action = menu.get(choice);
            if (action != null) action.run();
            else System.out.println("Invalid choice.");
        }
    }

    private static void enrollmentMenu() {
        Map<Integer, Runnable> menu = new HashMap<>();
        final boolean[] back = {false};

        menu.put(1, () -> {
            int sid = readInt("Enter student id: ");
            int cid = readInt("Enter course id: ");
            try {
                studentService.enrollStudentInCourse(sid, cid);
                System.out.println("Enrolled student " + sid + " in course " + cid);
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        });
        menu.put(2, () -> {
            int sid = readInt("Enter student id: ");
            int cid = readInt("Enter course id: ");
            try {
                studentService.removeStudentFromCourse(sid, cid);
                System.out.println("Removed student " + sid + " from course " + cid);
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        });
        menu.put(3, () -> {
            int sid = readInt("Enter student id: ");
            try {
                List<Course> courses = studentService.getCoursesOfStudent(sid);
                printList("Courses of student " + sid, courses);
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        });
        menu.put(4, () -> {
            int cid = readInt("Enter course id: ");
            try {
                List<Student> students = courseService.getStudentsOfCourse(cid);
                printList("Students of course " + cid, students);
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        });
        menu.put(5, () -> back[0] = true);

        while (!back[0]) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n----- Enrollment Management -----\n")
                    .append("1. Enroll a student in a course\n")
                    .append("2. Remove a student from a course\n")
                    .append("3. View courses of a student\n")
                    .append("4. View students of a course\n")
                    .append("5. Back to main menu");
            System.out.println(sb.toString());
            int choice = readInt("Enter your choice: ");
            Runnable action = menu.get(choice);
            if (action != null) action.run();
            else System.out.println("Invalid choice.");
        }
    }

    private static void queryMenu() {
        Map<Integer, Runnable> menu = new HashMap<>();
        final boolean[] back = {false};

        menu.put(1, () -> {
            int age = readInt("Enter age threshold: ");
            List<Student> students = studentService.findStudentsOlderThan(age);
            printList("Students older than " + age, students);
        });
        menu.put(2, () -> {
            String name = readString("Enter student name: ");
            List<Student> students = studentService.findStudentsByName(name);
            printList("Students named '" + name + "'", students);
        });
        menu.put(3, () -> {
            List<Object[]> rows = studentService.getStudentsWithCourses();
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Students and Their Courses ---\n");
            if (rows.isEmpty()) {
                sb.append("(no enrollments)");
            } else {
                rows.forEach(r -> sb.append("Student: ").append(r[0])
                        .append(" | Course: ").append(r[1]).append('\n'));
            }
            System.out.println(sb.toString());
        });
        menu.put(4, () -> {
            int credit = readInt("Enter credit threshold: ");
            List<Course> courses = courseService.findCoursesWithCreditGreaterThan(credit);
            printList("Courses with credit > " + credit, courses);
        });
        menu.put(5, () -> {
            List<Object[]> rows = studentService.countStudentsPerCourse();
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Student Count Per Course ---\n");
            if (rows.isEmpty()) {
                sb.append("(no courses)");
            } else {
                rows.forEach(r -> sb.append("Course: ").append(r[0])
                        .append(" | Students enrolled: ").append(r[1]).append('\n'));
            }
            System.out.println(sb.toString());
        });
        menu.put(6, () -> {
            int cid = readInt("Enter course id: ");
            List<Student> students = studentService.findStudentsByCourseId(cid);
            printList("Students enrolled in course " + cid, students);
        });
        menu.put(7, () -> back[0] = true);

        while (!back[0]) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n----- Queries and Reports -----\n")
                    .append("1. Find students older than a given age (HQL)\n")
                    .append("2. Find students by name (Named Query)\n")
                    .append("3. List students and their courses (HQL Join)\n")
                    .append("4. Find courses with credit greater than a value (Criteria API)\n")
                    .append("5. Count number of students in each course (Aggregation)\n")
                    .append("6. Find students enrolled in a specific course\n")
                    .append("7. Back to main menu");
            System.out.println(sb.toString());
            int choice = readInt("Enter your choice: ");
            Runnable action = menu.get(choice);
            if (action != null) action.run();
            else System.out.println("Invalid choice.");
        }
    }

    private static <T> void printList(String header, List<T> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- ").append(header).append(" ---\n");
        if (items == null || items.isEmpty()) {
            sb.append("(empty)");
        } else {
            items.forEach(item -> sb.append(item).append('\n'));
        }
        System.out.println(sb.toString());
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
