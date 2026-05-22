package main;

import dao.CatDAO;
import dao.CatDAOImpl;
import dao.OwnerDAO;
import dao.OwnerDAOImpl;
import entities.Cat;
import entities.Owner;
import utils.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final CatDAO catDAO = new CatDAOImpl();
    private static final OwnerDAO ownerDAO = new OwnerDAOImpl();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        //
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        while (!exit) {
            System.out.println("\n=========================================");
            System.out.println("            CAT MANAGEMENT               ");
            System.out.println("=========================================");
            System.out.println("1. Add new cat");
            System.out.println("2. Display all cats");
            System.out.println("3. Update a cat");
            System.out.println("4. Delete a cat");
            System.out.println("5. Search cat by ID");
            System.out.println("6. Exit");
            System.out.println("7. Design Database Table");
            System.out.println("=========================================");
            System.out.print("Please enter your choice (1-7): ");

            String choiceStr = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1 -> addNewRecord(scanner);
                case 2 -> displayAllRecords();
                case 3 -> updateRecord(scanner);
                case 4 -> deleteRecord(scanner);
                case 5 -> searchRecordById(scanner);
                case 6 -> {
                    exit = true;
                    System.out.println("Exiting... Goodbye!");
                }
                case 7 -> System.out.println("Design Database");
                default -> System.out.println("Invalid choice! Please choose a number between 1 and 7.");
            }
        }
        scanner.close();
    }

    private static void addNewRecord(Scanner scanner) {
        String ownerId, ownerName, ownerEmail, ownerPhone;
        Owner owner = null;
        while (true) {
            System.out.print("Enter Owner ID: ");
            ownerId = scanner.nextLine().trim();
            if (ownerId.isEmpty()) {
                System.out.println("Owner ID cannot be empty.");
                continue;
            }
            Owner foundOwner = ownerDAO.findById(ownerId);
            if (foundOwner == null) {
                System.out.println("Owner not found in database. Please register the new Owner details.");
                while (true) {
                    System.out.print("Enter Owner Name: ");
                    ownerName = scanner.nextLine().trim();
                    if (!ownerName.isEmpty()) {
                        break;
                    }
                    System.out.println("Owner Name cannot be empty.");
                }
                while (true) {
                    System.out.print("Enter Owner Email: ");
                    ownerEmail = scanner.nextLine().trim();
                    if (Validator.isValidEmail(ownerEmail)) {
                        break;
                    }
                    System.out.println("Invalid Email format.");
                }
                while (true) {
                    System.out.print("Enter Owner Phone: ");
                    ownerPhone = scanner.nextLine().trim();
                    if (Validator.isValidPhone(ownerPhone)) {
                        break;
                    }
                    System.out.println("Invalid Phone number (must be 9-10 digits).");
                }
                owner = new Owner(ownerId, ownerName, ownerEmail, ownerPhone);
                if (ownerDAO.add(owner)) {
                    System.out.println("Owner registered successfully!");
                    break;
                } else {
                    System.out.println("Failed to register Owner. Please try again");
                }
            } else {
                owner = foundOwner;
                System.out.println("Owner found: " + owner.getName() + " (" + owner.getEmail() + ")");
                break;
            }
        }
        String catId, catName, birthDateStr;
        while (true) {
            System.out.print("Enter Cat ID: ");
            catId = scanner.nextLine().trim();
            if (catId.isEmpty()) {
                System.out.println("Cat ID cannot be empty.");
            } else if (catDAO.findById(catId) != null) {
                System.out.println("Cat ID already exists.");
            } else {
                break;
            }
        }
        while (true) {
            System.out.print("Enter Cat Name: ");
            catName = scanner.nextLine().trim();
            if (!catName.isEmpty()) {
                break;
            }
            System.out.println("Cat Name cannot be empty.");
        }
        while (true) {
            System.out.print("Enter Cat Birth Date (dd/MM/yyyy): ");
            birthDateStr = scanner.nextLine().trim();
            if (Validator.isValidBirthDate(birthDateStr)) {
                break;
            }
            System.out.println("Invalid birth date format or date is in the future.");
        }
        try {
            Date birthDay = dateFormat.parse(birthDateStr);
            Cat cat = new Cat(catId, catName, birthDay, owner);
            if (catDAO.add(cat)) {
                System.out.println("Cat record added successfully!");
            } else {
                System.out.println("Failed to add Cat record.");
            }
        } catch (Exception e) {
            System.out.println("Error processing record: " + e.getMessage());
        }
    }

    private static void displayAllRecords() {
        List<Cat> cats = catDAO.getAllCat();
        if (cats.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.printf("%-10s | %-15s | %-12s | %-10s | %-15s | %-20s | %-12s\n",
                "Cat ID", "Cat Name", "Birth Date", "Owner ID", "Owner Name", "Owner Email", "Owner Phone");
        System.out.println("-".repeat(110));

        for (Cat cat : cats) {
            Owner o = cat.getOwner();
            String ownerId = o != null ? o.getId() : "N/A";
            String ownerName = o != null ? o.getName() : "N/A";
            String ownerEmail = o != null ? o.getEmail() : "N/A";
            String ownerPhone = o != null ? o.getPhone() : "N/A";
            String birthDateStr = cat.getBirthDay() != null ? dateFormat.format(cat.getBirthDay()) : "N/A";

            System.out.printf("%-10s | %-15s | %-12s | %-10s | %-15s | %-20s | %-12s\n",
                    cat.getId(), cat.getName(), birthDateStr, ownerId, ownerName, ownerEmail, ownerPhone);
        }
    }

    private static void updateRecord(Scanner scanner) {
        Cat cat = null;
        while (true) {
            System.out.print("Enter Cat ID to update: ");
            String catId = scanner.nextLine().trim();
            if (catId.isEmpty()) {
                System.out.println("Cat ID cannot be empty.");
                continue;
            }
            cat = catDAO.findById(catId);
            if (cat == null) {
                System.out.println("Cat not found.");
                return;
            }
            System.out.println("Found Cat: " + cat.getName() + " (Birth: " + dateFormat.format(cat.getBirthDay()) + ")");
            break;
        }
        while (true) {
            System.out.print("Enter new Cat Name (leave empty to keep '" + cat.getName() + "'): ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) {
                cat.setName(newName);
            }
            break;
        }

        while (true) {
            System.out.print("Enter new Birth Date (dd/MM/yyyy) (leave empty to keep '" + dateFormat.format(cat.getBirthDay()) + "'): ");
            String newBirthDateStr = scanner.nextLine().trim();
            if (newBirthDateStr.isEmpty()) {
                break;
            }
            if (Validator.isValidBirthDate(newBirthDateStr)) {
                try {
                    cat.setBirthDay(dateFormat.parse(newBirthDateStr));
                    break;
                } catch (Exception e) {
                    System.out.println("Error parsing date: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid birth date format or date is in the future.");
            }
        }

        String currentOwnerId = cat.getOwner() != null ? cat.getOwner().getId() : "None";
        while (true) {
            System.out.print("Enter new Owner ID (leave empty to keep '" + currentOwnerId + "'): ");
            String newOwnerId = scanner.nextLine().trim();
            if (newOwnerId.isEmpty()) {
                break;
            }
            Owner owner = ownerDAO.findById(newOwnerId);
            if (owner == null) {
                System.out.println("Owner not found in database. Please register the new Owner details.");
                String ownerName, ownerEmail, ownerPhone;
                while (true) {
                    System.out.print("Enter Owner Name: ");
                    ownerName = scanner.nextLine().trim();
                    if (!ownerName.isEmpty()) {
                        break;
                    }
                    System.out.println("Owner Name cannot be empty.");
                }
                while (true) {
                    System.out.print("Enter Owner Email: ");
                    ownerEmail = scanner.nextLine().trim();
                    if (Validator.isValidEmail(ownerEmail)) {
                        break;
                    }
                    System.out.println("Invalid Email format.");
                }
                while (true) {
                    System.out.print("Enter Owner Phone: ");
                    ownerPhone = scanner.nextLine().trim();
                    if (Validator.isValidPhone(ownerPhone)) {
                        break;
                    }
                    System.out.println("Invalid Phone number (must be 9-10 digits).");
                }
                owner = new Owner(newOwnerId, ownerName, ownerEmail, ownerPhone);
                if (ownerDAO.add(owner)) {
                    System.out.println("Owner registered successfully!");
                    cat.setOwner(owner);
                    break;
                } else {
                    System.out.println("Failed to register Owner. Please try again");
                }
            } else {
                System.out.println("Owner found: " + owner.getName() + " (" + owner.getEmail() + ")");
                cat.setOwner(owner);
                break;
            }
        }

        if (catDAO.update(cat)) {
            System.out.println("Cat record updated successfully!");
        } else {
            System.out.println("Failed to update Cat record.");
        }
    }

    private static void deleteRecord(Scanner scanner) {
        System.out.println("\n--- Delete a Cat Record ---");
        System.out.print("Enter Cat ID to delete: ");
        String catId = scanner.nextLine().trim();

        Cat cat = catDAO.findById(catId);
        if (cat == null) {
            System.out.println("Cat not found.");
            return;
        }

        System.out.print("Are you sure you want to delete Cat '" + cat.getName() + "'? (Y/N): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("Y")) {
            if (catDAO.delete(cat.getId())) {
                System.out.println("Cat record deleted successfully!");
            } else {
                System.out.println("Failed to delete Cat record.");
            }
        } else {
            System.out.println("Deletion canceled.");
        }
    }

    private static void searchRecordById(Scanner scanner) {
        System.out.println("\n--- Search Cat Record by ID ---");
        System.out.print("Enter Cat ID to search: ");
        String catId = scanner.nextLine().trim();

        Cat cat = catDAO.findById(catId);
        if (cat == null) {
            System.out.println("Cat record not found.");
            return;
        }
        System.out.println("\n--- Cat Found ---");
        System.out.println("Cat ID:      " + cat.getId());
        System.out.println("Cat Name:    " + cat.getName());
        System.out.println("Birth Date: " + dateFormat.format(cat.getBirthDay()));
        Owner o = cat.getOwner();
        if (o != null) {
            System.out.println("Owner ID:    " + o.getId());
            System.out.println("Owner Name: " + o.getName());
            System.out.println("Owner Email: " + o.getEmail());
            System.out.println("Owner Phone: " + o.getPhone());
        } else {
            System.out.println("Owner Info: None");
        }
    }
}