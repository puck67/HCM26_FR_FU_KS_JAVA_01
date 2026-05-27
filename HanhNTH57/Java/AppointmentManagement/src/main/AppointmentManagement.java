package main;

import entities.Appointment;
import entities.User;
import services.AppointmentService;
import services.UserService;
import utils.ConsoleUtils;
import utils.DBContext;
import utils.ValidationUtils;

import java.util.Scanner;

public class AppointmentManagement {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final AppointmentService appointmentService = new AppointmentService();

    public static void main(String[] args) {
        try {
            DBContext.initializeDatabase();
            while (true) {
                ConsoleUtils.printHeader("Appointment Management System");
                ConsoleUtils.printMenuOption("1", "User Management");
                ConsoleUtils.printMenuOption("2", "Appointment Management");
                ConsoleUtils.printMenuOption("3", "Exit");
                System.out.print("\nChoose an option: ");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> userMenu();
                    case "2" -> appointmentMenu();
                    case "3" -> {
                        System.out.println("Exiting... Goodbye!");
                        System.exit(0);
                    }
                    default -> ConsoleUtils.printError("Invalid choice. Try again.");
                }
            }
        } catch (Exception e) {
            ConsoleUtils.printError("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void userMenu() {
        while (true) {
            ConsoleUtils.printHeader("User Management");
            ConsoleUtils.printMenuOption("1", "Add User");
            ConsoleUtils.printMenuOption("2", "Display All Users");
            ConsoleUtils.printMenuOption("3", "Update User");
            ConsoleUtils.printMenuOption("4", "Delete User");
            ConsoleUtils.printMenuOption("5", "Back to Main Menu");
            System.out.print("\nChoose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addUser();
                case "2" -> {
                    ConsoleUtils.printHeader("List of All Users");
                    userService.displayAllUser();
                }
                case "3" -> updateUser();
                case "4" -> deleteUser();
                case "5" -> { return; }
                default -> ConsoleUtils.printError("Invalid choice.");
            }
        }
    }

    private static void addUser() {
        String id = getNonEmptyInput("Enter User ID: ");
        if (userService.exists(id)) {
            ConsoleUtils.printError("User ID already exists.");
            return;
        }
        String name = getNonEmptyInput("Enter User Name: ");
        userService.addUser(new User(id, name));
        ConsoleUtils.printSuccess("User added successfully.");
    }

    private static void updateUser() {
        String uId = getNonEmptyInput("Enter User ID to update: ");
        if (!userService.exists(uId)) {
            ConsoleUtils.printError("User not found.");
            return;
        }
        String uName = getNonEmptyInput("Enter New Name: ");
        userService.updateUser(uId, uName);
        ConsoleUtils.printSuccess("User updated successfully.");
    }

    private static void deleteUser() {
        String dId = getNonEmptyInput("Enter User ID to delete: ");
        if (userService.deleteUser(dId)) {
            ConsoleUtils.printSuccess("User deleted successfully.");
        } else {
            ConsoleUtils.printError("User not found.");
        }
    }

    private static void appointmentMenu() {
        while (true) {
            ConsoleUtils.printHeader("Appointment Management");
            ConsoleUtils.printMenuOption("1", "Add Appointment");
            ConsoleUtils.printMenuOption("2", "Display All Appointments");
            ConsoleUtils.printMenuOption("3", "Update Appointment");
            ConsoleUtils.printMenuOption("4", "Delete Appointment");
            ConsoleUtils.printMenuOption("5", "Back to Main Menu");
            System.out.print("\nChoose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addAppointment();
                case "2" -> {
                    ConsoleUtils.printHeader("List of All Appointments");
                    appointmentService.displayAllAppointments();
                }
                case "3" -> updateAppointment();
                case "4" -> deleteAppointment();
                case "5" -> { return; }
                default -> ConsoleUtils.printError("Invalid choice.");
            }
        }
    }

    private static void addAppointment() {
        String id = getNonEmptyInput("Enter Appointment ID: ");
        if (appointmentService.exists(id)) {
            ConsoleUtils.printError("Appointment ID already exists.");
            return;
        }
        String p1 = getNonEmptyInput("Enter Person 1 ID: ");
        if (!userService.exists(p1)) {
            ConsoleUtils.printError("User not found.");
            return;
        }
        String p2 = getNonEmptyInput("Enter Person 2 ID: ");
        if (!userService.exists(p2)) {
            ConsoleUtils.printError("User not found.");
            return;
        }
        if (!ValidationUtils.isDifferent(p1, p2)) {
            ConsoleUtils.printError("Person 1 and Person 2 must be different individuals.");
            return;
        }

        String start;
        while (true) {
            start = getNonEmptyInput("Enter Start Time (dd/MM/yyyy HH:mm): ");
            if (ValidationUtils.isValidDate(start)) break;
            ConsoleUtils.printError("Invalid format. Please use dd/MM/yyyy HH:mm");
        }

        String end;
        while (true) {
            end = getNonEmptyInput("Enter End Time (dd/MM/yyyy HH:mm): ");
            if (ValidationUtils.isValidDate(end)) {
                if (ValidationUtils.isAfter(start, end)) break;
                else ConsoleUtils.printError("End time must be after start time.");
            } else {
                ConsoleUtils.printError("Invalid format. Please use dd/MM/yyyy HH:mm");
            }
        }

        if (appointmentService.isOverlap(p1, start, end)) {
            ConsoleUtils.printError("Person 1 (" + p1 + ") already has an appointment during this time.");
            return;
        }
        if (appointmentService.isOverlap(p2, start, end)) {
            ConsoleUtils.printError("Person 2 (" + p2 + ") already has an appointment during this time.");
            return;
        }

        String place = getNonEmptyInput("Enter Place: ");
        String reason = getNonEmptyInput("Enter Reason: ");
        appointmentService.addAppointment(new Appointment(id, p1, p2, start, end, place, reason));
        ConsoleUtils.printSuccess("Appointment added successfully.");
    }

    private static void updateAppointment() {
        String uId = getNonEmptyInput("Enter Appointment ID to update: ");
        Appointment existing = appointmentService.findAppointmentById(uId);
        if (existing == null) {
            ConsoleUtils.printError("Appointment not found.");
            return;
        }
        String up1 = getNonEmptyInput("Enter New Person 1 ID (" + existing.getPerson1() + "): ");
        if (!userService.exists(up1)) {
            ConsoleUtils.printError("User not found.");
            return;
        }
        String up2 = getNonEmptyInput("Enter New Person 2 ID (" + existing.getPerson2() + "): ");
        if (!userService.exists(up2)) {
            ConsoleUtils.printError("User not found.");
            return;
        }
        if (!ValidationUtils.isDifferent(up1, up2)) {
            ConsoleUtils.printError("Person 1 and Person 2 must be different individuals.");
            return;
        }

        String ustart;
        while (true) {
            System.out.print("Enter New Start Time (dd/MM/yyyy HH:mm) [" + existing.getStartTime() + "]: ");
            ustart = scanner.nextLine();
            if (ustart.isEmpty()) { ustart = existing.getStartTime(); break; }
            if (ValidationUtils.isValidDate(ustart)) break;
            ConsoleUtils.printError("Invalid format. Please use dd/MM/yyyy HH:mm");
        }

        String uend;
        while (true) {
            System.out.print("Enter New End Time (dd/MM/yyyy HH:mm) [" + existing.getEndTime() + "]: ");
            uend = scanner.nextLine();
            if (uend.isEmpty()) { uend = existing.getEndTime(); break; }
            if (ValidationUtils.isValidDate(uend)) {
                if (ValidationUtils.isAfter(ustart, uend)) break;
                else ConsoleUtils.printError("End time must be after start time.");
            } else {
                ConsoleUtils.printError("Invalid format. Please use dd/MM/yyyy HH:mm");
            }
        }

        if (appointmentService.isOverlap(up1, ustart, uend, uId)) {
            ConsoleUtils.printError("Person 1 (" + up1 + ") has a conflict during this time.");
            return;
        }
        if (appointmentService.isOverlap(up2, ustart, uend, uId)) {
            ConsoleUtils.printError("Person 2 (" + up2 + ") has a conflict during this time.");
            return;
        }

        String uplace = getNonEmptyInput("Enter New Place: ");
        String ureason = getNonEmptyInput("Enter New Reason: ");
        appointmentService.updateAppointment(new Appointment(uId, up1, up2, ustart, uend, uplace, ureason));
        ConsoleUtils.printSuccess("Appointment updated successfully.");
    }

    private static void deleteAppointment() {
        String dId = getNonEmptyInput("Enter Appointment ID to delete: ");
        if (appointmentService.deleteAppointment(dId)) {
            ConsoleUtils.printSuccess("Appointment deleted successfully.");
        } else {
            ConsoleUtils.printError("Appointment not found.");
        }
    }

    private static String getNonEmptyInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (ValidationUtils.isNotEmpty(input)) {
                return input;
            }
            ConsoleUtils.printError("Input cannot be empty. Please try again.");
        }
    }
}
