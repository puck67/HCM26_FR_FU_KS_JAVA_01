package fa.training.service;

import fa.training.dao.BookDAO;
import fa.training.model.Book;
import fa.training.utils.Validator;

import java.util.*;
import java.util.stream.Collectors;

public class BookService {

    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public boolean idExists(String id) {
        return bookDAO.findById(id) != null;
    }

    public Book findById(String id) {
        return bookDAO.findById(id);
    }

    public String add(Book book) {
        if (book == null) {
            return "ERROR: Book data is null.";
        }

        if (!Validator.isNotBlank(book.getId()) || book.getId().length() > 10) {
            return "ERROR: ID cannot be blank and must be at most 10 characters.";
        }
        if (!Validator.isNotBlank(book.getTitle()) || book.getTitle().length() > 100) {
            return "ERROR: Title cannot be blank and must be at most 100 characters.";
        }
        if (!Validator.isNotBlank(book.getAuthor()) || book.getAuthor().length() > 100) {
            return "ERROR: Author cannot be blank and must be at most 100 characters.";
        }
        if (!Validator.isValidEmail(book.getEmail())) {
            return "ERROR: Invalid email format.";
        }
        if (!Validator.isValidPhone(book.getPhone())) {
            return "ERROR: Phone must be 7-15 digits.";
        }
        if (!Validator.isPositiveDouble(book.getPrice())) {
            return "ERROR: Price must be greater than 0.";
        }
        if (!Validator.isNonNegativeInt(book.getQuantity())) {
            return "ERROR: Quantity must be non-negative.";
        }
        if (!Validator.isNotBlank(book.getCategory()) || book.getCategory().length() > 50) {
            return "ERROR: Category cannot be blank and must be at most 50 characters.";
        }

        if (idExists(book.getId())) {
            return "ERROR: ID '" + book.getId() + "' already exists.";
        }

        boolean success = bookDAO.add(book);
        return success ? "SUCCESS: Book added." : "ERROR: Failed to add book to the database.";
    }

    public List<Book> getAll() {
        return bookDAO.getAll();
    }

    public String update(String id, String title, String author,
                         String email, String phone,
                         double price, int quantity, String category) {
        Book existing = bookDAO.findById(id);
        if (existing == null) {
            return "ERROR: Book ID '" + id + "' not found.";
        }

        if (!Validator.isNotBlank(title) || title.length() > 100) {
            return "ERROR: Title cannot be blank and must be at most 100 characters.";
        }
        if (!Validator.isNotBlank(author) || author.length() > 100) {
            return "ERROR: Author cannot be blank and must be at most 100 characters.";
        }
        if (!Validator.isValidEmail(email)) {
            return "ERROR: Invalid email format.";
        }
        if (!Validator.isValidPhone(phone)) {
            return "ERROR: Phone must be 7-15 digits.";
        }
        if (!Validator.isPositiveDouble(price)) {
            return "ERROR: Price must be greater than 0.";
        }
        if (!Validator.isNonNegativeInt(quantity)) {
            return "ERROR: Quantity must be non-negative.";
        }
        if (!Validator.isNotBlank(category) || category.length() > 50) {
            return "ERROR: Category cannot be blank and must be at most 50 characters.";
        }

        Book updatedBook = new Book(id, title, author, email, phone, price, quantity, category);
        boolean success = bookDAO.update(updatedBook);
        return success ? "SUCCESS: Book updated." : "ERROR: Failed to update book in the database.";
    }

    public String delete(String id) {
        if (!Validator.isNotBlank(id)) {
            return "ERROR: Book ID cannot be blank.";
        }
        if (!idExists(id)) {
            return "ERROR: Book ID '" + id + "' not found.";
        }
        boolean success = bookDAO.delete(id);
        return success ? "SUCCESS: Book deleted." : "ERROR: Failed to delete book from the database.";
    }

    public List<Book> searchById(String id) {
        if (!Validator.isNotBlank(id)) {
            return Collections.emptyList();
        }
        return bookDAO.getAll().stream()
                .filter(b -> b.getId().toLowerCase().contains(id.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchByTitle(String keyword) {
        if (!Validator.isNotBlank(keyword)) {
            return Collections.emptyList();
        }
        return bookDAO.getAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> sortByTitle() {
        return bookDAO.getAll().stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());
    }

    public List<Book> sortById() {
        return bookDAO.getAll().stream()
                .sorted(Comparator.comparing(Book::getId))
                .collect(Collectors.toList());
    }

    public List<Book> sortByPrice() {
        return bookDAO.getAll().stream()
                .sorted(Comparator.comparingDouble(Book::getPrice))
                .collect(Collectors.toList());
    }
}
