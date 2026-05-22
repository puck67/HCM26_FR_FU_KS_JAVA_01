package fa.training.dao.impl;

import fa.training.dao.AuthorDAO;
import fa.training.model.Author;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorDAOImpl
        extends BaseDAOImpl<Author, Integer>
        implements AuthorDAO {

    @Override
    protected String getInsertSQL() {
        return "SELECT insert_author(?)";
    }

    @Override
    protected String getGetAllSQL() {
        return "SELECT * FROM get_all_authors()";
    }

    @Override
    protected String getFindByIdSQL() {
        return "SELECT * FROM find_author_by_id(?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "CALL update_author(?,?)";
    }

    @Override
    protected String getDeleteSQL() {
        return "CALL delete_author(?)";
    }

    @Override
    protected void setInsertParams(
            CallableStatement stmt,
            Author author
    ) throws SQLException {

        stmt.setString(1, author.getName());
    }

    @Override
    protected void setUpdateParams(
            CallableStatement stmt,
            Author author
    ) throws SQLException {

        stmt.setInt(1, author.getId());
        stmt.setString(2, author.getName());
    }

    @Override
    protected void setDeleteParams(
            CallableStatement stmt,
            Integer id
    ) throws SQLException {

        stmt.setInt(1, id);
    }

    @Override
    protected void setFindByIdParams(
            PreparedStatement stmt,
            Integer id
    ) throws SQLException {

        stmt.setInt(1, id);
    }

    @Override
    protected Author mapRow(ResultSet rs)
            throws SQLException {

        Author author = new Author();

        author.setId(rs.getInt("id"));
        author.setName(rs.getString("name"));

        return author;
    }
}