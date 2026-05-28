package src.main;

import src.dao.ProductDAO;
import src.db.DBConnection;
import src.entities.Product;
import src.utils.Validation;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProductManagement {

    private static final Scanner sc = new Scanner(System.in);
    private static final ProductDAO dao = new ProductDAO();

    public static void main(String[] args) {
        try {
            DBConnection.getConnection();
            System.out.println("Connected successfully.");
        } catch (SQLException e) {
            System.err.println("Cannot connect to database: " + e.getMessage());
            return;
        }

        int choice;
        do {
            printMenu();
            choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addProduct();
                case 2 -> displayAll();
                case 3 -> updateProduct();
                case 4 -> deleteProduct();
                case 5 -> searchById();
                case 6 -> {
                    DBConnection.closeConnection();
                    System.out.println("Goodbye");
                }
                default -> System.out.println("Invalid choice. Please enter 1-6.");
            }
        } while (choice != 6);
    }

    private static void printMenu() {
        System.out.println("\n========== PRODUCT MANAGEMENT ==========");
        System.out.println("1. Add new product");
        System.out.println("2. Display all products");
        System.out.println("3. Update a product");
        System.out.println("4. Delete a product");
        System.out.println("5. Search product by ID");
        System.out.println("6. Exit");
        System.out.println("=========================================");
    }

    private static void addProduct() {
        System.out.println("\n--- Add New Product ---");
        try {
            List<String> existingIds = dao.getAll().stream()
                    .map(Product::getId).collect(Collectors.toList());

            String id;
            while (true) {
                id = readString("Enter ID: ");
                if (!Validation.isNotEmpty(id))
                    System.out.println("ID cannot be empty.");
                else if (!Validation.isUniqueId(id, existingIds))
                    System.out.println("ID already exists.");
                else break;
            }

            String name;
            while (true) {
                name = readString("Enter name: ");
                if (Validation.isNotEmpty(name))
                    break;
                System.out.println("Name cannot be empty.");
            }

            double price = 0;
            while (true) {
                String input = readString("Enter price (> 0): ");
                if (Validation.isPositiveDouble(input)) {
                    price = Double.parseDouble(input);
                    break;
                }
                System.out.println("Price must be a number greater than 0.");
            }

            int quantity = 0;
            while (true) {
                String input = readString("Enter quantity (>= 0): ");
                if (Validation.isNonNegativeInt(input)) {
                    quantity = Integer.parseInt(input);
                    break;
                }
                System.out.println("Quantity must be a non-negative integer.");
            }

            String category;
            while (true) {
                category = readString("Enter category: ");
                if (Validation.isNotEmpty(category))
                    break;
                System.out.println("Category cannot be empty.");
            }

            dao.add(new Product(id, name, price, quantity, category));
            System.out.println("Product added successfully!");

        } catch (SQLException e) {
            System.err.println("DB Error: " + e.getMessage());
        }
    }

    private static void displayAll() {
        try {
            List<Product> list = dao.getAll();
            if (list.isEmpty()) {
                System.out.println("No products found.");
                return;
            }
            String fmt = "%-8s %-20s %10s %6s  %s";
            System.out.println("\n" + String.format(fmt, "ID", "Name", "Price", "Qty", "Category"));
            System.out.println("-".repeat(80));
            for (Product p : list) System.out.println(p);
            System.out.println("Total: " + list.size() + " product(s).");
        } catch (SQLException e) {
            System.err.println("DB Error: " + e.getMessage());
        }
    }

    private static void updateProduct() {
        System.out.println("\n--- Update Product ---");
        try {
            String id = readString("Enter product ID to update: ");
            Product p = dao.findById(id);
            if (p == null) {
                System.out.println("Product not found.");
                return;
            }
            System.out.println("Found: " + p);

            String name = readString("New name [" + p.getName() + "]: ");
            if (Validation.isNotEmpty(name))
                p.setName(name);

            String priceStr = readString("New price [" + p.getPrice() + "]: ");
            if (Validation.isNotEmpty(priceStr)) {
                if (Validation.isPositiveDouble(priceStr)) {
                    p.setPrice(Double.parseDouble(priceStr));
                } else {
                    System.out.println("Invalid price — keeping old value.");
                }
            }

            String qtyStr = readString("New quantity [" + p.getQuantity() + "]: ");
            if (Validation.isNotEmpty(qtyStr)) {
                if (Validation.isNonNegativeInt(qtyStr)){
                    p.setQuantity(Integer.parseInt(qtyStr));
                } else {
                    System.out.println("Invalid quantity — keeping old value.");
                }
            }

            String category = readString("New category [" + p.getCategory() + "]: ");
            if (Validation.isNotEmpty(category))
                p.setCategory(category);

            dao.update(p);
            System.out.println("Product updated successfully!");

        } catch (SQLException e) {
            System.err.println("DB Error: " + e.getMessage());
        }
    }

    private static void deleteProduct() {
        System.out.println("\n--- Delete Product ---");
        try {
            String id = readString("Enter product ID to delete: ");
            if (dao.delete(id)) {
                System.out.println("  [OK] Product deleted successfully!");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.err.println("DB Error: " + e.getMessage());
        }
    }

    private static void searchById() {
        System.out.println("\n--- Search by ID ---");
        try {
            String id = readString("Enter ID: ");
            Product p = dao.findById(id);
            if (p != null) {
                String fmt = "%-8s %-20s %10s %6s  %s";
                System.out.println("\n" + String.format(fmt, "ID", "Name", "Price", "Qty", "Category"));
                System.out.println("-".repeat(80));
                System.out.println(p);
            } else {
                System.out.println("Not found.");
            }
        } catch (SQLException e) {
            System.err.println("DB Error: " + e.getMessage());
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
