package fa;

import fa.config.JPAUtil;
import fa.service.StudentCourseService;
import fa.service.impl.StudentCourseServiceImpl;
import fa.util.Validations;

public class App {

    private static final StudentCourseService service = new StudentCourseServiceImpl();

    public static void main(String[] args) {
        System.out.println("=========================================================================");
        System.out.println("        STARTING HIBERNATE & H2 DATABASE DEMONSTRATION RUNNER           ");
        System.out.println("=========================================================================");

        // Automatically run the full demo suite on startup to satisfy task requirements
        service.executeAutomatedDemo();

        System.out.println("=========================================================================");
        System.out.println("                     AUTOMATED DEMONSTRATION COMPLETE                   ");
        System.out.println("=========================================================================");

        boolean running = true;
        while (running) {
            StringBuilder mainMenu = new StringBuilder();
            mainMenu.append("\n========================== MAIN MENU (LEVEL 1) ==========================\n")
                    .append("1. Student Management\n")
                    .append("2. Course Management\n")
                    .append("3. Enrollment Management\n")
                    .append("4. Query Practice & Reports (Task 5 & Bonus)\n")
                    .append("5. Re-run Complete Automated Demo Suite\n")
                    .append("0. Exit Application\n")
                    .append("======================================================================\n");
            System.out.print(mainMenu.toString());

            int choice = Validations.getMenuChoice("Enter choice: ");

            switch (choice) {
                case 1 -> handleStudentMenu();
                case 2 -> handleCourseMenu();
                case 3 -> handleEnrollmentMenu();
                case 4 -> handleQueryMenu();
                case 5 -> {
                    service.executeAutomatedDemo();
                    System.out.println("=========================================================================");
                    System.out.println("                     AUTOMATED DEMONSTRATION COMPLETE                   ");
                    System.out.println("=========================================================================");
                }
                case 0 -> {
                    System.out.println("Shutting down EntityManagerFactory and exiting application. Goodbye!");
                    JPAUtil.shutdown();
                    running = false;
                }
                default -> System.out.println("Invalid choice! Please choose an option from the menu.");
            }
        }
    }

    // ==========================================
    // SUBMENU 1: STUDENT MANAGEMENT (LEVEL 2)
    // ==========================================
    private static void handleStudentMenu() {
        boolean inSubmenu = true;
        while (inSubmenu) {
            StringBuilder sm = new StringBuilder();
            sm.append("\n------------------ STUDENT MANAGEMENT MENU ------------------\n")
              .append("1. Create new Student\n")
              .append("2. View All Students\n")
              .append("3. Update Student by ID\n")
              .append("4. Delete Student by ID\n")
              .append("0. Back to Main Menu\n")
              .append("-------------------------------------------------------------\n");
            System.out.print(sm.toString());

            int choice = Validations.getMenuChoice("Enter choice: ");

            switch (choice) {
                case 1 -> service.addStudentInteractive();
                case 2 -> service.displayAllStudentsInteractive();
                case 3 -> service.updateStudentInteractive();
                case 4 -> service.deleteStudentInteractive();
                case 0 -> inSubmenu = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    // ==========================================
    // SUBMENU 2: COURSE MANAGEMENT (LEVEL 2)
    // ==========================================
    private static void handleCourseMenu() {
        boolean inSubmenu = true;
        while (inSubmenu) {
            StringBuilder sm = new StringBuilder();
            sm.append("\n------------------- COURSE MANAGEMENT MENU ------------------\n")
              .append("1. Create new Course\n")
              .append("2. View All Courses\n")
              .append("3. Update Course by ID\n")
              .append("4. Delete Course by ID\n")
              .append("0. Back to Main Menu\n")
              .append("-------------------------------------------------------------\n");
            System.out.print(sm.toString());

            int choice = Validations.getMenuChoice("Enter choice: ");

            switch (choice) {
                case 1 -> service.addCourseInteractive();
                case 2 -> service.displayAllCoursesInteractive();
                case 3 -> service.updateCourseInteractive();
                case 4 -> service.deleteCourseInteractive();
                case 0 -> inSubmenu = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    // ==========================================
    // SUBMENU 3: ENROLLMENT MANAGEMENT (LEVEL 2)
    // ==========================================
    private static void handleEnrollmentMenu() {
        boolean inSubmenu = true;
        while (inSubmenu) {
            StringBuilder sm = new StringBuilder();
            sm.append("\n----------------- ENROLLMENT MANAGEMENT MENU ----------------\n")
              .append("1. Enroll Student in a Course\n")
              .append("2. Unenroll Student from a Course\n")
              .append("3. View Courses of a Student\n")
              .append("4. View Students of a Course\n")
              .append("0. Back to Main Menu\n")
              .append("-------------------------------------------------------------\n");
            System.out.print(sm.toString());

            int choice = Validations.getMenuChoice("Enter choice: ");

            switch (choice) {
                case 1 -> service.enrollInteractive();
                case 2 -> service.unenrollInteractive();
                case 3 -> service.displayCoursesOfStudentInteractive();
                case 4 -> service.displayStudentsOfCourseInteractive();
                case 0 -> inSubmenu = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    // ==========================================
    // SUBMENU 4: QUERY PRACTICE & REPORTS (LEVEL 2)
    // ==========================================
    private static void handleQueryMenu() {
        boolean inSubmenu = true;
        while (inSubmenu) {
            StringBuilder sm = new StringBuilder();
            sm.append("\n------------------ QUERY PRACTICE & REPORTS -----------------\n")
              .append("1. Find Students Older Than Age (HQL)\n")
              .append("2. Search Student by Name (Named Query)\n")
              .append("3. Find Courses with Credit > Limit (Criteria API)\n")
              .append("4. Show Student Count per Course (Aggregation)\n")
              .append("5. Find Students with No Enrollments (Bonus)\n")
              .append("6. View Students with Pagination (Bonus)\n")
              .append("0. Back to Main Menu\n")
              .append("-------------------------------------------------------------\n");
            System.out.print(sm.toString());

            int choice = Validations.getMenuChoice("Enter choice: ");

            switch (choice) {
                case 1 -> service.findStudentsOlderThanInteractive();
                case 2 -> service.searchStudentByNameInteractive();
                case 3 -> service.findCoursesWithCreditGreaterThanInteractive();
                case 4 -> service.showStudentCountPerCourseInteractive();
                case 5 -> service.findStudentsWithNoEnrollmentsInteractive();
                case 6 -> service.viewStudentsWithPaginationInteractive();
                case 0 -> inSubmenu = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }
}
