package main;

import entities.Certificate;
import entities.User;
import services.CertificateService;
import services.UserService;
import utils.Validator;
import static utils.Constants.*;

import java.util.List;
import java.util.Scanner;

public class Test {

    private static final CertificateService certificateService = new CertificateService();
    private static final UserService userService = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\n" + ANSI_BLUE + "========================================" + ANSI_RESET);
            System.out.println("   " + ANSI_BLUE_BOLD + "CERTIFICATE MANAGEMENT SYSTEM" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "========================================" + ANSI_RESET);
            System.out.println("  " + ANSI_BLUE_BOLD + "[1]" + ANSI_RESET + " Add new Certificate");
            System.out.println("  " + ANSI_BLUE_BOLD + "[2]" + ANSI_RESET + " Display all Certificates");
            System.out.println("  " + ANSI_BLUE_BOLD + "[3]" + ANSI_RESET + " Update a Certificate");
            System.out.println("  " + ANSI_BLUE_BOLD + "[4]" + ANSI_RESET + " Delete a Certificate");
            System.out.println("  " + ANSI_BLUE_BOLD + "[5]" + ANSI_RESET + " Search Certificate by ID");
            System.out.println("  " + ANSI_BLUE_BOLD + "[6]" + ANSI_RESET + " Exit");
            System.out.println(ANSI_BLUE + "========================================" + ANSI_RESET);
            System.out.print(ANSI_BLUE_BOLD + "Please enter your choice: " + ANSI_RESET);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addCertificate();
                case "2" -> displayAllCertificates();
                case "3" -> updateCertificate();
                case "4" -> deleteCertificate();
                case "5" -> findCertificateById();
                case "6" -> {
                    keepRunning = false;
                    System.out.println(ANSI_GREEN_BOLD + "Program ended. Goodbye!" + ANSI_RESET);
                }
                default -> System.out.println(ANSI_RED_BOLD + "[ERROR] Invalid choice. Please try again." + ANSI_RESET);
            }
        }
        scanner.close();
    }

    private static void addCertificate() {
        System.out.println(ANSI_PURPLE_BOLD + "\n--- Add New Certificate ---" + ANSI_RESET);

        String id;
        while (true) {
            System.out.print(ANSI_BLUE + "Certificate ID (max 10 chars): " + ANSI_RESET);
            id = scanner.nextLine().trim();
            if (!Validator.isValidId(id)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] ID is invalid (empty or too long). Please enter again." + ANSI_RESET);
                continue;
            }
            if (certificateService.findById(id) != null) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Certificate ID already exists! Please enter again." + ANSI_RESET);
                continue;
            }
            break;
        }

        String name;
        while (true) {
            System.out.print(ANSI_BLUE + "Certificate Name: " + ANSI_RESET);
            name = scanner.nextLine().trim();
            if (!Validator.isNotEmpty(name)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Certificate Name cannot be empty. Please enter again." + ANSI_RESET);
                continue;
            }
            break;
        }

        String number;
        while (true) {
            System.out.print(ANSI_BLUE + "Certificate Number: " + ANSI_RESET);
            number = scanner.nextLine().trim();
            if (!Validator.isNotEmpty(number)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Certificate Number cannot be empty. Please enter again." + ANSI_RESET);
                continue;
            }
            if (!Validator.isValidCertificateNumber(number)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Certificate Number must contain digits only. Please enter again." + ANSI_RESET);
                continue;
            }
            break;
        }

        String issueDate;
        while (true) {
            System.out.print(ANSI_BLUE + "Issue Date (dd/MM/yyyy): " + ANSI_RESET);
            issueDate = scanner.nextLine().trim();
            if (!Validator.isValidDate(issueDate)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Invalid date format. Please enter again (dd/MM/yyyy)." + ANSI_RESET);
                continue;
            }
            issueDate = Validator.normalizeDate(issueDate);
            break;
        }

        String expiryDate;
        while (true) {
            System.out.print(ANSI_BLUE + "Expiry Date (dd/MM/yyyy): " + ANSI_RESET);
            expiryDate = scanner.nextLine().trim();
            if (!Validator.isValidDate(expiryDate)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Invalid date format. Please enter again (dd/MM/yyyy)." + ANSI_RESET);
                continue;
            }
            expiryDate = Validator.normalizeDate(expiryDate);
            
            try {
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.time.LocalDate issue = java.time.LocalDate.parse(issueDate, formatter);
                java.time.LocalDate expiry = java.time.LocalDate.parse(expiryDate, formatter);
                if (!expiry.isAfter(issue)) {
                    System.out.println(ANSI_RED_BOLD + "[ERROR] Expiry Date must be after Issue Date. Please enter again." + ANSI_RESET);
                    continue;
                }
            } catch (Exception e) {
            }
            break;
        }

        double score;
        while (true) {
            System.out.print(ANSI_BLUE + "Score/GPA (0.0 - 4.0): " + ANSI_RESET);
            try {
                score = Double.parseDouble(scanner.nextLine().trim());
                if (!Validator.isValidScore(score)) {
                    System.out.println(ANSI_RED_BOLD + "[ERROR] Score must be between 0.0 and 4.0. Please enter again." + ANSI_RESET);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Score must be a valid number. Please enter again." + ANSI_RESET);
            }
        }

        String userId;
        User user = null;
        while (true) {
            System.out.print(ANSI_BLUE + "User ID (owner, must exist): " + ANSI_RESET);
            userId = scanner.nextLine().trim();
            if (!Validator.isValidId(userId)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] User ID is invalid (empty or too long). Please enter again." + ANSI_RESET);
                continue;
            }
            user = userService.findById(userId);
            if (user == null) {
                System.out.println(ANSI_BLUE_BOLD + "[INFO] User not found. Creating new user..." + ANSI_RESET);
                user = createUserInteractive(userId);
                if (user == null) {
                    System.out.println(ANSI_RED_BOLD + "[ERROR] Failed to create user. Please enter a valid User ID or try creating again." + ANSI_RESET);
                    continue;
                }
            }
            break;
        }

        Certificate cert = new Certificate(id, name, number, issueDate, expiryDate, score, userId);
        if (certificateService.addCertificate(cert)) {
            System.out.println(ANSI_GREEN_BOLD + "[SUCCESS] Certificate added successfully!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED_BOLD + "[ERROR] Failed to add certificate." + ANSI_RESET);
        }
    }

    private static void displayAllCertificates() {
        System.out.println(ANSI_PURPLE_BOLD + "\n--- All Certificates ---" + ANSI_RESET);
        List<Certificate> list = certificateService.getAllCertificates();
        if (list.isEmpty()) {
            System.out.println(ANSI_BLUE_BOLD + "No certificates found." + ANSI_RESET);
            return;
        }
        String reset = ANSI_RESET;
        String border = ANSI_BLUE + "|" + reset;
        String borderLine = ANSI_BLUE + new String(new char[105]).replace('\0', '-') + reset;

        System.out.println(borderLine);
        System.out.println(border + " " + ANSI_BLUE_BOLD + String.format("%-6s", "ID") + reset +
                           " " + border + " " + ANSI_BLUE_BOLD + String.format("%-25s", "Name") + reset +
                           " " + border + " " + ANSI_BLUE_BOLD + String.format("%-18s", "Number") + reset +
                           " " + border + " " + ANSI_BLUE_BOLD + String.format("%-12s", "Issue Date") + reset +
                           " " + border + " " + ANSI_BLUE_BOLD + String.format("%-12s", "Expiry Date") + reset +
                           " " + border + " " + ANSI_BLUE_BOLD + String.format("%-5s", "Score") + reset +
                           " " + border + " " + ANSI_BLUE_BOLD + String.format("%-6s", "UserID") + reset +
                           " " + border);
        System.out.println(borderLine);
        for (Certificate c : list) {
            System.out.println(c);
        }
        System.out.println(borderLine);
        System.out.println(ANSI_BLUE_BOLD + "Total: " + list.size() + " record(s)." + ANSI_RESET);
    }

    private static void updateCertificate() {
        System.out.println(ANSI_PURPLE_BOLD + "\n--- Update Certificate ---" + ANSI_RESET);
        System.out.print(ANSI_BLUE + "Enter Certificate ID to update: " + ANSI_RESET);
        String id = scanner.nextLine().trim();

        Certificate existing = certificateService.findById(id);
        if (existing == null) {
            System.out.println(ANSI_RED_BOLD + "[ERROR] Certificate not found with ID: " + id + ANSI_RESET);
            return;
        }

        System.out.println(ANSI_BLUE + "Current: " + ANSI_RESET + existing);
        System.out.println(ANSI_GRAY + "(Press Enter to keep current value)" + ANSI_RESET);

        System.out.print(ANSI_BLUE + "New Certificate Name [" + ANSI_PURPLE + existing.getCertificateName() + ANSI_BLUE + "]: " + ANSI_RESET);
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = existing.getCertificateName();
        }

        String number;
        while (true) {
            System.out.print(ANSI_BLUE + "New Certificate Number [" + ANSI_PURPLE + existing.getCertificateNumber() + ANSI_BLUE + "]: " + ANSI_RESET);
            number = scanner.nextLine().trim();
            if (number.isEmpty()) {
                number = existing.getCertificateNumber();
                break;
            }
            if (!Validator.isValidCertificateNumber(number)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Certificate Number must contain digits only. Please enter again." + ANSI_RESET);
                continue;
            }
            break;
        }

        String issueDate;
        while (true) {
            System.out.print(ANSI_BLUE + "New Issue Date [" + ANSI_PURPLE + Validator.normalizeDate(existing.getIssueDate()) + ANSI_BLUE + "]: " + ANSI_RESET);
            issueDate = scanner.nextLine().trim();
            if (issueDate.isEmpty()) {
                issueDate = Validator.normalizeDate(existing.getIssueDate());
                break;
            }
            if (!Validator.isValidDate(issueDate)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Invalid date format. Please enter again (dd/MM/yyyy)." + ANSI_RESET);
                continue;
            }
            issueDate = Validator.normalizeDate(issueDate);
            break;
        }

        String expiryDate;
        while (true) {
            System.out.print(ANSI_BLUE + "New Expiry Date [" + ANSI_PURPLE + Validator.normalizeDate(existing.getExpiryDate()) + ANSI_BLUE + "]: " + ANSI_RESET);
            expiryDate = scanner.nextLine().trim();
            if (expiryDate.isEmpty()) {
                expiryDate = Validator.normalizeDate(existing.getExpiryDate());
            } else {
                if (!Validator.isValidDate(expiryDate)) {
                    System.out.println(ANSI_RED_BOLD + "[ERROR] Invalid date format. Please enter again (dd/MM/yyyy)." + ANSI_RESET);
                    continue;
                }
                expiryDate = Validator.normalizeDate(expiryDate);
            }
            
            try {
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.time.LocalDate issue = java.time.LocalDate.parse(issueDate, formatter);
                java.time.LocalDate expiry = java.time.LocalDate.parse(expiryDate, formatter);
                if (!expiry.isAfter(issue)) {
                    System.out.println(ANSI_RED_BOLD + "[ERROR] Expiry Date must be after Issue Date. Please enter again." + ANSI_RESET);
                    continue;
                }
            } catch (Exception e) {
            }
            break;
        }

        double score;
        while (true) {
            System.out.print(ANSI_BLUE + "New Score [" + ANSI_PURPLE + existing.getScore() + ANSI_BLUE + "]: " + ANSI_RESET);
            String scoreStr = scanner.nextLine().trim();
            if (scoreStr.isEmpty()) {
                score = existing.getScore();
                break;
            }
            try {
                double newScore = Double.parseDouble(scoreStr);
                if (!Validator.isValidScore(newScore)) {
                    System.out.println(ANSI_RED_BOLD + "[ERROR] Score must be between 0.0 and 4.0. Please enter again." + ANSI_RESET);
                    continue;
                }
                score = newScore;
                break;
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Score must be a valid number. Please enter again." + ANSI_RESET);
            }
        }

        String userId;
        User user = null;
        while (true) {
            System.out.print(ANSI_BLUE + "New User ID [" + ANSI_PURPLE + existing.getUserId() + ANSI_BLUE + "]: " + ANSI_RESET);
            userId = scanner.nextLine().trim();
            if (userId.isEmpty()) {
                userId = existing.getUserId();
                break;
            }
            if (!Validator.isValidId(userId)) {
                System.out.println(ANSI_RED_BOLD + "[ERROR] User ID is invalid (empty or too long). Please enter again." + ANSI_RESET);
                continue;
            }
            user = userService.findById(userId);
            if (user == null) {
                System.out.println(ANSI_BLUE_BOLD + "[INFO] User not found. Creating new user..." + ANSI_RESET);
                user = createUserInteractive(userId);
                if (user == null) {
                    System.out.println(ANSI_RED_BOLD + "[ERROR] Failed to create user. Please enter a valid User ID or try creating again." + ANSI_RESET);
                    continue;
                }
            }
            break;
        }

        Certificate updated = new Certificate(id, name, number, issueDate, expiryDate, score, userId);
        if (certificateService.updateCertificate(updated)) {
            System.out.println(ANSI_GREEN_BOLD + "[SUCCESS] Certificate updated successfully!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED_BOLD + "[ERROR] Failed to update certificate." + ANSI_RESET);
        }
    }

    private static void deleteCertificate() {
        System.out.println(ANSI_PURPLE_BOLD + "\n--- Delete Certificate ---" + ANSI_RESET);
        System.out.print(ANSI_BLUE + "Enter Certificate ID to delete: " + ANSI_RESET);
        String id = scanner.nextLine().trim();

        Certificate existing = certificateService.findById(id);
        if (existing == null) {
            System.out.println(ANSI_RED_BOLD + "[ERROR] Certificate not found with ID: " + id + ANSI_RESET);
            return;
        }

        System.out.println(ANSI_BLUE + "Found: " + ANSI_RESET + existing);
        System.out.print(ANSI_RED_BOLD + "Are you sure you want to delete? (y/n): " + ANSI_RESET);
        String confirm = scanner.nextLine().trim();

        if ("y".equalsIgnoreCase(confirm)) {
            if (certificateService.deleteCertificate(id)) {
                System.out.println(ANSI_GREEN_BOLD + "[SUCCESS] Certificate deleted successfully!" + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED_BOLD + "[ERROR] Failed to delete certificate." + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_BLUE + "Delete cancelled." + ANSI_RESET);
        }
    }

    private static void findCertificateById() {
        System.out.println(ANSI_PURPLE_BOLD + "\n--- Search Certificate by ID ---" + ANSI_RESET);
        System.out.print(ANSI_BLUE + "Enter Certificate ID: " + ANSI_RESET);
        String id = scanner.nextLine().trim();

        Certificate cert = certificateService.findById(id);
        if (cert != null) {
            String reset = ANSI_RESET;
            String border = ANSI_BLUE + "|" + reset;
            String borderLine = ANSI_BLUE + new String(new char[105]).replace('\0', '-') + reset;

            System.out.println(borderLine);
            System.out.println(border + " " + ANSI_BLUE_BOLD + String.format("%-6s", "ID") + reset +
                               " " + border + " " + ANSI_BLUE_BOLD + String.format("%-25s", "Name") + reset +
                               " " + border + " " + ANSI_BLUE_BOLD + String.format("%-18s", "Number") + reset +
                               " " + border + " " + ANSI_BLUE_BOLD + String.format("%-12s", "Issue Date") + reset +
                               " " + border + " " + ANSI_BLUE_BOLD + String.format("%-12s", "Expiry Date") + reset +
                               " " + border + " " + ANSI_BLUE_BOLD + String.format("%-5s", "Score") + reset +
                               " " + border + " " + ANSI_BLUE_BOLD + String.format("%-6s", "UserID") + reset +
                               " " + border);
            System.out.println(borderLine);
            System.out.println(cert);
            System.out.println(borderLine);
        } else {
            System.out.println(ANSI_RED_BOLD + "[ERROR] Certificate not found with ID: " + id + ANSI_RESET);
        }
    }

    private static User createUserInteractive(String userId) {
        String fullName;
        while (true) {
            System.out.print(ANSI_BLUE + "  Full Name: " + ANSI_RESET);
            fullName = scanner.nextLine().trim();
            if (!Validator.isNotEmpty(fullName)) {
                System.out.println(ANSI_RED_BOLD + "  [ERROR] Full Name cannot be empty. Please enter again." + ANSI_RESET);
                continue;
            }
            break;
        }

        String phone;
        while (true) {
            System.out.print(ANSI_BLUE + "  Phone (digits only, 9-15 chars): " + ANSI_RESET);
            phone = scanner.nextLine().trim();
            if (!Validator.isValidPhone(phone)) {
                System.out.println(ANSI_RED_BOLD + "  [ERROR] Invalid phone number. Please enter again." + ANSI_RESET);
                continue;
            }
            break;
        }

        String email;
        while (true) {
            System.out.print(ANSI_BLUE + "  Email: " + ANSI_RESET);
            email = scanner.nextLine().trim();
            if (!Validator.isValidEmail(email)) {
                System.out.println(ANSI_RED_BOLD + "  [ERROR] Invalid email format. Please enter again." + ANSI_RESET);
                continue;
            }
            break;
        }

        String password;
        while (true) {
            System.out.print(ANSI_BLUE + "  Password: " + ANSI_RESET);
            password = scanner.nextLine().trim();
            if (!Validator.isNotEmpty(password)) {
                System.out.println(ANSI_RED_BOLD + "  [ERROR] Password cannot be empty. Please enter again." + ANSI_RESET);
                continue;
            }
            break;
        }

        User user = new User(userId, fullName, phone, email, password);
        if (userService.addUser(user)) {
            System.out.println(ANSI_GREEN_BOLD + "[SUCCESS] User created successfully!" + ANSI_RESET);
            return user;
        }
        return null;
    }
}
