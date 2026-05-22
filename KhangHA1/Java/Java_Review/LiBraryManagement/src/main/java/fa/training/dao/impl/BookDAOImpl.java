package fa.training.dao.impl;

import fa.training.dao.BookDAO;
import fa.training.model.Book;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAOImpl
        extends BaseDAOImpl<Book, String>
        implements BookDAO {

    @Override
    protected String getInsertSQL() {
        return "CALL insert_book(?,?,?,?,?,?)";
    }

    @Override
    protected String getGetAllSQL() {
        return "SELECT * FROM get_all_books()";
    }

    @Override
    protected String getFindByIdSQL() {
        return "SELECT * FROM find_book_by_id(?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "CALL update_book(?,?,?,?,?,?)";
    }

    @Override
    protected String getDeleteSQL() {
        return "CALL delete_book(?)";
    }

    @Override
    protected void setInsertParams(
            CallableStatement stmt,
            Book book
    ) throws SQLException {

        stmt.setString(1, book.getId());
        stmt.setString(2, book.getTitle());
        stmt.setInt(3, book.getAuthorId());
        stmt.setInt(4, book.getCategoryId());
        stmt.setInt(5, book.getPublishYear());
        stmt.setDouble(6, book.getPrice());
    }

    @Override
    protected void setUpdateParams(
            CallableStatement stmt,
            Book book
    ) throws SQLException {

        stmt.setString(1, book.getId());
        stmt.setString(2, book.getTitle());
        stmt.setInt(3, book.getAuthorId());
        stmt.setInt(4, book.getCategoryId());
        stmt.setInt(5, book.getPublishYear());
        stmt.setDouble(6, book.getPrice());
    }

    @Override
    protected void setDeleteParams(
            CallableStatement stmt,
            String id
    ) throws SQLException {

        stmt.setString(1, id);
    }

    @Override
    protected void setFindByIdParams(
            PreparedStatement stmt,
            String id
    ) throws SQLException {

        stmt.setString(1, id);
    }

    @Override
    protected Book mapRow(ResultSet rs)
            throws SQLException {

        Book book = new Book();

        book.setId(rs.getString("id"));
        book.setTitle(rs.getString("title"));

        book.setAuthorId(rs.getInt("author_id"));
        book.setAuthorName(rs.getString("author_name"));

        book.setCategoryId(rs.getInt("category_id"));
        book.setCategoryName(rs.getString("category_name"));

        book.setPublishYear(rs.getInt("publish_year"));

        book.setPrice(rs.getDouble("price"));

        return book;
    }
}