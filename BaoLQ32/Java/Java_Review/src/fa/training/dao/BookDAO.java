package fa.training.dao;

import fa.training.database.DBConnection;
import fa.training.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public boolean add(Book book) {
        String sql = "INSERT INTO books (id, title, author, email, phone, price, quantity, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getId());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getEmail());
            stmt.setString(5, book.getPhone());
            stmt.setDouble(6, book.getPrice());
            stmt.setInt(7, book.getQuantity());
            stmt.setString(8, book.getCategory());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DB ERROR] add(): " + e.getMessage());
            return false;
        }
    }

    public List<Book> getAll() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT id, title, author, email, phone, price, quantity, category FROM books";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = mapRowToBook(rs);
                list.add(book);
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] getAll(): " + e.getMessage());
        }
        return list;
    }

    public boolean update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, email = ?, phone = ?, price = ?, quantity = ?, category = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getEmail());
            stmt.setString(4, book.getPhone());
            stmt.setDouble(5, book.getPrice());
            stmt.setInt(6, book.getQuantity());
            stmt.setString(7, book.getCategory());
            stmt.setString(8, book.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DB ERROR] update(): " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("[DB ERROR] delete(): " + e.getMessage());
            return false;
        }
    }

    public Book findById(String id) {
        String sql = "SELECT id, title, author, email, phone, price, quantity, category FROM books WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBook(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB ERROR] findById(): " + e.getMessage());
        }
        return null;
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getString("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setEmail(rs.getString("email"));
        book.setPhone(rs.getString("phone"));
        book.setPrice(rs.getDouble("price"));
        book.setQuantity(rs.getInt("quantity"));
        book.setCategory(rs.getString("category"));
        return book;
    }
}
