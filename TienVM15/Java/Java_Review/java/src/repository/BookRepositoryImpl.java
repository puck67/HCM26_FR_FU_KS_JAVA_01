package repository;

import model.Book;
import util.DBConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Forced rebuild comment to trigger Java Language Server refresh
public class BookRepositoryImpl implements BookRepository {

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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
