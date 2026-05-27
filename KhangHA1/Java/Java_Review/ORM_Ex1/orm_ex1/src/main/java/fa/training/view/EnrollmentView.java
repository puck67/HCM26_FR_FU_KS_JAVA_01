package fa.training.view;

import fa.training.entity.Course;
import fa.training.entity.Student;

import java.util.List;
import java.util.Set;

/**
 * View for Enrollment management screens.
 */
public class EnrollmentView {

    public void printMenu() {
        System.out.println("""

                --- Enrollment Management ---
                1. Enroll student in a course
                2. Remove student from a course
                3. View courses of a student
                4. View students in a course
                5. Students not enrolled in any course
                6. Back to main menu
                """);
    }

    public void displayCoursesOfStudent(String studentName, Set<Course> courses) {
        System.out.println("\nCourses of student \"" + studentName + "\":");
        if (courses.isEmpty()) {
            System.out.println("  (not enrolled in any course)");
            return;
        }
        courses.forEach(c ->
                System.out.printf("  - [%d] %s (%d credits)%n",
                        c.getId(), c.getTitle(), c.getCredit()));
    }

    public void displayStudentsOfCourse(String courseTitle, Set<Student> students) {
        System.out.println("\nStudents enrolled in \"" + courseTitle + "\":");
        if (students.isEmpty()) {
            System.out.println("  (no students enrolled)");
            return;
        }
        students.forEach(s ->
                System.out.printf("  - [%d] %s (age %d)%n",
                        s.getId(), s.getName(), s.getAge()));
    }

    public void displayUnenrolledStudents(List<Student> students) {
        System.out.println("\nStudents not enrolled in any course:");
        if (students.isEmpty()) {
            System.out.println("  (all students are enrolled)");
            return;
        }
        students.forEach(s ->
                System.out.printf("  - [%d] %s%n", s.getId(), s.getName()));
    }

    public void printHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public void printSuccess(String message) {
        System.out.println("  [+] " + message);
    }

    public void printError(String message) {
        System.out.println("  [x] " + message);
    }

    public void printWarning(String message) {
        System.out.println("  [!] " + message);
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice.");
    }
}
