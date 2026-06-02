package org.example;

import org.example.db.DBConnection;
import org.example.model.Student;
import org.example.service.StudentService;

import java.util.*;
import java.util.function.Consumer;


public class Main {

    private static final StudentService service = new StudentService();
    private static final Scanner scanner = new Scanner(System.in);

    // ─── ANSI Colors ─────────────────────────────────────────────────────────────
    private static final String RESET  = "\033[0m";
    private static final String CYAN   = "\033[1;36m";
    private static final String GREEN  = "\033[1;32m";
    private static final String RED    = "\033[1;31m";
    private static final String YELLOW = "\033[1;33m";
    private static final String BLUE   = "\033[1;34m";

    // ─── Table layout ────────────────────────────────────────────────────────────
    private static final String TABLE_LINE =
            "+------------+---------------------------+--------------------------------+--------------+-------+";
    private static final String TABLE_HEADER =
            "| ID         | Name                      | Email                          | Phone        | GPA   |";

    // ─── Lambda print helpers (Consumer) ─────────────────────────────────────────
    private static final Consumer<String> printSuccess =
            msg -> System.out.println(GREEN + "  ✓ " + msg + RESET);

    private static final Consumer<String> printError =
            msg -> System.out.println(RED   + "  ✗ " + msg + RESET);

    private static final Consumer<String> printInfo =
            msg -> System.out.println(YELLOW + "  " + msg + RESET);


    private static Map<Integer, Runnable> buildMenuActions() {
        Map<Integer, Runnable> actions = new LinkedHashMap<>();
        actions.put(1, Main::addStudent);
        actions.put(2, Main::displayAll);
        actions.put(3, Main::updateStudent);
        actions.put(4, Main::deleteStudent);
        actions.put(5, Main::searchStudent);
        return actions;
    }


    public static void main(String[] args) {
        printBanner();

        try {
            DBConnection.getConnection();
        } catch (Exception e) {
            printError.accept("Cannot connect to database: " + e.getMessage());
            printInfo.accept("Check DB_URL, DB_USER, DB_PASS in DBConnection.java");
            return;
        }

        Map<Integer, Runnable> actions = buildMenuActions();
        boolean[] running = {true};

        while (running[0]) {
            printMenu();
            int choice = readInt("  Enter your choice: ");

            if (choice == 6) {
                running[0] = false;
                System.out.println(GREEN + "\n  Goodbye! Have a great day!" + RESET);
                DBConnection.closeConnection();
            } else {
                // Lambda dispatch: get action or print invalid
                Optional.ofNullable(actions.get(choice))
                        .ifPresent(Runnable::run);

                if (!actions.containsKey(choice)) {
                    printError.accept("Invalid choice. Please select 1-6.");
                }
            }
        }

        scanner.close();
    }

    // ─── 1. Add ──────────────────────────────────────────────────────────────────

    private static void addStudent() {
        System.out.println(CYAN + "\n  -- Add New Student --" + RESET);
        String id    = readString("  Student ID   : ");
        String name  = readString("  Full Name    : ");
        String email = readString("  Email        : ");
        String phone = readString("  Phone        : ");
        double gpa   = readDouble("  GPA (0-4)    : ");

        Student s = new Student(id.trim(), name.trim(), email.trim(), phone.trim(), gpa);
        handleResult(service.addStudent(s));
    }

    // ─── 2. Display All ──────────────────────────────────────────────────────────

    private static void displayAll() {
        System.out.println(CYAN + "\n  -- All Students --" + RESET);
        List<Student> list = service.getAllStudents();

        if (list.isEmpty()) {
            printInfo.accept("No students found.");
            return;
        }

        printTableHeader();
        // Lambda forEach (method reference to println)
        list.forEach(s -> System.out.println("  " + s));
        System.out.println("  " + TABLE_LINE);
        printSuccess.accept("Total: " + list.size() + " student(s).");
    }

    // ─── 3. Update ───────────────────────────────────────────────────────────────

    private static void updateStudent() {
        System.out.println(CYAN + "\n  -- Update Student --" + RESET);
        String id = readString("  Enter Student ID to update: ");

        // Optional.ifPresentOrElse equivalent for Java 8
        service.findById(id.trim()).map(existing -> {
            System.out.println(GREEN + "  Found: " + existing + RESET);
            printInfo.accept("(Press ENTER to keep existing value)");

            String name  = readOptional("  New Name  [" + existing.getName()  + "]: ", existing.getName());
            String email = readOptional("  New Email [" + existing.getEmail() + "]: ", existing.getEmail());
            String phone = readOptional("  New Phone [" + existing.getPhone() + "]: ", existing.getPhone());
            String gpaStr = readOptional("  New GPA   [" + existing.getGpa()  + "]: ",
                                         String.valueOf(existing.getGpa()));

            try {
                double gpa = Double.parseDouble(gpaStr);
                Student updated = new Student(id.trim(), name, email, phone, gpa);
                handleResult(service.updateStudent(updated));
            } catch (NumberFormatException e) {
                printError.accept("Invalid GPA input: " + gpaStr);
            }
            return existing;
        }).orElseGet(() -> {
            printError.accept("Student with ID '" + id + "' not found.");
            return null;
        });
    }

    // ─── 4. Delete ───────────────────────────────────────────────────────────────

    private static void deleteStudent() {
        System.out.println(CYAN + "\n  -- Delete Student --" + RESET);
        String id = readString("  Enter Student ID to delete: ");

        service.findById(id.trim()).map(existing -> {
            printInfo.accept("Found: " + existing);
            System.out.print(YELLOW + "  Confirm delete? (y/n): " + RESET);
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("y")) {
                handleResult(service.deleteStudent(id.trim()));
            } else {
                printInfo.accept("Deletion cancelled.");
            }
            return existing;
        }).orElseGet(() -> {
            printError.accept("Student with ID '" + id + "' not found.");
            return null;
        });
    }

    // ─── 5. Search ───────────────────────────────────────────────────────────────

    private static void searchStudent() {
        System.out.println(CYAN + "\n  -- Search Student --" + RESET);
        System.out.println("  1. Search by ID");
        System.out.println("  2. Search by Name");

        // Map of search options using lambdas
        Map<Integer, Runnable> searchActions = new LinkedHashMap<>();
        searchActions.put(1, Main::searchById);
        searchActions.put(2, Main::searchByName);

        int opt = readInt("  Choose search type: ");
        Optional.ofNullable(searchActions.get(opt))
                .orElse(() -> printError.accept("Invalid option."))
                .run();
    }

    private static void searchById() {
        String id = readString("  Enter Student ID: ");
        service.findById(id.trim())
               .map(found -> {
                   printTableHeader();
                   System.out.println("  " + found);
                   System.out.println("  " + TABLE_LINE);
                   return found;
               })
               .orElseGet(() -> {
                   printError.accept("No student found with ID '" + id + "'.");
                   return null;
               });
    }

    private static void searchByName() {
        String name = readString("  Enter name (partial ok): ");
        List<Student> results = service.findByName(name.trim());

        if (results.isEmpty()) {
            printError.accept("No students found with name containing '" + name + "'.");
        } else {
            printTableHeader();
            results.forEach(s -> System.out.println("  " + s));
            System.out.println("  " + TABLE_LINE);
            printSuccess.accept("Found: " + results.size() + " result(s).");
        }
    }

    // ─── UI Helpers ──────────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println(CYAN);
        System.out.println("  +==================================================+");
        System.out.println("  |     STUDENT MANAGEMENT SYSTEM  (PostgreSQL)      |");
        System.out.println("  |         Java CRUD + JDBC + Stored Procs          |");
        System.out.println("  +==================================================+");
        System.out.println(RESET);
    }

    private static void printMenu() {
        System.out.println(BLUE + "\n  +-----------------------------+" + RESET);
        System.out.println(BLUE + "  |         MAIN MENU           |" + RESET);
        System.out.println(BLUE + "  +--------------------- -------+" + RESET);
        System.out.println(BLUE + "  |  1. Add new student         |" + RESET);
        System.out.println(BLUE + "  |  2. Display all students    |" + RESET);
        System.out.println(BLUE + "  |  3. Update a student        |" + RESET);
        System.out.println(BLUE + "  |  4. Delete a student        |" + RESET);
        System.out.println(BLUE + "  |  5. Search student          |" + RESET);
        System.out.println(BLUE + "  |  6. Exit                    |" + RESET);
        System.out.println(BLUE + "  +-----------------------------+" + RESET);
    }

    private static void printTableHeader() {
        System.out.println("  " + TABLE_LINE);
        System.out.println("  " + TABLE_HEADER);
        System.out.println("  " + TABLE_LINE);
    }

    /** Dispatches success/error print using Consumer lambdas. */
    private static void handleResult(String message) {
        Consumer<String> printer = (message.startsWith("Error") || message.startsWith("Validation"))
                ? printError
                : printSuccess;
        printer.accept(message);
    }

    /** Reads a non-blank string; re-prompts if blank. */
    private static String readString(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                printError.accept("Input cannot be blank.");
            }
        } while (input.trim().isEmpty());
        return input;
    }

    /** Returns defaultValue if user presses ENTER; otherwise returns input. */
    private static String readOptional(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    /** Reads an integer with re-prompt on invalid input. */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                printError.accept("Please enter a valid integer.");
            }
        }
    }

    /** Reads a double with re-prompt on invalid input. */
    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                printError.accept("Please enter a valid number.");
            }
        }
    }
}
