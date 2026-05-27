package view;

import controller.TeacherController;
import model.Teacher;
import util.InputUtils;

import java.util.List;

public class TeacherView {

    private final TeacherController controller;

    public TeacherView() {
        this.controller = new TeacherController();
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            printMenu();
            String choice = InputUtils.getRawLine("");
            switch (choice) {
                case "1" -> handleAdd();
                case "2" -> handleDisplayAll();
                case "3" -> handleUpdate();
                case "4" -> handleDelete();
                case "5" -> handleSearchById();
                case "6" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("[!] Invalid option. Please enter 1-6.");
            }
        }
    }

    // Menu
    private void printBanner() {
        System.out.println("========================================");
        System.out.println("   TEACHER MANAGEMENT SYSTEM (JDBC)    ");
        System.out.println("========================================");
    }

    private void printMenu() {
        System.out.println("\n----- MENU -----");
        System.out.println("1. Add Teacher");
        System.out.println("2. Display All Teachers");
        System.out.println("3. Update Teacher");
        System.out.println("4. Delete Teacher");
        System.out.println("5. Search by ID");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }


    // 1. Add
    private void handleAdd() {
        System.out.println("\n-- Add New Teacher --");

        String id     = InputUtils.getString("Enter ID (max 10 chars): ");
        String name   = InputUtils.getString("Enter Name: ");
        String email  = InputUtils.getEmail("Enter Email: ");
        String phone  = InputUtils.getPhone("Enter Phone (digits only): ");
        double salary = InputUtils.getPositiveDouble("Enter Salary (> 0): ");

        String result = controller.addTeacher(id, name, email, phone, salary);
        printResult(result);
    }


    // 2. Display All
    private void handleDisplayAll() {
        System.out.println("\n-- All Teachers --");
        List<Teacher> list = controller.getAllTeachers();

        if (list.isEmpty()) {
            System.out.println("No teachers found.");
            return;
        }

        System.out.printf("%-10s %-20s %-30s %-15s %10s%n",
            "ID", "Name", "Email", "Phone", "Salary");
        System.out.println("-".repeat(90));
        for (Teacher t : list) {
            System.out.printf("%-10s %-20s %-30s %-15s %10.2f%n",
                t.getId(), t.getName(), t.getEmail(),
                t.getPhone(), t.getSalary());
        }
    }


    // 3. Update  (press ENTER to keep any existing field)
    private void handleUpdate() {
        System.out.println("\n-- Update Teacher --");

        String id = InputUtils.getString("Enter ID of teacher to update: ");

        Teacher existing = controller.findTeacherById(id);
        if (existing == null) {
            System.out.println("[!] Teacher with ID '" + id + "' not found.");
            return;
        }

        System.out.println("Current record: " + existing);
        System.out.println("(Press ENTER to keep the current value for any field)");

        String name   = InputUtils.getUpdateString("Name",   existing.getName());
        String email  = InputUtils.getUpdateEmail("Email",  existing.getEmail());
        String phone  = InputUtils.getUpdatePhone("Phone",  existing.getPhone());
        double salary = InputUtils.getUpdateDouble("Salary", existing.getSalary());

        String result = controller.updateTeacher(id, name, email, phone, salary);
        printResult(result);
    }


    // 4. Delete
    private void handleDelete() {
        System.out.println("\n-- Delete Teacher --");

        String id = InputUtils.getString("Enter ID of teacher to delete: ");

        Teacher existing = controller.findTeacherById(id);
        if (existing == null) {
            System.out.println("[!] Teacher with ID '" + id + "' not found.");
            return;
        }

        String confirm = InputUtils.getRawLine(
            "Are you sure you want to delete '" + existing.getName() + "'? (y/n): "
        );
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        String result = controller.deleteTeacher(id);
        printResult(result);
    }


    // 5. Search by ID
    private void handleSearchById() {
        System.out.println("\n-- Search Teacher by ID --");

        String id = InputUtils.getString("Enter ID: ");
        Teacher teacher = controller.findTeacherById(id);

        if (teacher == null) {
            System.out.println("[!] No teacher found with ID '" + id + "'.");
        } else {
            System.out.println("Found: " + teacher);
        }
    }

    private void printResult(String message) {
        if (message.startsWith("SUCCESS")) {
            System.out.println("[✓] " + message.substring("SUCCESS: ".length()));
        } else {
            System.out.println("[!] " + message.substring("ERROR: ".length()));
        }
    }
}
