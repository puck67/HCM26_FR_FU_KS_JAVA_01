package dao;

import entity.Book;
import util.DBConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    /**
     * Adds a new book to the database via the insert_book Stored Procedure.
     * @param book The book object to add
     * @return true if successful, false if failed
     */
    public boolean add(Book book) {
        String sql = "CALL insert_book(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthorEmail());
            stmt.setString(4, book.getPublisherPhone());
            stmt.setDouble(5, book.getPrice());
            stmt.setInt(6, book.getQuantity());
            
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all books in the database via the get_all_books Stored Function.
     * @return A list of books
     */
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        String sql = "{call get_all_books()}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(new Book(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("author_email"),
                    rs.getString("publisher_phone"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving list of books: " + e.getMessage());
        }
        return books;
    }

    /**
     * Updates book information via the update_book Stored Procedure.
     * @param book The book object containing new information
     * @return true if successful, false if failed
     */
    public boolean update(Book book) {
        String sql = "CALL update_book(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthorEmail());
            stmt.setString(4, book.getPublisherPhone());
            stmt.setDouble(5, book.getPrice());
            stmt.setInt(6, book.getQuantity());
            
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a book by ID via the delete_book Stored Procedure.
     * @param id The book ID to delete
     * @return true if successful, false if failed
     */
    public boolean delete(String id) {
        String sql = "CALL delete_book(?)";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, id);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting book with ID " + id + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches for a book by ID via the find_book_by_id Stored Function.
     * @param id The book ID to search
     * @return The Book object if found, otherwise null
     */
    public Book findById(String id) {
        String sql = "{call find_book_by_id(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author_email"),
                        rs.getString("publisher_phone"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching for book with ID " + id + ": " + e.getMessage());
        }
        return null;
    }
}
