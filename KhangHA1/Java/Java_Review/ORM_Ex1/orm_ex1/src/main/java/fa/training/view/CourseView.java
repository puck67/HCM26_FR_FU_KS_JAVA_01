package fa.training.view;

import fa.training.entity.Course;

import java.util.List;

/**
 * View for Course management screens.
 */
public class CourseView {

    public void printMenu() {
        System.out.println("""

                --- Course Management ---
                1. Add new course
                2. Display all courses
                3. Find course by ID
                4. Update course
                5. Delete course
                6. Back to main menu
                """);
    }

    public void displayCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("\nNo courses found.");
            return;
        }
        System.out.printf("%n| %-5s | %-30s | %-7s |%n", "ID", "Title", "Credits");
        System.out.println("|-------|--------------------------------|---------|");
        courses.forEach(c ->
                System.out.printf("| %-5d | %-30s | %-7d |%n",
                        c.getId(), c.getTitle(), c.getCredit()));
    }

    public void displayCourse(Course course) {
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        System.out.println("\n===== COURSE DETAILS =====");
        System.out.println("ID     : " + course.getId());
        System.out.println("Title  : " + course.getTitle());
        System.out.println("Credits: " + course.getCredit());
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
