package fa.training.view;

import fa.training.model.Book;

import java.util.List;

public class BookView {

    public void printMenu() {

        System.out.println("""
                
                --- Book Management ---
                1. Add new book
                2. Display all books
                3. Find book by ID
                4. Update book
                5. Delete book
                6. Back to main menu
                """);
    }

    public void displayBooks(List<Book> books) {

        if (books.isEmpty()) {
            System.out.println("\nNo books found.");
            return;
        }

        System.out.printf(
                "\n| %-10s | %-25s | %-20s | %-15s | %-6s | %-10s |%n",
                "ID",
                "Title",
                "Author",
                "Category",
                "Year",
                "Price");

        System.out.println(
                "----------------------------------------------------------------------------------------------------");

        books.forEach(book ->
                System.out.printf(
                        "| %-10s | %-25s | %-20s | %-15s | %-6d | %-10.2f |%n",
                        book.getId(),
                        book.getTitle(),
                        book.getAuthorName(),
                        book.getCategoryName(),
                        book.getPublishYear(),
                        book.getPrice()));
    }

    public void displayBook(Book book) {

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("\n===== BOOK DETAILS =====");
        System.out.println("ID        : " + book.getId());
        System.out.println("Title     : " + book.getTitle());
        System.out.println("Author    : " + book.getAuthorName());
        System.out.println("Category  : " + book.getCategoryName());
        System.out.println("Year      : " + book.getPublishYear());
        System.out.println("Price     : " + book.getPrice());
    }

    public void printHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public void printSuccess(String message) {
        System.out.println("  [+] " + message);
    }

    public void printError(String message) {
        System.out.println("  [x] " + message);
    }

    public void printWarning(String message) {
        System.out.println("  [!] " + message);
    }

    public void printInvalidChoice() {
        System.out.println("Invalid choice.");
    }
}