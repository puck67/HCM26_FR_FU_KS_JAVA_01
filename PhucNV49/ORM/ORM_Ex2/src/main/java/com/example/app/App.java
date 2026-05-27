package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class App {
    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();

    public static void main(String[] args) {
        // Initialize connection and seed initial demo data if database is empty
        try {
            seedDataIfNeeded();
        } catch (Exception e) {
            Menu.printError("Failed to seed initial data: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        Menu mainMenu = new Menu("Training Center Manager", scanner);

        mainMenu.addItem("Student Management", () -> showStudentMenu(scanner));
        mainMenu.addItem("Course Management", () -> showCourseMenu(scanner));
        mainMenu.addItem("Enrollment Management", () -> showEnrollmentMenu(scanner));
        mainMenu.addItem("Queries and Reports", () -> showQueriesMenu(scanner));
        mainMenu.addItem("Exit", () -> {
            Menu.printSuccess("Shutting down Hibernate SessionFactory...");
            try {
                HibernateUtil.shutdown();
            } catch (Exception e) {
                Menu.printError("Error closing SessionFactory: " + e.getMessage());
            }
            Menu.printSuccess("Goodbye!");
            mainMenu.setExit(true);
        });

        mainMenu.displayAndRun();
        scanner.close();
    }

    private static void seedDataIfNeeded() {
        if (studentService.getAllStudents().isEmpty() && courseService.getAllCourses().isEmpty()) {
            System.out.println(Menu.YELLOW + "Database is empty. Seeding initial training center records..." + Menu.RESET);
            
            // Seed Courses
            Course c1 = courseService.createCourse("Java Programming", 4);
            Course c2 = courseService.createCourse("Database Systems", 3);
            Course c3 = courseService.createCourse("Web Development", 3);
            Course c4 = courseService.createCourse("Software Architecture", 4);
            
            // Seed Students
            Student s1 = studentService.createStudent("Alice Nguyen", 20);
            Student s2 = studentService.createStudent("Bob Tran", 22);
            Student s3 = studentService.createStudent("Charlie Pham", 19);
            Student s4 = studentService.createStudent("David Le", 25);
            
            // Seed Enrollments
            studentService.enrollStudentInCourse(s1.getId(), c1.getId());
            studentService.enrollStudentInCourse(s1.getId(), c2.getId());
            studentService.enrollStudentInCourse(s2.getId(), c1.getId());
            studentService.enrollStudentInCourse(s3.getId(), c3.getId());
            studentService.enrollStudentInCourse(s4.getId(), c1.getId());
            studentService.enrollStudentInCourse(s4.getId(), c2.getId());
            studentService.enrollStudentInCourse(s4.getId(), c4.getId());
            
            Menu.printSuccess("Database seeded successfully with initial courses, students, and enrollments.");
        }
    }

    // ==========================================
    // SUB-MENUS
    // ==========================================

    private static void showStudentMenu(Scanner scanner) {
        Menu sub = new Menu("Student Management", scanner);
        
        sub.addItem("Create a new student", () -> {
            String name = sub.readString("Enter student name: ");
            int age = sub.readInt("Enter student age: ", 1, 120);
            Student s = studentService.createStudent(name, age);
            Menu.printSuccess("Student created successfully with ID: " + s.getId());
        });
        
        sub.addItem("Update student information", () -> {
            int id = sub.readInt("Enter student ID to update: ");
            String name = sub.readString("Enter new name: ");
            int age = sub.readInt("Enter new age: ", 1, 120);
            studentService.updateStudent(id, name, age);
            Menu.printSuccess("Student updated successfully.");
        });
        
        sub.addItem("Delete a student", () -> {
            int id = sub.readInt("Enter student ID to delete: ");
            studentService.deleteStudent(id);
            Menu.printSuccess("Student deleted successfully.");
        });
        
        sub.addItem("View student by ID", () -> {
            int id = sub.readInt("Enter student ID: ");
            Student s = studentService.getStudentById(id);
            if (s == null) {
                Menu.printWarning("No student found with ID " + id);
                return;
            }
            System.out.println(Menu.GREEN + "Student Profile Details:" + Menu.RESET);
            System.out.println("  ID:   " + s.getId());
            System.out.println("  Name: " + s.getName());
            System.out.println("  Age:  " + s.getAge());
            System.out.print("  Enrolled Courses: ");
            if (s.getCourses().isEmpty()) {
                System.out.println("None");
            } else {
                List<String> courseList = new ArrayList<>();
                for (Course c : s.getCourses()) {
                    courseList.add(c.getTitle() + " (ID:" + c.getId() + ")");
                }
                System.out.println(String.join(", ", courseList));
            }
        });
        
        sub.addItem("List all students", () -> {
            printStudentsTable(studentService.getAllStudents());
        });
        
        sub.addItem("Back to main menu", () -> sub.setExit(true));
        
        sub.displayAndRun();
    }

    private static void showCourseMenu(Scanner scanner) {
        Menu sub = new Menu("Course Management", scanner);
        
        sub.addItem("Create a new course", () -> {
            String title = sub.readString("Enter course title: ");
            int credit = sub.readInt("Enter credit value: ", 1, 10);
            Course c = courseService.createCourse(title, credit);
            Menu.printSuccess("Course created successfully with ID: " + c.getId());
        });
        
        sub.addItem("Update course information", () -> {
            int id = sub.readInt("Enter course ID to update: ");
            String title = sub.readString("Enter new title: ");
            int credit = sub.readInt("Enter new credit value: ", 1, 10);
            courseService.updateCourse(id, title, credit);
            Menu.printSuccess("Course updated successfully.");
        });
        
        sub.addItem("Delete a course", () -> {
            int id = sub.readInt("Enter course ID to delete: ");
            courseService.deleteCourse(id);
            Menu.printSuccess("Course deleted successfully.");
        });
        
        sub.addItem("View course by ID", () -> {
            int id = sub.readInt("Enter course ID: ");
            Course c = courseService.getCourseById(id);
            if (c == null) {
                Menu.printWarning("No course found with ID " + id);
                return;
            }
            System.out.println(Menu.GREEN + "Course Details:" + Menu.RESET);
            System.out.println("  ID:     " + c.getId());
            System.out.println("  Title:  " + c.getTitle());
            System.out.println("  Credit: " + c.getCredit());
            System.out.print("  Enrolled Students: ");
            if (c.getStudents().isEmpty()) {
                System.out.println("None");
            } else {
                List<String> studentList = new ArrayList<>();
                for (Student s : c.getStudents()) {
                    studentList.add(s.getName() + " (ID:" + s.getId() + ")");
                }
                System.out.println(String.join(", ", studentList));
            }
        });
        
        sub.addItem("List all courses", () -> {
            printCoursesTable(courseService.getAllCourses());
        });
        
        sub.addItem("Back to main menu", () -> sub.setExit(true));
        
        sub.displayAndRun();
    }

    private static void showEnrollmentMenu(Scanner scanner) {
        Menu sub = new Menu("Enrollment Management", scanner);
        
        sub.addItem("Enroll a student in a course", () -> {
            int studentId = sub.readInt("Enter Student ID: ");
            int courseId = sub.readInt("Enter Course ID: ");
            studentService.enrollStudentInCourse(studentId, courseId);
            Menu.printSuccess("Successfully enrolled student in the course.");
        });
        
        sub.addItem("Remove a student from a course", () -> {
            int studentId = sub.readInt("Enter Student ID: ");
            int courseId = sub.readInt("Enter Course ID: ");
            studentService.removeStudentFromCourse(studentId, courseId);
            Menu.printSuccess("Successfully removed student from the course.");
        });
        
        sub.addItem("View courses of a student", () -> {
            int studentId = sub.readInt("Enter Student ID: ");
            Set<Course> courses = studentService.getCoursesOfStudent(studentId);
            System.out.println(Menu.GREEN + "Courses for Student ID " + studentId + ":" + Menu.RESET);
            printCoursesTable(new ArrayList<>(courses));
        });
        
        sub.addItem("View students of a course", () -> {
            int courseId = sub.readInt("Enter Course ID: ");
            Set<Student> students = courseService.getStudentsOfCourse(courseId);
            System.out.println(Menu.GREEN + "Students enrolled in Course ID " + courseId + ":" + Menu.RESET);
            printStudentsTable(new ArrayList<>(students));
        });
        
        sub.addItem("Back to main menu", () -> sub.setExit(true));
        
        sub.displayAndRun();
    }

    private static void showQueriesMenu(Scanner scanner) {
        Menu sub = new Menu("Queries and Reports", scanner);
        
        sub.addItem("Find students older than a given age (HQL)", () -> {
            int age = sub.readInt("Enter age threshold: ", 1, 120);
            List<Student> results = studentService.findStudentsOlderThan(age);
            System.out.println(Menu.GREEN + "Students older than " + age + ":" + Menu.RESET);
            printStudentsTable(results);
        });
        
        sub.addItem("Find students by name (Named Query)", () -> {
            String name = sub.readString("Enter student name: ");
            List<Student> results = studentService.findStudentsByName(name);
            System.out.println(Menu.GREEN + "Students named \"" + name + "\":" + Menu.RESET);
            printStudentsTable(results);
        });
        
        sub.addItem("List students and their courses (HQL Join)", () -> {
            List<Object[]> results = studentService.getStudentsAndCourseTitles();
            if (results.isEmpty()) {
                Menu.printWarning("No active enrollments to show.");
                return;
            }
            System.out.println(Menu.PURPLE + "   ┌──────────────────────────────┬──────────────────────────────┐" + Menu.RESET);
            System.out.println(Menu.PURPLE + "   │ Student Name                 │ Course Title                 │" + Menu.RESET);
            System.out.println(Menu.PURPLE + "   ├──────────────────────────────┼──────────────────────────────┤" + Menu.RESET);
            for (Object[] row : results) {
                Student s = (Student) row[0];
                String courseTitle = (String) row[1];
                System.out.printf(Menu.PURPLE + "   │" + Menu.WHITE + " %-28s " + Menu.PURPLE + "│" + Menu.WHITE + " %-28s " + Menu.PURPLE + "│%n" + Menu.RESET, s.getName(), courseTitle);
            }
            System.out.println(Menu.PURPLE + "   └──────────────────────────────┴──────────────────────────────┘" + Menu.RESET);
        });
        
        sub.addItem("Find courses with credit greater than a value (Criteria API)", () -> {
            int credit = sub.readInt("Enter credit value threshold: ", 0, 10);
            List<Course> results = courseService.findCoursesByCreditGreaterThan(credit);
            System.out.println(Menu.GREEN + "Courses with credits > " + credit + ":" + Menu.RESET);
            printCoursesTable(results);
        });
        
        sub.addItem("Count number of students in each course (Aggregation Query)", () -> {
            List<Object[]> results = courseService.getStudentCountPerCourse();
            if (results.isEmpty()) {
                Menu.printWarning("No courses exist in the database.");
                return;
            }
            System.out.println(Menu.PURPLE + "   ┌──────────────────────────────┬───────────────────┐" + Menu.RESET);
            System.out.println(Menu.PURPLE + "   │ Course Title                 │ Enrolled Students │" + Menu.RESET);
            System.out.println(Menu.PURPLE + "   ├──────────────────────────────┼───────────────────┤" + Menu.RESET);
            for (Object[] row : results) {
                String courseTitle = (String) row[0];
                Long studentCount = (Long) row[1];
                System.out.printf(Menu.PURPLE + "   │" + Menu.WHITE + " %-28s " + Menu.PURPLE + "│" + Menu.WHITE + " %-17d " + Menu.PURPLE + "│%n" + Menu.RESET, courseTitle, studentCount);
            }
            System.out.println(Menu.PURPLE + "   └──────────────────────────────┴───────────────────┘" + Menu.RESET);
        });
        
        sub.addItem("Find students enrolled in a specific course", () -> {
            int courseId = sub.readInt("Enter course ID: ");
            List<Student> results = studentService.findStudentsEnrolledInCourse(courseId);
            System.out.println(Menu.GREEN + "Students registered in Course ID " + courseId + ":" + Menu.RESET);
            printStudentsTable(results);
        });
        
        sub.addItem("Back to main menu", () -> sub.setExit(true));
        
        sub.displayAndRun();
    }

    // ==========================================
    // VISUAL TABLE RENDERERS
    // ==========================================

    private static void printStudentsTable(List<Student> students) {
        if (students.isEmpty()) {
            Menu.printWarning("No student records available.");
            return;
        }
        System.out.println(Menu.PURPLE + "   ┌──────┬──────────────────────────────┬──────┐" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   │  ID  │ Student Name                 │ Age  │" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   ├──────┼──────────────────────────────┼──────┤" + Menu.RESET);
        for (Student s : students) {
            System.out.printf(Menu.PURPLE + "   │" + Menu.WHITE + " %-4d " + Menu.PURPLE + "│" + Menu.WHITE + " %-28s " + Menu.PURPLE + "│" + Menu.WHITE + " %-4d " + Menu.PURPLE + "│%n" + Menu.RESET, s.getId(), s.getName(), s.getAge());
        }
        System.out.println(Menu.PURPLE + "   └──────┴──────────────────────────────┴──────┘" + Menu.RESET);
    }

    private static void printCoursesTable(List<Course> courses) {
        if (courses.isEmpty()) {
            Menu.printWarning("No course records available.");
            return;
        }
        System.out.println(Menu.PURPLE + "   ┌──────┬──────────────────────────────┬────────┐" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   │  ID  │ Course Title                 │ Credit │" + Menu.RESET);
        System.out.println(Menu.PURPLE + "   ├──────┼──────────────────────────────┼────────┤" + Menu.RESET);
        for (Course c : courses) {
            System.out.printf(Menu.PURPLE + "   │" + Menu.WHITE + " %-4d " + Menu.PURPLE + "│" + Menu.WHITE + " %-28s " + Menu.PURPLE + "│" + Menu.WHITE + " %-6d " + Menu.PURPLE + "│%n" + Menu.RESET, c.getId(), c.getTitle(), c.getCredit());
        }
        System.out.println(Menu.PURPLE + "   └──────┴──────────────────────────────┴────────┘" + Menu.RESET);
    }
}
