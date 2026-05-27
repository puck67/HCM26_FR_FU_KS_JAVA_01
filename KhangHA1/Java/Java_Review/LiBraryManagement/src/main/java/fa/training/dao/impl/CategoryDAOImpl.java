package fa.training.dao.impl;

import fa.training.dao.CategoryDAO;
import fa.training.model.Category;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAOImpl
        extends BaseDAOImpl<Category, Integer>
        implements CategoryDAO {

    @Override
    protected String getInsertSQL() {
        return "SELECT insert_category(?)";
    }

    @Override
    protected String getGetAllSQL() {
        return "SELECT * FROM get_all_categories()";
    }

    @Override
    protected String getFindByIdSQL() {
        return "SELECT * FROM find_category_by_id(?)";
    }

    @Override
    protected String getUpdateSQL() {
        return "CALL update_category(?,?)";
    }

    @Override
    protected String getDeleteSQL() {
        return "CALL delete_category(?)";
    }

    @Override
    protected void setInsertParams(
            CallableStatement stmt,
            Category category
    ) throws SQLException {

        stmt.setString(1, category.getName());
    }

    @Override
    protected void setUpdateParams(
            CallableStatement stmt,
            Category category
    ) throws SQLException {

        stmt.setInt(1, category.getId());
        stmt.setString(2, category.getName());
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
    protected Category mapRow(ResultSet rs)
            throws SQLException {

        Category category = new Category();

        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));

        return category;
    }
}