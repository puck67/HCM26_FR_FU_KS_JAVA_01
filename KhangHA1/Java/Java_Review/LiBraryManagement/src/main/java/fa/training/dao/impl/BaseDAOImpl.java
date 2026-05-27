package fa.training.dao.impl;

import fa.training.dao.BaseDAO;
import fa.training.database.DBConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAOImpl<T, ID> implements BaseDAO<T, ID> {

    protected abstract String getInsertSQL();
    protected abstract String getGetAllSQL();
    protected abstract String getFindByIdSQL();
    protected abstract String getUpdateSQL();
    protected abstract String getDeleteSQL();

    protected abstract T mapRow(ResultSet rs) throws SQLException;
    protected abstract void setInsertParams(CallableStatement stmt, T entity) throws SQLException;
    protected abstract void setUpdateParams(CallableStatement stmt, T entity) throws SQLException;
    protected abstract void setDeleteParams(CallableStatement stmt, ID id) throws SQLException;
    protected abstract void setFindByIdParams(PreparedStatement stmt, ID id) throws SQLException;

    @Override
    public boolean add(T entity) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(getInsertSQL())) {
                setInsertParams(stmt, entity);
                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    @Override
    public List<T> getAll() {
        var list = new ArrayList<T>();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(getGetAllSQL());
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return list;
    }

    @Override
    public T findById(ID id) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(getFindByIdSQL())) {
                setFindByIdParams(stmt, id);
                try (var rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding by id: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public boolean update(T entity) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(getUpdateSQL())) {
                setUpdateParams(stmt, entity);
                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    @Override
    public boolean delete(ID id) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(getDeleteSQL())) {
                setDeleteParams(stmt, id);
                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
