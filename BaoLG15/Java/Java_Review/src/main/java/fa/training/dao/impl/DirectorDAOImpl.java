package fa.training.dao.impl;

import fa.training.dao.DirectorDAO;
import fa.training.database.DBConnection;
import fa.training.entities.Director;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DirectorDAOImpl extends BaseDAOImpl<Director, Integer> implements DirectorDAO {

    private static final String SP_INSERT = "{? = call insert_director(?)}";
    private static final String SP_GET_ALL = "{call get_all_directors()}";
    private static final String SP_FIND_BY_ID = "{call find_director_by_id(?)}";
    private static final String SP_FIND_BY_NAME = "{call find_director_by_name(?)}";
    private static final String SP_UPDATE = "CALL update_director(?,?)";
    private static final String SP_DELETE = "CALL delete_director(?)";

    @Override
    protected String getGetAllSP() { return SP_GET_ALL; }

    @Override
    protected String getFindByIdSP() { return SP_FIND_BY_ID; }

    @Override
    protected String getUpdateSP() { return SP_UPDATE; }

    @Override
    protected String getDeleteSP() { return SP_DELETE; }

    @Override
    protected Director mapRow(ResultSet rs) throws SQLException {
        return new Director(rs.getInt("id"), rs.getString("name"));
    }

    @Override
    protected void setUpdateParams(CallableStatement stmt, Director entity) throws SQLException {
        stmt.setInt(1, entity.getId());
        stmt.setString(2, entity.getName());
    }

    @Override
    protected void setDeleteParam(CallableStatement stmt, Integer id) throws SQLException {
        stmt.setInt(1, id);
    }

    @Override
    protected void setFindByIdParam(CallableStatement stmt, Integer id) throws SQLException {
        stmt.setInt(1, id);
    }

    @Override
    public boolean add(Director director) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(SP_INSERT)) {
                stmt.registerOutParameter(1, Types.INTEGER);
                stmt.setString(2, director.getName());
                stmt.execute();
                director.setId(stmt.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding director: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    @Override
    public List<Director> findByName(String name) {
        var directors = new ArrayList<Director>();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(SP_FIND_BY_NAME)) {
                stmt.setString(1, name);
                try (var rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        directors.add(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding director by name: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return directors;
    }
}
