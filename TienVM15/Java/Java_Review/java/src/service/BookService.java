package service;

import java.util.List;
import java.util.Scanner;

import repository.BookRepository;
import repository.BookRepositoryImpl;
import model.Book;
import validation.InputValidator;

// Forced rebuild comment to trigger Java Language Server refresh
public class BookService {
    private final BookRepository bookRepository = new BookRepositoryImpl();

    private String readString(Scanner sc, String prompt, String errorMsg,
            java.util.function.Predicate<String> validator) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (validator.test(input)) {
                return input;
            }
            System.out.println("Error: " + errorMsg);
        }
    }

    private double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (InputValidator.isValidPrice(val)) {
                    return val;
                }
                System.out.println("Error: Price must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid decimal number.");
            }
        }
    }

    private int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (InputValidator.isValidQuantity(val)) {
                    return val;
                }
                System.out.println("Error: Quantity must be 0 or greater.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer.");
            }
        }
    }

    public void addBook(Scanner sc) {
        System.out.println("\n--- ADD NEW BOOK ---");

        String id = readString(sc, "Enter Book ID (Bxxxx, e.g., B0001): ",
                "Invalid ID format. Must start with 'B' followed by exactly 4 digits.",
                InputValidator::isValidBookId);

        if (bookRepository.findById(id) != null) {
            System.out.println("Error: A book with ID " + id + " already exists.");
            return;
        }

        String title = readString(sc, "Enter Book Title: ",
                "Title cannot be empty.",
                InputValidator::isNotEmpty);

        String authorEmail = readString(sc, "Enter Author Email: ",
                "Invalid email format.",
                InputValidator::isValidEmail);

        String publisherPhone = readString(sc, "Enter Publisher Phone (10-11 digits): ",
                "Invalid phone format. Must only contain digits and have a length of 10 to 11 characters.",
                InputValidator::isValidPhone);

        double price = readDouble(sc, "Enter Book Price: ");

        int quantity = readInt(sc, "Enter Book Quantity: ");

        Book book = new Book(id, title, authorEmail, publisherPhone, price, quantity);
        if (bookRepository.add(book)) {
            System.out.println("Book added successfully!");
        } else {
            System.out.println("Error: Failed to add book to the database.");
        }
    }

    public void displayAllBooks() {
        System.out.println("\n--- DISPLAY ALL BOOKS ---");
        List<Book> books = bookRepository.getAll();
        if (books.isEmpty()) {
            System.out.println("No books recorded yet.");
            return;
        }

        printTableHeaders();
        for (Book book : books) {
            printBookRow(book);
        }
        printTableDivider();
    }

    public void updateBook(Scanner sc) {
        System.out.println("\n--- UPDATE A BOOK ---");
        System.out.print("Enter Book ID to update: ");
        String id = sc.nextLine().trim();

        Book book = bookRepository.findById(id);
        if (book == null) {
            System.out.println("Error: Book not found with ID: " + id);
            return;
        }

        System.out.println("Book found: " + book);
        System.out.println("Leave empty and press Enter to keep current values.");

        System.out.print("Enter new Title (" + book.getTitle() + "): ");
        String title = sc.nextLine().trim();
        if (!title.isEmpty()) {
            book.setTitle(title);
        }

        while (true) {
            System.out.print("Enter new Author Email (" + book.getAuthorEmail() + "): ");
            String email = sc.nextLine().trim();
            if (email.isEmpty()) {
                break;
            }
            if (InputValidator.isValidEmail(email)) {
                book.setAuthorEmail(email);
                break;
            }
            System.out.println("Error: Invalid email format. Try again.");
        }

        while (true) {
            System.out.print("Enter new Publisher Phone (" + book.getPublisherPhone() + "): ");
            String phone = sc.nextLine().trim();
            if (phone.isEmpty()) {
                break;
            }
            if (InputValidator.isValidPhone(phone)) {
                book.setPublisherPhone(phone);
                break;
            }
            System.out.println("Error: Invalid phone format. Try again.");
        }

        while (true) {
            System.out.print("Enter new Price (" + book.getPrice() + "): ");
            String priceStr = sc.nextLine().trim();
            if (priceStr.isEmpty()) {
                break;
            }
            try {
                double price = Double.parseDouble(priceStr);
                if (InputValidator.isValidPrice(price)) {
                    book.setPrice(price);
                    break;
                }
                System.out.println("Error: Price must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid decimal number.");
            }
        }

        while (true) {
            System.out.print("Enter new Quantity (" + book.getQuantity() + "): ");
            String qtyStr = sc.nextLine().trim();
            if (qtyStr.isEmpty()) {
                break;
            }
            try {
                int qty = Integer.parseInt(qtyStr);
                if (InputValidator.isValidQuantity(qty)) {
                    book.setQuantity(qty);
                    break;
                }
                System.out.println("Error: Quantity must be 0 or greater.");
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid integer number.");
            }
        }

        if (bookRepository.update(book)) {
            System.out.println("Book updated successfully!");
        } else {
            System.out.println("Error: Failed to update book in database.");
        }
    }

    public void deleteBook(Scanner sc) {
        System.out.println("\n--- DELETE A BOOK ---");
        System.out.print("Enter Book ID to delete: ");
        String id = sc.nextLine().trim();

        Book book = bookRepository.findById(id);
        if (book == null) {
            System.out.println("Error: Book not found with ID: " + id);
            return;
        }

        System.out.println("Book found: " + book);
        System.out.print("Are you sure you want to delete this book? (Y/N): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            if (bookRepository.delete(id)) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Error: Failed to delete book from database.");
            }
        } else {
            System.out.println("Deletion canceled.");
        }
    }

    public Book getBookById(String id) {
        return bookRepository.findById(id);
    }

    public void searchBookById(Scanner sc) {
        System.out.println("\n--- SEARCH RECORD BY ID ---");
        System.out.print("Enter Book ID to search: ");
        String id = sc.nextLine().trim();

        Book book = bookRepository.findById(id);
        if (book == null) {
            System.out.println("Error: Book not found with ID: " + id);
            return;
        }

        printTableHeaders();
        printBookRow(book);
        printTableDivider();
    }

    private void printTableHeaders() {
        printTableDivider();
        System.out.printf("| %-6s | %-25s | %-25s | %-12s | %-10s | %-8s |\n",
                "ID", "Title", "Author Email", "Phone", "Price", "Quantity");
        printTableDivider();
    }

    private void printBookRow(Book book) {
        System.out.printf("| %-6s | %-25s | %-25s | %-12s | %-10.2f | %-8d |\n",
                book.getId(),
                truncateString(book.getTitle(), 25),
                truncateString(book.getAuthorEmail(), 25),
                book.getPublisherPhone(),
                book.getPrice(),
                book.getQuantity());
    }

    private void printTableDivider() {
        System.out.println(
                "+--------+---------------------------+---------------------------+--------------+------------+----------+");
    }

    private String truncateString(String str, int len) {
        if (str == null)
            return "";
        if (str.length() <= len)
            return str;
        return str.substring(0, len - 3) + "...";
    }
}
