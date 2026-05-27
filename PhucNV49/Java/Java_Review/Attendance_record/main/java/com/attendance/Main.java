package com.attendance;

import com.attendance.model.AttendanceRecord;
import com.attendance.model.Employee;
import com.attendance.repository.DatabaseConnection;
import com.attendance.service.AttendanceService;
import com.attendance.service.EmployeeService;
import com.attendance.validation.InputValidator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final AttendanceService attendanceService = new AttendanceService(
            new com.attendance.repository.AttendanceRepositoryImpl()
    );
    private static final EmployeeService employeeService = new EmployeeService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Attendance Record System ===");

        Map<String, Runnable> actions = Map.of(
                "1", Main::listEmployees,
                "2", Main::addEmployee,
                "3", Main::checkIn,
                "4", Main::checkOut,
                "5", Main::viewByDate,
                "6", Main::viewByEmployee
        );

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) break;
            Runnable action = actions.get(choice);
            if (action != null) action.run();
            else System.out.println("Invalid choice. Enter 0-6.");
        }

        DatabaseConnection.close();
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\n1. List employees");
        System.out.println("2. Add employee");
        System.out.println("3. Check-in");
        System.out.println("4. Check-out");
        System.out.println("5. View attendance by date");
        System.out.println("6. View attendance by employee");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private static String readEmployeeId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String val = scanner.nextLine().trim();
            if (InputValidator.isValidEmployeeId(val)) return val;
            System.out.println("  Invalid employee ID. Use format EMP001.");
        }
    }

    private static String readName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String val = scanner.nextLine().trim();
            if (InputValidator.isValidName(val)) return val;
            System.out.println("  Invalid name. Only letters and common name characters allowed.");
        }
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String val = scanner.nextLine().trim();
            if (InputValidator.isValidText(val)) return val;
            System.out.println("  Invalid text. Length 2-100 and special characters limited.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Must be a number.");
            }
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return LocalDate.now();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("  Invalid format. Use yyyy-mm-dd.");
            }
        }
    }

    private static void listEmployees() {
        List<Employee> list = employeeService.getAllEmployees();
        if (list.isEmpty()) { System.out.println("No employees found."); return; }
        list.forEach(System.out::println);
    }

    private static void addEmployee() {
        String id   = readEmployeeId("ID: ");
        if (employeeService.getEmployee(id) != null) {
            System.out.println("Employee ID already exists: " + id);
            return;
        }
        String name = readName("Name: ");
        String dept = readText("Department: ");
        String pos  = readText("Position: ");
        employeeService.addEmployee(id, name, dept, pos);
    }

    private static void checkIn() {
        String id   = readEmployeeId("Employee ID: ");
        String name = readName("Employee Name: ");
        attendanceService.checkIn(id, name);
    }

    private static void checkOut() {
        int id = readInt("Record ID: ");
        attendanceService.checkOut(id);
    }

    private static void viewByDate() {
        LocalDate date = readDate("Date (yyyy-mm-dd) [Enter = today]: ");
        List<AttendanceRecord> list = attendanceService.getByDate(date);
        if (list.isEmpty()) { System.out.println("No records for " + date); return; }
        list.forEach(System.out::println);
    }

    private static void viewByEmployee() {
        String id = readEmployeeId("Employee ID: ");
        List<AttendanceRecord> list = attendanceService.getByEmployee(id);
        if (list.isEmpty()) { System.out.println("No records for " + id); return; }
        list.forEach(System.out::println);
    }
}
