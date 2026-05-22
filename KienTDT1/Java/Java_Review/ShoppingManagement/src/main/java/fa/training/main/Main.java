package fa.training.main;

import fa.training.dao.ShoppingItemDAO;
import fa.training.dao.impl.ShoppingItemDAOImpl;
import fa.training.entity.ShoppingItem;
import fa.training.utils.ValidationUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static final ShoppingItemDAO dao = new ShoppingItemDAOImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            choice = getChoice();
            switch (choice) {
                case 1:
                    addShoppingItem();
                    break;
                case 2:
                    displayAllItems();
                    break;
                case 3:
                    updateShoppingItem();
                    break;
                case 4:
                    deleteShoppingItem();
                    break;
                case 5:
                    searchShoppingItem();
                    break;
                case 6:
                    sortItemsByName();
                    break;
                case 7:
                    System.out.println("\nExiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select between 1 and 7.");
            }
        } while (choice != 7);
    }

    private static void printMenu() {
        System.out.println("\n=========================================");
        System.out.println("        SHOPPING MANAGEMENT SYSTEM        ");
        System.out.println("=========================================");
        System.out.println("1. Add shopping item");
        System.out.println("2. Display all shopping items");
        System.out.println("3. Update shopping item");
        System.out.println("4. Delete shopping item");
        System.out.println("5. Search shopping item by ID or Name");
        System.out.println("6. Sort shopping items by Name");
        System.out.println("7. Exit");
        System.out.println("=========================================");
    }

    private static int getChoice() {
        while (true) {
            try {
                System.out.print("Please enter your choice (1-7): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    continue;
                }
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 7) {
                    return choice;
                }
                System.out.println("Invalid input! Choice must be between 1 and 7.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
            }
        }
    }

    private static void addShoppingItem() {
        System.out.println("\n--- Add New Shopping Item ---");

        String id;
        while (true) {
            System.out.print("Enter Item ID (e.g. S001): ");
            id = scanner.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("Item ID cannot be empty!");
                continue;
            }
            if (dao.findById(id) != null) {
                System.out.println("Item ID '" + id + "' already exists! Please enter a unique ID.");
                continue;
            }
            break;
        }

        String name;
        while (true) {
            System.out.print("Enter Item Name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Item Name cannot be empty!");
                continue;
            }
            break;
        }

        String category;
        while (true) {
            System.out.print("Enter Category: ");
            category = scanner.nextLine().trim();
            if (category.isEmpty()) {
                System.out.println("Category cannot be empty!");
                continue;
            }
            break;
        }

        double price;
        while (true) {
            try {
                System.out.print("Enter Price (must be > 0): ");
                String priceStr = scanner.nextLine().trim();
                price = Double.parseDouble(priceStr);
                if (!ValidationUtils.isValidPrice(price)) {
                    System.out.println("Price must be greater than 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid format! Please enter a valid number for price.");
            }
        }

        int quantity;
        while (true) {
            try {
                System.out.print("Enter Quantity (must be >= 0): ");
                String quantityStr = scanner.nextLine().trim();
                quantity = Integer.parseInt(quantityStr);
                if (!ValidationUtils.isValidQuantity(quantity)) {
                    System.out.println("Quantity must be greater than or equal to 0!");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid format! Please enter a valid integer for quantity.");
            }
        }

        ShoppingItem newItem = new ShoppingItem(id, name, category, price, quantity);
        boolean success = dao.add(newItem);

        if (success) {
            System.out.println("Success: Shopping item added successfully!");
        } else {
            System.out.println("Error: Failed to add shopping item to database.");
        }
    }

    private static void displayAllItems() {
        System.out.println("\n--- Display All Shopping Items ---");
        List<ShoppingItem> items = dao.getAll();
        printTable(items);
    }

    private static void updateShoppingItem() {
        System.out.println("\n--- Update Shopping Item ---");
        System.out.print("Enter Item ID to update: ");
        String id = scanner.nextLine().trim();

        ShoppingItem existingItem = dao.findById(id);
        if (existingItem == null) {
            System.out.println("Error: Shopping item with ID '" + id + "' not found!");
            return;
        }

        System.out.println("Current details: " + existingItem);
        System.out.println("Leave input blank (press Enter) to keep the current value.");

        System.out.print("Enter New Name [" + existingItem.getItemName() + "]: ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            existingItem.setItemName(newName);
        }

        System.out.print("Enter New Category [" + existingItem.getCategory() + "]: ");
        String newCategory = scanner.nextLine().trim();
        if (!newCategory.isEmpty()) {
            existingItem.setCategory(newCategory);
        }

        while (true) {
            System.out.print("Enter New Price [" + existingItem.getPrice() + "]: ");
            String priceStr = scanner.nextLine().trim();
            if (priceStr.isEmpty()) {
                break;
            }
            try {
                double newPrice = Double.parseDouble(priceStr);
                if (!ValidationUtils.isValidPrice(newPrice)) {
                    System.out.println("Price must be greater than 0!");
                    continue;
                }
                existingItem.setPrice(newPrice);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid format! Please enter a valid number for price.");
            }
        }

        while (true) {
            System.out.print("Enter New Quantity [" + existingItem.getQuantity() + "]: ");
            String quantityStr = scanner.nextLine().trim();
            if (quantityStr.isEmpty()) {
                break;
            }
            try {
                int newQuantity = Integer.parseInt(quantityStr);
                if (!ValidationUtils.isValidQuantity(newQuantity)) {
                    System.out.println("Quantity must be greater than or equal to 0!");
                    continue;
                }
                existingItem.setQuantity(newQuantity);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid format! Please enter a valid integer for quantity.");
            }
        }

        boolean success = dao.update(existingItem);
        if (success) {
            System.out.println("Success: Shopping item updated successfully!");
        } else {
            System.out.println("Error: Failed to update shopping item in database.");
        }
    }

    private static void deleteShoppingItem() {
        System.out.println("\n--- Delete Shopping Item ---");
        System.out.print("Enter Item ID to delete: ");
        String id = scanner.nextLine().trim();

        ShoppingItem existingItem = dao.findById(id);
        if (existingItem == null) {
            System.out.println("Error: Shopping item with ID '" + id + "' not found!");
            return;
        }

        System.out.println("Current details: " + existingItem);
        System.out.print("Are you sure you want to delete this item? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean success = dao.delete(id);
            if (success) {
                System.out.println("Success: Shopping item deleted successfully!");
            } else {
                System.out.println("Error: Failed to delete shopping item from database.");
            }
        } else {
            System.out.println("Deletion canceled.");
        }
    }

    private static void searchShoppingItem() {
        System.out.println("\n--- Search Shopping Item ---");
        System.out.print("Enter search keyword (matches ID or Name): ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty!");
            return;
        }

        List<ShoppingItem> allItems = dao.getAll();
        List<ShoppingItem> filteredItems = allItems.stream()
                .filter(item -> item.getId().toLowerCase().contains(keyword)
                        || item.getItemName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        System.out.println("Search results for '" + keyword + "':");
        printTable(filteredItems);
    }

    private static void sortItemsByName() {
        System.out.println("\n--- Sort Shopping Items by Name ---");
        List<ShoppingItem> items = dao.getAll();
        items.sort(Comparator.comparing(ShoppingItem::getItemName, String.CASE_INSENSITIVE_ORDER));
        printTable(items);
    }

    private static void printTable(List<ShoppingItem> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("No shopping items to display.");
            return;
        }

        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-25s | %-15s | %-10s | %-10s |\n", "ID", "Name", "Category", "Price", "Quantity");
        System.out.println("---------------------------------------------------------------------------------");
        for (ShoppingItem item : items) {
            System.out.printf("| %-10s | %-25s | %-15s | %-10.2f | %-10d |\n",
                    item.getId(),
                    item.getItemName(),
                    item.getCategory(),
                    item.getPrice(),
                    item.getQuantity());
        }
        System.out.println("---------------------------------------------------------------------------------");
    }
}
