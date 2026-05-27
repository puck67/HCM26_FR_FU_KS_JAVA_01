package fa.training;

import fa.training.dao.EmployeeDAO;
import fa.training.dao.ProductDAO;
import fa.training.dao.WarehouseDAO;
import fa.training.dao.impl.EmployeeDAOImpl;
import fa.training.dao.impl.ProductDAOImpl;
import fa.training.dao.impl.WarehouseDAOImpl;
import fa.training.model.Employee;
import fa.training.model.Product;
import fa.training.model.Warehouse;
import fa.training.validation.Validator;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final WarehouseDAO warehouseDAO = new WarehouseDAOImpl();
    private static final EmployeeDAO employeeDAO = new EmployeeDAOImpl();
    private static final ProductDAO productDAO = new ProductDAOImpl();

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=======================================================");
            System.out.println("        WAREHOUSE MANAGEMENT SYSTEM (JDBC)             ");
            System.out.println("=======================================================");
            System.out.println("1. Add new Warehouse");
            System.out.println("2. Display all Warehouses");
            System.out.println("3. Update a Warehouse");
            System.out.println("4. Delete a Warehouse");
            System.out.println("5. Search Warehouse by ID");
            System.out.println("6. Add Employee to a Warehouse");
            System.out.println("7. Add Product to a Warehouse");
            System.out.println("8. Exit");
            System.out.print("Please enter your choice (1-8): ");

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer choice!");
                continue;
            }

            // Using Java 14+ Enhanced Switch Expression
            switch (choice) {
                case 1 -> addNewWarehouse(scanner);
                case 2 -> displayAllWarehouses();
                case 3 -> updateWarehouse(scanner);
                case 4 -> deleteWarehouse(scanner);
                case 5 -> searchWarehouseById(scanner);
                case 6 -> addEmployeeToWarehouse(scanner);
                case 7 -> addProductToWarehouse(scanner);
                case 8 -> {
                    System.out.println("Thank you for using the system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! Please select between 1 and 8.");
            }
        }
    }

    private static void addNewWarehouse(Scanner scanner) {
        System.out.println("\n--- ADD NEW WAREHOUSE ---");
        try {
            System.out.print("Enter Warehouse ID (Format: WH-XXX): ");
            var id = scanner.nextLine().trim();
            if (!Validator.isValidWarehouseId(id)) {
                System.out.println("Error: Invalid Warehouse ID format (e.g., WH-001)!");
                return;
            }

            System.out.print("Enter Warehouse Name: ");
            var name = scanner.nextLine().trim();
            if (!Validator.isValidName(name)) {
                System.out.println("Error: Invalid Warehouse Name!");
                return;
            }

            System.out.print("Enter Warehouse Address: ");
            var address = scanner.nextLine().trim();
            if (!Validator.isValidAddress(address)) {
                System.out.println("Error: Address must be between 5 and 200 characters!");
                return;
            }

            System.out.print("Enter Warehouse Capacity: ");
            int capacity = Integer.parseInt(scanner.nextLine().trim());
            if (!Validator.isValidCapacity(capacity)) {
                System.out.println("Error: Capacity must be greater than 0!");
                return;
            }

            var warehouse = new Warehouse(id, name, address, capacity);
            if (warehouseDAO.add(warehouse)) {
                System.out.println("Successfully added new warehouse!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Capacity must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    private static void displayAllWarehouses() {
        System.out.println("\n--- DISPLAY ALL WAREHOUSES ---");
        try {
            var list = warehouseDAO.getAll();
            if (list.isEmpty()) {
                System.out.println("No warehouses found in the system.");
                return;
            }

            list.forEach(w -> {
                System.out.println("\n-------------------------------------------------------");
                System.out.println(w);
                
                var employees = w.getEmployees();
                if (employees.isEmpty()) {
                    System.out.println("  + Employees: (No employees assigned)");
                } else {
                    System.out.println("  + Employee List:");
                    employees.forEach(emp -> System.out.println("    - " + emp));
                }

                var products = w.getProducts();
                if (products.isEmpty()) {
                    System.out.println("  + Products: (Warehouse is empty)");
                } else {
                    System.out.println("  + Product List:");
                    products.forEach(prod -> System.out.println("    - " + prod));
                }
            });
            System.out.println("-------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    private static void updateWarehouse(Scanner scanner) {
        System.out.println("\n--- UPDATE WAREHOUSE ---");
        try {
            System.out.print("Enter Warehouse ID to update (WH-XXX): ");
            var id = scanner.nextLine().trim();
            
            var existing = warehouseDAO.findById(id);
            if (existing == null) {
                System.out.println("Error: Warehouse not found with ID " + id);
                return;
            }

            System.out.println("Current details: " + existing);
            System.out.print("Enter new Name (leave empty to keep unchanged): ");
            var name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = existing.getName();
            }

            System.out.print("Enter new Address (leave empty to keep unchanged): ");
            var address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                address = existing.getAddress();
            }

            System.out.print("Enter new Capacity (leave empty to keep unchanged): ");
            var capacityInput = scanner.nextLine().trim();
            int capacity = capacityInput.isEmpty() ? existing.getCapacity() : Integer.parseInt(capacityInput);

            var updatedWh = new Warehouse(id, name, address, capacity);
            if (warehouseDAO.update(updatedWh)) {
                System.out.println("Successfully updated warehouse details!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Capacity must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    private static void deleteWarehouse(Scanner scanner) {
        System.out.println("\n--- DELETE WAREHOUSE ---");
        try {
            System.out.print("Enter Warehouse ID to delete (WH-XXX): ");
            var id = scanner.nextLine().trim();

            var existing = warehouseDAO.findById(id);
            if (existing == null) {
                System.out.println("Error: Warehouse not found with ID " + id);
                return;
            }

            System.out.println("Warehouse found: " + existing.getName());
            System.out.print("Are you sure you want to delete this warehouse? All related products and employee associations will be removed. (Y/N): ");
            var confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("Y")) {
                if (warehouseDAO.delete(id)) {
                    System.out.println("Successfully deleted warehouse!");
                }
            } else {
                System.out.println("Delete operation canceled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    private static void searchWarehouseById(Scanner scanner) {
        System.out.println("\n--- SEARCH WAREHOUSE BY ID ---");
        try {
            System.out.print("Enter Warehouse ID to search (WH-XXX): ");
            var id = scanner.nextLine().trim();

            var w = warehouseDAO.findById(id);
            if (w == null) {
                System.out.println("Error: Warehouse not found with ID: " + id);
                return;
            }

            System.out.println("\n=== SEARCH RESULT ===");
            System.out.println(w);
            System.out.println("Address: " + w.getAddress());
            
            var employees = w.getEmployees();
            System.out.println("Employees (" + employees.size() + "):");
            employees.forEach(emp -> System.out.println("  - " + emp));

            var products = w.getProducts();
            System.out.println("Products (" + w.getCurrentProductCount() + " / " + w.getCapacity() + "):");
            products.forEach(prod -> System.out.println("  - " + prod));
            System.out.println("=====================");

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    private static void addEmployeeToWarehouse(Scanner scanner) {
        System.out.println("\n--- ADD EMPLOYEE TO WAREHOUSE ---");
        try {
            System.out.print("Enter Employee ID (Format: EM-XXX): ");
            var id = scanner.nextLine().trim();
            if (!Validator.isValidEmployeeId(id)) {
                System.out.println("Error: Invalid Employee ID format (e.g., EM-001)!");
                return;
            }

            System.out.print("Enter Employee Name: ");
            var name = scanner.nextLine().trim();
            if (!Validator.isValidName(name)) {
                System.out.println("Error: Invalid Employee Name!");
                return;
            }

            System.out.print("Enter Employee Email: ");
            var email = scanner.nextLine().trim();
            if (!Validator.isValidEmail(email)) {
                System.out.println("Error: Invalid Email format!");
                return;
            }

            System.out.print("Enter Employee Phone: ");
            var phone = scanner.nextLine().trim();
            if (!Validator.isValidPhone(phone)) {
                System.out.println("Error: Phone must be 10 or 11 digits starting with 0!");
                return;
            }

            System.out.print("Enter Warehouse ID to assign (WH-XXX, optional): ");
            var warehouseId = scanner.nextLine().trim();
            if (warehouseId.isEmpty()) {
                warehouseId = null;
            } else {
                var wh = warehouseDAO.findById(warehouseId);
                if (wh == null) {
                    System.out.println("Error: Warehouse '" + warehouseId + "' does not exist!");
                    return;
                }
            }

            var employee = new Employee(id, name, email, phone, warehouseId);
            if (employeeDAO.add(employee)) {
                System.out.println("Successfully added employee!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    private static void addProductToWarehouse(Scanner scanner) {
        System.out.println("\n--- ADD PRODUCT TO WAREHOUSE ---");
        try {
            System.out.print("Enter Warehouse ID for storage (WH-XXX): ");
            var warehouseId = scanner.nextLine().trim();
            var wh = warehouseDAO.findById(warehouseId);
            if (wh == null) {
                System.out.println("Error: Warehouse '" + warehouseId + "' does not exist!");
                return;
            }

            System.out.print("Enter Product ID (Format: PR-XXX): ");
            var id = scanner.nextLine().trim();
            if (!Validator.isValidProductId(id)) {
                System.out.println("Error: Invalid Product ID format (e.g., PR-001)!");
                return;
            }

            System.out.print("Enter Product Name: ");
            var name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Error: Product name cannot be empty!");
                return;
            }

            System.out.print("Enter Product Price: ");
            double price = Double.parseDouble(scanner.nextLine().trim());
            if (!Validator.isValidPrice(price)) {
                System.out.println("Error: Price must be greater than 0!");
                return;
            }

            System.out.print("Enter Product Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            if (!Validator.isValidQuantity(quantity)) {
                System.out.println("Error: Quantity must be non-negative!");
                return;
            }

            // Real business constraint: check capacity of the target warehouse
            int currentTotalQty = productDAO.getTotalQuantityByWarehouseId(warehouseId);
            if (currentTotalQty + quantity > wh.getCapacity()) {
                System.out.printf("Error: Cannot add product! Remaining warehouse capacity: %d, but attempted to add: %d (Current: %d/%d).%n",
                        (wh.getCapacity() - currentTotalQty), quantity, currentTotalQty, wh.getCapacity());
                return;
            }

            var product = new Product(id, name, price, quantity, warehouseId);
            if (productDAO.add(product)) {
                System.out.println("Successfully added product to warehouse!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Price and Quantity must be numeric values!");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}