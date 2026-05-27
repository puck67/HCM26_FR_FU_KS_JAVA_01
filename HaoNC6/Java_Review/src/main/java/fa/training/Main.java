package fa.training;

import fa.training.dao.CategoryDAO;
import fa.training.dao.ManufacturerDAO;
import fa.training.dao.VehicleDAO;
import fa.training.dao.impl.CategoryDAOImpl;
import fa.training.dao.impl.ManufacturerDAOImpl;
import fa.training.dao.impl.VehicleDAOImpl;
import fa.training.entities.Category;
import fa.training.entities.Manufacturer;
import fa.training.entities.Vehicle;
import fa.training.util.ConsoleUtil;
import fa.training.util.Validator;

import java.util.List;

public class Main {

    private static final VehicleDAO vehicleDAO = new VehicleDAOImpl();
    private static final ManufacturerDAO manufacturerDAO = new ManufacturerDAOImpl();
    private static final CategoryDAO categoryDAO = new CategoryDAOImpl();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = ConsoleUtil.readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> vehicleManagementMenu();
                case 2 -> manufacturerManagementMenu();
                case 3 -> categoryManagementMenu();
                case 4 -> {
                    System.out.println("Goodbye!");
                    exit = true;
                }
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("""

                ====================================
                === MAIN SYSTEM MENU ===
                ====================================
                1. Vehicle Management
                2. Manufacturer Management
                3. Category Management
                4. Exit
                ====================================
                """);
    }

    private static void vehicleManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""

                    ====================================
                    === VEHICLE MANAGEMENT ===
                    ====================================
                    1. Add new vehicle
                    2. Display all vehicles
                    3. Update a vehicle
                    4. Delete a vehicle
                    5. Search vehicle by ID
                    6. Back to main menu
                    ====================================
                    """);
            int choice = ConsoleUtil.readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> addNewRecord();
                case 2 -> displayAllRecords();
                case 3 -> updateRecord();
                case 4 -> deleteRecord();
                case 5 -> searchRecordById();
                case 6 -> back = true;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private static void manufacturerManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""

                    ====================================
                    === MANUFACTURER MANAGEMENT ===
                    ====================================
                    1. Add new manufacturer
                    2. Display all manufacturers
                    3. Update a manufacturer
                    4. Delete a manufacturer
                    5. Search manufacturer by ID
                    6. Back to main menu
                    ====================================
                    """);
            int choice = ConsoleUtil.readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> addNewManufacturer();
                case 2 -> displayAllManufacturers();
                case 3 -> updateManufacturer();
                case 4 -> deleteManufacturer();
                case 5 -> searchManufacturerById();
                case 6 -> back = true;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private static void categoryManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("""

                    ====================================
                    === CATEGORY MANAGEMENT ===
                    ====================================
                    1. Add new category
                    2. Display all categories
                    3. Update a category
                    4. Delete a category
                    5. Search category by ID
                    6. Back to main menu
                    ====================================
                    """);
            int choice = ConsoleUtil.readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> addNewCategory();
                case 2 -> displayAllCategories();
                case 3 -> updateCategory();
                case 4 -> deleteCategory();
                case 5 -> searchCategoryById();
                case 6 -> back = true;
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private static void addNewRecord() {
        System.out.println("\n--- Add New Vehicle ---");

        // 1. Auto-generate ID
        String id = generateVehicleId();
        System.out.println("Generated Vehicle ID: " + id);

        // 2. Validate Model
        String model;
        while (true) {
            model = ConsoleUtil.readString("Enter Vehicle Model: ");
            if (!Validator.isValidModel(model)) {
                System.out.println("Model cannot be empty!");
                continue;
            }
            break;
        }

        // 3. Validate Price
        double price;
        while (true) {
            price = ConsoleUtil.readDouble("Enter Vehicle Price ($): ");
            if (!Validator.isValidPrice(price)) {
                System.out.println("Price must be a positive numeric value!");
                continue;
            }
            break;
        }

        // 4. Validate Email
        String email;
        while (true) {
            email = ConsoleUtil.readString("Enter Owner Email: ");
            if (!Validator.isValidEmail(email)) {
                System.out.println("Invalid Email format (e.g., owner@example.com)!");
                continue;
            }
            break;
        }

        // 5. Validate Phone
        String phone;
        while (true) {
            phone = ConsoleUtil.readString("Enter Owner Phone (10-11 digits): ");
            if (!Validator.isValidPhone(phone)) {
                System.out.println(
                        "Invalid Phone format! Phone must contain only digits and be 10 or 11 characters long.");
                continue;
            }
            break;
        }

        // 6. Select Manufacturer
        List<Manufacturer> manufacturers = manufacturerDAO.getAll();
        Manufacturer manufacturer = null;
        while (true) {
            System.out.println("Available Manufacturers:");
            for (var m : manufacturers) {
                System.out.printf("  [%s] %s (%s)%n", m.getId(), m.getName(), m.getCountry());
            }
            String mId = ConsoleUtil.readString("Select Manufacturer ID: ");
            manufacturer = manufacturers.stream()
                    .filter(m -> m.getId().equalsIgnoreCase(mId))
                    .findFirst()
                    .orElse(null);

            if (manufacturer == null) {
                System.out.println("Invalid Manufacturer ID. Please select from the list.");
                continue;
            }
            break;
        }

        // 7. Select Category
        List<Category> categories = categoryDAO.getAll();
        Category category = null;
        while (true) {
            System.out.println("Available Categories:");
            for (var c : categories) {
                System.out.printf("  [%s] %s - %s%n", c.getId(), c.getName(), c.getDescription());
            }
            String cId = ConsoleUtil.readString("Select Category ID: ");
            category = categories.stream()
                    .filter(c -> c.getId().equalsIgnoreCase(cId))
                    .findFirst()
                    .orElse(null);

            if (category == null) {
                System.out.println("Invalid Category ID. Please select from the list.");
                continue;
            }
            break;
        }

        // Insert
        Vehicle vehicle = new Vehicle(id, model, price, email, phone, manufacturer, category);
        if (vehicleDAO.add(vehicle)) {
            System.out.println("Vehicle record added successfully!");
        } else {
            System.out.println("Failed to add vehicle record.");
        }
    }

    private static void displayAllRecords() {
        System.out.println("\n--- Display All Vehicle Records ---");
        List<Vehicle> list = vehicleDAO.getAll();
        ConsoleUtil.printVehicleTable(list);
    }

    private static void updateRecord() {
        System.out.println("\n--- Update Vehicle Record ---");
        String id = ConsoleUtil.readString("Enter Vehicle ID to update: ");
        Vehicle vehicle = vehicleDAO.findById(id);
        if (vehicle == null) {
            System.out.println("Vehicle with ID " + id + " not found!");
            return;
        }

        System.out.println("Current information: " + vehicle);
        System.out.println("Leave input blank to keep current value.");

        // 1. Update Model
        String newModel = ConsoleUtil.readString("Enter new Model (" + vehicle.getModel() + "): ");
        if (!newModel.isBlank()) {
            vehicle.setModel(newModel);
        }

        // 2. Update Price
        while (true) {
            String priceStr = ConsoleUtil.readString("Enter new Price ($) (" + vehicle.getPrice() + "): ");
            if (priceStr.isBlank()) {
                break;
            }
            double price = ConsoleUtil.parseDoubleOrDefault(priceStr, -1);
            if (!Validator.isValidPrice(price)) {
                System.out.println("Price must be a positive numeric value!");
                continue;
            }
            vehicle.setPrice(price);
            break;
        }

        // 3. Update Email
        while (true) {
            String newEmail = ConsoleUtil.readString("Enter new Owner Email (" + vehicle.getOwnerEmail() + "): ");
            if (newEmail.isBlank()) {
                break;
            }
            if (!Validator.isValidEmail(newEmail)) {
                System.out.println("Invalid Email format!");
                continue;
            }
            vehicle.setOwnerEmail(newEmail);
            break;
        }

        // 4. Update Phone
        while (true) {
            String newPhone = ConsoleUtil.readString("Enter new Owner Phone (" + vehicle.getOwnerPhone() + "): ");
            if (newPhone.isBlank()) {
                break;
            }
            if (!Validator.isValidPhone(newPhone)) {
                System.out.println("Invalid Phone format (10-11 digits)!");
                continue;
            }
            vehicle.setOwnerPhone(newPhone);
            break;
        }

        // 5. Update Manufacturer
        List<Manufacturer> manufacturers = manufacturerDAO.getAll();
        while (true) {
            System.out.println("Available Manufacturers:");
            for (var m : manufacturers) {
                System.out.printf("  [%s] %s (%s)%n", m.getId(), m.getName(), m.getCountry());
            }
            String mId = ConsoleUtil.readString("Select new Manufacturer ID or press Enter to keep current ("
                    + vehicle.getManufacturer().getId() + "): ");
            if (mId.isBlank()) {
                break;
            }
            Manufacturer manufacturer = manufacturers.stream()
                    .filter(m -> m.getId().equalsIgnoreCase(mId))
                    .findFirst()
                    .orElse(null);
            if (manufacturer == null) {
                System.out.println("Invalid Manufacturer ID. Please select from the list.");
                continue;
            }
            vehicle.setManufacturer(manufacturer);
            break;
        }

        // 6. Update Category
        List<Category> categories = categoryDAO.getAll();
        while (true) {
            System.out.println("Available Categories:");
            for (var c : categories) {
                System.out.printf("  [%s] %s - %s%n", c.getId(), c.getName(), c.getDescription());
            }
            String cId = ConsoleUtil.readString("Select new Category ID or press Enter to keep current ("
                    + vehicle.getCategory().getId() + "): ");
            if (cId.isBlank()) {
                break;
            }
            Category category = categories.stream()
                    .filter(c -> c.getId().equalsIgnoreCase(cId))
                    .findFirst()
                    .orElse(null);
            if (category == null) {
                System.out.println("Invalid Category ID. Please select from the list.");
                continue;
            }
            vehicle.setCategory(category);
            break;
        }

        // Execute Update
        if (vehicleDAO.update(vehicle)) {
            System.out.println("Vehicle record updated successfully!");
        } else {
            System.out.println("Failed to update vehicle record.");
        }
    }

    private static void deleteRecord() {
        System.out.println("\n--- Delete Vehicle Record ---");
        String id = ConsoleUtil.readString("Enter Vehicle ID to delete: ");
        Vehicle vehicle = vehicleDAO.findById(id);
        if (vehicle == null) {
            System.out.println("Vehicle with ID " + id + " not found!");
            return;
        }

        System.out.println(
                "Are you sure you want to delete vehicle: " + vehicle.getModel() + " (" + vehicle.getId() + ")?");
        String confirmation = ConsoleUtil.readString("Type 'Y' to confirm, any other key to cancel: ");
        if (confirmation.equalsIgnoreCase("Y")) {
            if (vehicleDAO.delete(id)) {
                System.out.println("Vehicle record deleted successfully!");
            } else {
                System.out.println("Failed to delete vehicle record.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void searchRecordById() {
        System.out.println("\n--- Search Vehicle by ID ---");
        String id = ConsoleUtil.readString("Enter Vehicle ID: ");
        Vehicle vehicle = vehicleDAO.findById(id);
        if (vehicle != null) {
            System.out.println("Found Record:");
            ConsoleUtil.printVehicleTable(List.of(vehicle));
        } else {
            System.out.println("Vehicle with ID " + id + " not found.");
        }
    }

    // ====================================
    // MANUFACTURER CRUD METHODS
    // ====================================

    private static void addNewManufacturer() {
        System.out.println("\n--- Add New Manufacturer ---");

        String id = generateManufacturerId();
        System.out.println("Generated Manufacturer ID: " + id);

        String name;
        while (true) {
            name = ConsoleUtil.readString("Enter Manufacturer Name: ");
            if (!Validator.isValidName(name)) {
                System.out.println("Invalid Name! Name cannot be empty and must be under 100 characters.");
                continue;
            }
            break;
        }

        String country;
        while (true) {
            country = ConsoleUtil.readString("Enter Country: ");
            if (!Validator.isValidCountry(country)) {
                System.out.println("Invalid Country! Country cannot be empty and must be under 50 characters.");
                continue;
            }
            break;
        }

        Manufacturer manufacturer = new Manufacturer(id, name, country);
        if (manufacturerDAO.add(manufacturer)) {
            System.out.println("Manufacturer added successfully!");
        } else {
            System.out.println("Failed to add manufacturer.");
        }
    }

    private static void displayAllManufacturers() {
        System.out.println("\n--- Display All Manufacturers ---");
        List<Manufacturer> list = manufacturerDAO.getAll();
        ConsoleUtil.printManufacturerTable(list);
    }

    private static void updateManufacturer() {
        System.out.println("\n--- Update Manufacturer ---");
        String id = ConsoleUtil.readString("Enter Manufacturer ID to update: ");
        Manufacturer manufacturer = manufacturerDAO.findById(id);
        if (manufacturer == null) {
            System.out.println("Manufacturer with ID " + id + " not found!");
            return;
        }

        System.out.println("Current details: ID=" + manufacturer.getId() + ", Name=" + manufacturer.getName() + ", Country=" + manufacturer.getCountry());
        System.out.println("Leave input blank to keep current value.");

        while (true) {
            String newName = ConsoleUtil.readString("Enter new Name (" + manufacturer.getName() + "): ");
            if (newName.isBlank()) {
                break;
            }
            if (!Validator.isValidName(newName)) {
                System.out.println("Invalid Name! Must be under 100 characters.");
                continue;
            }
            manufacturer.setName(newName);
            break;
        }

        while (true) {
            String newCountry = ConsoleUtil.readString("Enter new Country (" + manufacturer.getCountry() + "): ");
            if (newCountry.isBlank()) {
                break;
            }
            if (!Validator.isValidCountry(newCountry)) {
                System.out.println("Invalid Country! Must be under 50 characters.");
                continue;
            }
            manufacturer.setCountry(newCountry);
            break;
        }

        if (manufacturerDAO.update(manufacturer)) {
            System.out.println("Manufacturer updated successfully!");
        } else {
            System.out.println("Failed to update manufacturer.");
        }
    }

    private static void deleteManufacturer() {
        System.out.println("\n--- Delete Manufacturer ---");
        String id = ConsoleUtil.readString("Enter Manufacturer ID to delete: ");
        Manufacturer manufacturer = manufacturerDAO.findById(id);
        if (manufacturer == null) {
            System.out.println("Manufacturer with ID " + id + " not found!");
            return;
        }

        System.out.println("Are you sure you want to delete manufacturer: " + manufacturer.getName() + " (" + manufacturer.getId() + ")?");
        String confirmation = ConsoleUtil.readString("Type 'Y' to confirm, any other key to cancel: ");
        if (confirmation.equalsIgnoreCase("Y")) {
            if (manufacturerDAO.delete(id)) {
                System.out.println("Manufacturer deleted successfully!");
            } else {
                System.out.println("Failed to delete manufacturer. It might be referenced by existing vehicles.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void searchManufacturerById() {
        System.out.println("\n--- Search Manufacturer by ID ---");
        String id = ConsoleUtil.readString("Enter Manufacturer ID: ");
        Manufacturer manufacturer = manufacturerDAO.findById(id);
        if (manufacturer != null) {
            System.out.println("Found Manufacturer:");
            ConsoleUtil.printManufacturerTable(List.of(manufacturer));
        } else {
            System.out.println("Manufacturer with ID " + id + " not found.");
        }
    }

    // ====================================
    // CATEGORY CRUD METHODS
    // ====================================

    private static void addNewCategory() {
        System.out.println("\n--- Add New Category ---");

        String id = generateCategoryId();
        System.out.println("Generated Category ID: " + id);

        String name;
        while (true) {
            name = ConsoleUtil.readString("Enter Category Name: ");
            if (!Validator.isValidName(name)) {
                System.out.println("Invalid Name! Name cannot be empty and must be under 100 characters.");
                continue;
            }
            break;
        }

        String description;
        while (true) {
            description = ConsoleUtil.readString("Enter Description: ");
            if (!Validator.isValidDescription(description)) {
                System.out.println("Invalid Description! Description must be under 255 characters.");
                continue;
            }
            break;
        }

        Category category = new Category(id, name, description);
        if (categoryDAO.add(category)) {
            System.out.println("Category added successfully!");
        } else {
            System.out.println("Failed to add category.");
        }
    }

    private static void displayAllCategories() {
        System.out.println("\n--- Display All Categories ---");
        List<Category> list = categoryDAO.getAll();
        ConsoleUtil.printCategoryTable(list);
    }

    private static void updateCategory() {
        System.out.println("\n--- Update Category ---");
        String id = ConsoleUtil.readString("Enter Category ID to update: ");
        Category category = categoryDAO.findById(id);
        if (category == null) {
            System.out.println("Category with ID " + id + " not found!");
            return;
        }

        System.out.println("Current details: ID=" + category.getId() + ", Name=" + category.getName() + ", Description=" + category.getDescription());
        System.out.println("Leave input blank to keep current value.");

        while (true) {
            String newName = ConsoleUtil.readString("Enter new Name (" + category.getName() + "): ");
            if (newName.isBlank()) {
                break;
            }
            if (!Validator.isValidName(newName)) {
                System.out.println("Invalid Name! Must be under 100 characters.");
                continue;
            }
            category.setName(newName);
            break;
        }

        while (true) {
            String newDesc = ConsoleUtil.readString("Enter new Description (" + category.getDescription() + "): ");
            if (newDesc.isBlank()) {
                break;
            }
            if (!Validator.isValidDescription(newDesc)) {
                System.out.println("Invalid Description! Must be under 255 characters.");
                continue;
            }
            category.setDescription(newDesc);
            break;
        }

        if (categoryDAO.update(category)) {
            System.out.println("Category updated successfully!");
        } else {
            System.out.println("Failed to update category.");
        }
    }

    private static void deleteCategory() {
        System.out.println("\n--- Delete Category ---");
        String id = ConsoleUtil.readString("Enter Category ID to delete: ");
        Category category = categoryDAO.findById(id);
        if (category == null) {
            System.out.println("Category with ID " + id + " not found!");
            return;
        }

        System.out.println("Are you sure you want to delete category: " + category.getName() + " (" + category.getId() + ")?");
        String confirmation = ConsoleUtil.readString("Type 'Y' to confirm, any other key to cancel: ");
        if (confirmation.equalsIgnoreCase("Y")) {
            if (categoryDAO.delete(id)) {
                System.out.println("Category deleted successfully!");
            } else {
                System.out.println("Failed to delete category. It might be referenced by existing vehicles.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void searchCategoryById() {
        System.out.println("\n--- Search Category by ID ---");
        String id = ConsoleUtil.readString("Enter Category ID: ");
        Category category = categoryDAO.findById(id);
        if (category != null) {
            System.out.println("Found Category:");
            ConsoleUtil.printCategoryTable(List.of(category));
        } else {
            System.out.println("Category with ID " + id + " not found.");
        }
    }

    // ====================================
    // ID GENERATION METHODS
    // ====================================

    private static String generateVehicleId() {
        List<Vehicle> list = vehicleDAO.getAll();
        int maxNum = 0;
        for (var v : list) {
            String id = v.getId();
            if (id != null && id.startsWith("V")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("V%02d", maxNum + 1);
    }

    private static String generateManufacturerId() {
        List<Manufacturer> list = manufacturerDAO.getAll();
        int maxNum = 0;
        for (var m : list) {
            String id = m.getId();
            if (id != null && id.startsWith("M")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("M%02d", maxNum + 1);
    }

    private static String generateCategoryId() {
        List<Category> list = categoryDAO.getAll();
        int maxNum = 0;
        for (var c : list) {
            String id = c.getId();
            if (id != null && id.startsWith("C")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > maxNum) {
                        maxNum = num;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return String.format("C%02d", maxNum + 1);
    }
}
