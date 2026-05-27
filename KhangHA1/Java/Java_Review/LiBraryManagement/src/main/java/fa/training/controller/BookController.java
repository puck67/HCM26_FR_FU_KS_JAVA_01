package fa.training.controller;

import fa.training.dao.BookDAO;
import fa.training.dao.impl.BookDAOImpl;
import fa.training.model.Book;
import fa.training.util.ConsoleUtil;
import fa.training.view.BookView;

public class BookController {

    private final BookDAO bookDAO = new BookDAOImpl();

    private final BookView view = new BookView();

    public BookView getView() {
        return view;
    }

    public void addBook() {

        view.printHeader("Add Book");

        Book book = new Book();

        book.setId(ConsoleUtil.readString("Book ID: "));
        book.setTitle(ConsoleUtil.readString("Title: "));

        book.setAuthorId(
                ConsoleUtil.readInt("Author ID: "));

        book.setCategoryId(
                ConsoleUtil.readInt("Category ID: "));

        book.setPublishYear(
                ConsoleUtil.readInt("Publish Year: "));

        book.setPrice(
                ConsoleUtil.readDouble("Price: "));

        if (bookDAO.add(book)) {
            view.printSuccess(
                    "Book added successfully.");
        } else {
            view.printError(
                    "Failed to add book.");
        }
    }

    public void displayAllBooks() {
        view.displayBooks(bookDAO.getAll());
    }

    public void findBookById() {

        view.printHeader("Find Book");

        String id =
                ConsoleUtil.readString("Book ID: ");

        Book book =
                bookDAO.findById(id);

        view.displayBook(book);
    }

    public void updateBook() {

        view.printHeader("Update Book");

        String id =
                ConsoleUtil.readString(
                        "Book ID: ");

        Book existing =
                bookDAO.findById(id);

        if (existing == null) {
            view.printWarning(
                    "Book not found.");
            return;
        }

        String title =
                ConsoleUtil.readString(
                        "Title [" +
                                existing.getTitle()
                                + "]: ");

        if (!title.isBlank()) {
            existing.setTitle(title);
        }

        existing.setAuthorId(
                ConsoleUtil.readInt(
                        "Author ID ["
                                + existing.getAuthorId()
                                + "]: "));

        existing.setCategoryId(
                ConsoleUtil.readInt(
                        "Category ID ["
                                + existing.getCategoryId()
                                + "]: "));

        existing.setPublishYear(
                ConsoleUtil.readInt(
                        "Publish Year ["
                                + existing.getPublishYear()
                                + "]: "));

        existing.setPrice(
                ConsoleUtil.readDouble(
                        "Price ["
                                + existing.getPrice()
                                + "]: "));

        if (bookDAO.update(existing)) {
            view.printSuccess(
                    "Book updated successfully.");
        } else {
            view.printError(
                    "Failed to update book.");
        }
    }

    public void deleteBook() {

        view.printHeader("Delete Book");

        String id =
                ConsoleUtil.readString(
                        "Book ID: ");

        Book existing =
                bookDAO.findById(id);

        if (existing == null) {
            view.printWarning(
                    "Book not found.");
            return;
        }

        if (bookDAO.delete(id)) {
            view.printSuccess(
                    "Book deleted successfully.");
        } else {
            view.printError(
                    "Failed to delete book.");
        }
    }
}