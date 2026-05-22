package controller;

import service.BookService;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BookController {
    private final BookService bookService = new BookService();

    public void run() {
        Scanner sc = new Scanner(System.in);
        boolean[] running = { true };

        Map<Integer, Runnable> menuActions = new HashMap<>();
        menuActions.put(1, () -> bookService.addBook(sc));
        menuActions.put(2, () -> bookService.displayAllBooks());
        menuActions.put(3, () -> bookService.updateBook(sc));
        menuActions.put(4, () -> bookService.deleteBook(sc));
        menuActions.put(5, () -> bookService.searchBookById(sc));
        menuActions.put(6, () -> {
            System.out.println("Closing the application...");
            sc.close();
            System.out.println("Goodbye!");
            running[0] = false;
        });
        
        while (running[0]) {
            try {
                System.out.println("\n=================================");
                System.out.println("     BOOK MANAGEMENT SYSTEM      ");
                System.out.println("=================================");
                System.out.println("1. Add new record (Book)");
                System.out.println("2. Display all records");
                System.out.println("3. Update a record");
                System.out.println("4. Delete a record");
                System.out.println("5. Search record by ID");
                System.out.println("6. Exit");
                System.out.println("=================================");
                System.out.print("Please enter your choice (1-6): ");

                int choice;
                try {
                    choice = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Error: Input is not a valid number. Please enter a choice between 1 and 6.");
                    continue;
                }

                Runnable action = menuActions.get(choice);
                if (action != null) {
                    action.run();
                } else {
                    System.out.println("Error: Choice out of range. Please choose a number between 1 and 6.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }
}
