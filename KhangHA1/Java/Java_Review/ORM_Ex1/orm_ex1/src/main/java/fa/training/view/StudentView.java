package fa.training.view;

import fa.training.entity.Student;

import java.util.List;

/**
 * View for Student management screens.
 */
public class StudentView {

    public void printMenu() {
        System.out.println("""

                --- Student Management ---
                1. Add new student
                2. Display all students
                3. Find student by ID
                4. Update student
                5. Delete student
                6. Back to main menu
                """);
    }

    public void displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("\nNo students found.");
            return;
        }
        System.out.printf("%n| %-5s | %-25s | %-5s |%n", "ID", "Name", "Age");
        System.out.println("|-------|---------------------------|-------|");
        students.forEach(s ->
                System.out.printf("| %-5d | %-25s | %-5d |%n",
                        s.getId(), s.getName(), s.getAge()));
    }

    public void displayStudent(Student student) {
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("\n===== STUDENT DETAILS =====");
        System.out.println("ID   : " + student.getId());
        System.out.println("Name : " + student.getName());
        System.out.println("Age  : " + student.getAge());
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
