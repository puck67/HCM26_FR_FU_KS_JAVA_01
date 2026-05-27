package dao;

import database.DBConnection;
import entities.Owner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OwnerDAOImpl extends BaseDAOImpl<Owner> implements OwnerDAO {

    @Override
    public boolean add(Owner owner) {
        return DBConnection.executeProcedureUpdate("{call addOwner(?, ?, ?, ?)}",
                owner.getId(),
                owner.getName(),
                owner.getEmail(),
                owner.getPhone());
    }

    @Override
    public boolean update(Owner owner) {
        return DBConnection.executeProcedureUpdate("{call updateOwner(?, ?, ?, ?)}",
                owner.getId(),
                owner.getName(),
                owner.getEmail(),
                owner.getPhone());
    }

    @Override
    public boolean delete(Object id) {
        return DBConnection.executeProcedureUpdate("{call deleteOwner(?)}", id.toString());
    }

    @Override
    public Owner findById(Object id) {
        List<Owner> list = DBConnection.executeProcedureQuery("{call getOwnerById(?)}", defaultMapper, id.toString());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Owner> findAll() {
        return DBConnection.executeProcedureQuery("{call getAllOwners()}", defaultMapper);
    }

    @Override
    protected Owner mapResultSetToEntity(ResultSet rs) throws SQLException {
        try {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String phone = rs.getString("phone");
            return new Owner(id, name, email, phone);
        } catch (Exception e) {
            throw new SQLException("Failed to map ResultSet to Owner: " + e.getMessage(), e);
        }
    }
}
