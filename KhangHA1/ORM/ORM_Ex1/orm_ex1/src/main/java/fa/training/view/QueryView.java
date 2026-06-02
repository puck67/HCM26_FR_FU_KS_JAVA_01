package fa.training.view;

import fa.training.entity.Course;
import fa.training.entity.Student;

import java.util.List;

/**
 * View for the five Hibernate query screens.
 */
public class QueryView {

    public void printMenu() {
        System.out.println("""

                --- Hibernate Query Practice ---
                1. [HQL]          Students older than a given age
                2. [HQL + Join]   Students with their enrolled courses
                3. [Named Query]  Find students by name
                4. [Criteria API] Courses with credit > N
                5. [Aggregation]  Student count per course
                6. Back to main menu
                """);
    }

    public void displayStudents(String header, List<Student> students) {
        System.out.println("\n" + header);
        if (students.isEmpty()) {
            System.out.println("  (no results)");
            return;
        }
        students.forEach(s ->
                System.out.printf("  %s%n", s));
    }

    public void displayStudentCoursePairs(List<Object[]> pairs) {
        System.out.println("\nStudents and their enrolled courses:");
        if (pairs.isEmpty()) {
            System.out.println("  (no results)");
            return;
        }
        pairs.forEach(row -> {
            Student s = (Student) row[0];
            Course  c = (Course)  row[1];
            System.out.printf("  %-20s -> %s%n", s.getName(), c.getTitle());
        });
    }

    public void displayCourses(String header, List<Course> courses) {
        System.out.println("\n" + header);
        if (courses.isEmpty()) {
            System.out.println("  (no results)");
            return;
        }
        courses.forEach(c ->
                System.out.printf("  %s%n", c));
    }

    public void displayStudentCountPerCourse(List<Object[]> counts) {
        System.out.println("\nStudent count per course:");
        if (counts.isEmpty()) {
            System.out.println("  (no courses found)");
            return;
        }
        counts.forEach(row ->
                System.out.printf("  %-30s : %s student(s)%n", row[0], row[1]));
    }

    public void printHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice.");
    }
}
