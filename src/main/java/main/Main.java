package main;

import dao.StoreDao;
import model.Store;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StoreDao storeDao = new StoreDao();
    private static final StoreDao.StoreValidator validator = new StoreDao.StoreValidator();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== STORE MANAGEMENT SYSTEM (H2) =====");
            System.out.println("1. Add new store");
            System.out.println("2. Display all stores");
            System.out.println("3. Update a store");
            System.out.println("4. Delete a record");
            System.out.println("5. Search record by ID");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addRecord();
                case "2" -> displayAllRecords();
                case "3" -> updateRecord();
                case "4" -> deleteRecord();
                case "5" -> searchRecordById();
                case "6" -> {
                    System.out.println("Exiting the program...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addRecord() {
        String id;
        while (true) {
            System.out.print("Enter Store ID: ");
            id = scanner.nextLine().trim();
            String error = validator.validateId(id);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            if (storeDao.findById(id) != null) {
                System.out.println("Error: Store ID already exists! Please enter a different ID.");
                continue;
            }
            break;
        }

        String name;
        while (true) {
            System.out.print("Enter Store Name: ");
            name = scanner.nextLine().trim();
            String error = validator.validateName(name);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            break;
        }

        String location;
        while (true) {
            System.out.print("Enter Location: ");
            location = scanner.nextLine().trim();
            String error = validator.validateLocation(location);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            break;
        }

        String phone;
        while (true) {
            System.out.print("Enter Phone: ");
            phone = scanner.nextLine().trim();
            String error = validator.validatePhone(phone);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            break;
        }

        double rating;
        while (true) {
            System.out.print("Enter Rating (0-5): ");
            String line = scanner.nextLine().trim();
            try {
                rating = Double.parseDouble(line);
                String error = validator.validateRating(rating);
                if (error != null) {
                    System.out.println("Error: " + error);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Rating must be a valid number!");
            }
        }

        Store store = new Store(id, name, location, phone, rating);
        if (storeDao.add(store)) {
            System.out.println("Store added successfully!");
        } else {
            System.out.println("Failed to add store.");
        }
    }

    private static void displayAllRecords() {
        List<Store> stores = storeDao.getAll();
        if (stores.isEmpty()) {
            System.out.println("No records found.");
        } else {
            System.out.println("\n--- All Stores ---");
            for (Store store : stores) {
                System.out.println(store);
            }
        }
    }

    private static void updateRecord() {
        String id;
        Store existingStore;
        while (true) {
            System.out.print("Enter Store ID to update: ");
            id = scanner.nextLine().trim();
            String error = validator.validateId(id);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            existingStore = storeDao.findById(id);
            if (existingStore == null) {
                System.out.println("Error: Store not found! Please enter a valid ID.");
                continue;
            }
            break;
        }

        String name;
        while (true) {
            System.out.print("Enter new Name (leave empty to keep current: " + existingStore.getName() + "): ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = existingStore.getName();
                break;
            }
            String error = validator.validateName(name);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            break;
        }

        String location;
        while (true) {
            System.out.print("Enter new Location (leave empty to keep current: " + existingStore.getLocation() + "): ");
            location = scanner.nextLine().trim();
            if (location.isEmpty()) {
                location = existingStore.getLocation();
                break;
            }
            String error = validator.validateLocation(location);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            break;
        }

        String phone;
        while (true) {
            System.out.print("Enter new Phone (leave empty to keep current: " + existingStore.getPhone() + "): ");
            phone = scanner.nextLine().trim();
            if (phone.isEmpty()) {
                phone = existingStore.getPhone();
                break;
            }
            String error = validator.validatePhone(phone);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            break;
        }

        double rating = existingStore.getRating();
        while (true) {
            System.out.print("Enter new Rating (leave empty to keep current: " + existingStore.getRating() + "): ");
            String ratingStr = scanner.nextLine().trim();
            if (ratingStr.isEmpty()) {
                break;
            }
            try {
                rating = Double.parseDouble(ratingStr);
                String error = validator.validateRating(rating);
                if (error != null) {
                    System.out.println("Error: " + error);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Rating must be a valid number!");
            }
        }


        Store updatedStore = new Store(id, name, location, phone, rating);
        if (storeDao.update(updatedStore)) {
            System.out.println("Store updated successfully!");
        } else {
            System.out.println("Failed to update store.");
        }
    }

    private static void deleteRecord() {
        String id;
        while (true) {
            System.out.print("Enter Store ID to delete: ");
            id = scanner.nextLine().trim();
            String error = validator.validateId(id);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            if (storeDao.findById(id) == null) {
                System.out.println("Error: Store not found! Please enter a valid ID.");
                continue;
            }
            break;
        }

        if (storeDao.delete(id)) {
            System.out.println("Store deleted successfully!");
        } else {
            System.out.println("Failed to delete store.");
        }
    }

    private static void searchRecordById() {
        String id;
        while (true) {
            System.out.print("Enter Store ID to search: ");
            id = scanner.nextLine().trim();
            String error = validator.validateId(id);
            if (error != null) {
                System.out.println("Error: " + error);
                continue;
            }
            Store store = storeDao.findById(id);
            if (store != null) {
                System.out.println("Store Found:");
                System.out.println(store);
                break;
            } else {
                System.out.println("Store not found! Please enter a valid ID.");
            }
        }
    }
}

