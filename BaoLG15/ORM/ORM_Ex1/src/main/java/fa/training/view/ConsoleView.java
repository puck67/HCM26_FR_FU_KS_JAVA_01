package fa.training.view;

import fa.training.entities.Course;
import fa.training.entities.Student;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner;

    public ConsoleView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printMainMenu() {
        System.out.println("==================================================");
        System.out.println("                   MAIN MENU                      ");
        System.out.println("==================================================");
        System.out.println(" 1. Manage Students");
        System.out.println(" 2. Manage Courses");
        System.out.println(" 3. Manage Enrollments & Queries");
        System.out.println(" 4. Seed Sample Data");
        System.out.println(" 5. Exit");
        System.out.println("==================================================");
    }

    public void printStudentMenu() {
        System.out.println("==================================================");
        System.out.println("            STUDENT MANAGEMENT SUBMENU            ");
        System.out.println("==================================================");
        System.out.println(" 1. Add Student");
        System.out.println(" 2. Update Student");
        System.out.println(" 3. Delete Student");
        System.out.println(" 4. Find Student by ID");
        System.out.println(" 5. List All Students (with Pagination option)");
        System.out.println(" 6. Find Students by Name (Named Query)");
        System.out.println(" 7. Find Students Older Than Age (HQL Query)");
        System.out.println(" 8. List Unenrolled Students");
        System.out.println(" 9. Back to Main Menu");
        System.out.println("==================================================");
    }

    public void printCourseMenu() {
        System.out.println("==================================================");
        System.out.println("            COURSE MANAGEMENT SUBMENU             ");
        System.out.println("==================================================");
        System.out.println(" 1. Add Course");
        System.out.println(" 2. Update Course");
        System.out.println(" 3. Delete Course");
        System.out.println(" 4. Find Course by ID");
        System.out.println(" 5. List All Courses");
        System.out.println(" 6. Find Courses with Credits > Threshold (Criteria API)");
        System.out.println(" 7. Count Students per Course (Aggregation Query)");
        System.out.println(" 8. Back to Main Menu");
        System.out.println("==================================================");
    }

    public void printEnrollmentMenu() {
        System.out.println("==================================================");
        System.out.println("            ENROLLMENT & QUERY SUBMENU            ");
        System.out.println("==================================================");
        System.out.println(" 1. Enroll Student in Course");
        System.out.println(" 2. Remove Student from Course");
        System.out.println(" 3. List Courses of a Student");
        System.out.println(" 4. List Students in a Course");
        System.out.println(" 5. List All Student & Course Enrollments (HQL Join)");
        System.out.println(" 6. Back to Main Menu");
        System.out.println("==================================================");
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid integer.");
            }
        }
    }

    public String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please enter a value.");
        }
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayStudent(Student student) {
        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }

    public void displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(System.out::println);
        }
    }

    public void displayCourse(Course course) {
        if (course == null) {
            System.out.println("Course not found.");
        } else {
            System.out.println(course);
        }
    }

    public void displayCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            courses.forEach(System.out::println);
        }
    }

    public void displayStudentCounts(List<Object[]> counts) {
        if (counts.isEmpty()) {
            System.out.println("No course data found.");
        } else {
            counts.forEach(row -> System.out.println("Course: " + row[0] + " | Enrolled Students: " + row[1]));
        }
    }

    public void displayEnrollments(List<Object[]> enrollments) {
        if (enrollments.isEmpty()) {
            System.out.println("No active course enrollments found.");
        } else {
            enrollments.forEach(pair -> System.out.println("Student: " + pair[0] + " is enrolled in course: " + pair[1]));
        }
    }
}
