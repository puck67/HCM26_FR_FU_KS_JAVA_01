package fa.training.main;

import fa.training.model.Book;
import fa.training.service.BookService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final BookService service = new BookService();

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║  BOOK MANAGEMENT SYSTEM (DATABASE)   ║");
        System.out.println("╚══════════════════════════════════════╝");

        Map<String, Runnable> menuActions = new HashMap<>();
        menuActions.put("1", () -> handleAdd());
        menuActions.put("2", () -> handleDisplay());
        menuActions.put("3", () -> handleUpdate());
        menuActions.put("4", () -> handleDelete());
        menuActions.put("5", () -> handleSearch());

        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            if ("6".equals(choice)) {
                running = false;
            } else {
                Runnable action = menuActions.get(choice);
                if (action != null) {
                    action.run();
                } else {
                    System.out.println("  Invalid choice. Please try again.");
                }
            }
        }
        System.out.println("Goodbye!");
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\n┌─────────────────────────────┐");
        System.out.println("│           MENU              │");
        System.out.println("├─────────────────────────────┤");
        System.out.println("│  1. Add new record          │");
        System.out.println("│  2. Display all records     │");
        System.out.println("│  3. Update a record         │");
        System.out.println("│  4. Delete a record         │");
        System.out.println("│  5. Search record by ID     │");
        System.out.println("│  6. Exit                    │");
        System.out.println("└─────────────────────────────┘");
        System.out.print("Your choice: ");
    }

    private static void handleAdd() {
        System.out.println("\n--- ADD NEW BOOK ---");

        System.out.print("Book ID: ");
        String id = sc.nextLine().trim();

        System.out.print("Title: ");
        String title = sc.nextLine().trim();

        System.out.print("Author: ");
        String author = sc.nextLine().trim();

        System.out.print("Author email: ");
        String email = sc.nextLine().trim();

        System.out.print("Publisher phone: ");
        String phone = sc.nextLine().trim();

        double price = 0;
        System.out.print("Price: ");
        try {
            price = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            price = -1;
        }

        int qty = 0;
        System.out.print("Quantity: ");
        try {
            qty = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            qty = -1;
        }

        System.out.print("Category: ");
        String cat = sc.nextLine().trim();

        Book book = new Book(id, title, author, email, phone, price, qty, cat);
        System.out.println(service.add(book));
    }

    private static void handleDisplay() {
        System.out.println("\n--- ALL BOOKS FROM DATABASE ---");
        List<Book> list = service.getAll();
        printTable(list);
    }

    private static void handleUpdate() {
        System.out.println("\n--- UPDATE BOOK ---");
        System.out.print("Enter Book ID to update: ");
        String id = sc.nextLine().trim();

        Book existing = service.findById(id);
        if (existing == null) {
            System.out.println("ERROR: Book ID '" + id + "' not found.");
            return;
        }

        System.out.println("Current data: " + existing);
        System.out.println("(Press Enter to keep current value)");

        System.out.print("Title [" + existing.getTitle() + "]: ");
        String titleInput = sc.nextLine().trim();
        String title = titleInput.isEmpty() ? existing.getTitle() : titleInput;

        System.out.print("Author [" + existing.getAuthor() + "]: ");
        String authorInput = sc.nextLine().trim();
        String author = authorInput.isEmpty() ? existing.getAuthor() : authorInput;

        System.out.print("Email [" + existing.getEmail() + "]: ");
        String emailInput = sc.nextLine().trim();
        String email = emailInput.isEmpty() ? existing.getEmail() : emailInput;

        System.out.print("Phone [" + existing.getPhone() + "]: ");
        String phoneInput = sc.nextLine().trim();
        String phone = phoneInput.isEmpty() ? existing.getPhone() : phoneInput;

        System.out.print("Price [" + existing.getPrice() + "]: ");
        String priceInput = sc.nextLine().trim();
        double price = existing.getPrice();
        if (!priceInput.isEmpty()) {
            try {
                price = Double.parseDouble(priceInput);
            } catch (NumberFormatException e) {
                price = -1;
            }
        }

        System.out.print("Quantity [" + existing.getQuantity() + "]: ");
        String qtyInput = sc.nextLine().trim();
        int qty = existing.getQuantity();
        if (!qtyInput.isEmpty()) {
            try {
                qty = Integer.parseInt(qtyInput);
            } catch (NumberFormatException e) {
                qty = -1;
            }
        }

        System.out.print("Category [" + existing.getCategory() + "]: ");
        String catInput = sc.nextLine().trim();
        String category = catInput.isEmpty() ? existing.getCategory() : catInput;

        System.out.println(service.update(id, title, author, email, phone, price, qty, category));
    }

    private static void handleDelete() {
        System.out.println("\n--- DELETE BOOK ---");
        System.out.print("Enter Book ID to delete: ");
        String id = sc.nextLine().trim();

        Book book = service.findById(id);
        if (book == null) {
            System.out.println("ERROR: Book ID '" + id + "' not found.");
            return;
        }

        System.out.println("Found: " + book);
        System.out.print("Confirm delete? (y/n): ");
        String confirm = sc.nextLine().trim();
        if ("y".equalsIgnoreCase(confirm)) {
            System.out.println(service.delete(id));
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private static void handleSearch() {
        System.out.println("\n--- SEARCH RECORD BY ID ---");
        System.out.print("Enter ID to search: ");
        String id = sc.nextLine().trim();

        Book book = service.findById(id);
        if (book == null) {
            System.out.println("  (No record found with ID '" + id + "')");
        } else {
            System.out.println("\nFound Book:");
            String header = String.format("| %-8s | %-30s | %-20s | %-10s | %-5s | %-15s |",
                    "ID", "Title", "Author", "Price", "Qty", "Category");
            String line = "-".repeat(header.length());
            System.out.println(line);
            System.out.println(header);
            System.out.println(line);
            System.out.println(book);
            System.out.println(line);
        }
    }

    private static void printTable(List<Book> list) {
        if (list.isEmpty()) {
            System.out.println("  (No records found)");
            return;
        }
        String header = String.format("| %-8s | %-30s | %-20s | %-10s | %-5s | %-15s |",
                "ID", "Title", "Author", "Price", "Qty", "Category");
        String line = "-".repeat(header.length());
        System.out.println(line);
        System.out.println(header);
        System.out.println(line);
        list.forEach(System.out::println);
        System.out.println(line);
        System.out.println("Total: " + list.size() + " record(s).");
    }
}
