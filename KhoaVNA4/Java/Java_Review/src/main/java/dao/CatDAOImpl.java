package dao;

import database.DBConnection;
import entities.Cat;
import entities.Owner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CatDAOImpl extends BaseDAOImpl<Cat> implements CatDAO {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public List<Cat> getAllCat() {
        return findAll();
    }

    @Override
    public boolean add(Cat cat) {
        String birthDateStr = dateFormat.format(cat.getBirthDay());
        return DBConnection.executeProcedureUpdate("{call addCat(?, ?, ?, ?)}",
                cat.getId(),
                cat.getName(),
                birthDateStr,
                cat.getOwner().getId());
    }

    @Override
    public boolean update(Cat cat) {
        String birthDateStr = dateFormat.format(cat.getBirthDay());
        return DBConnection.executeProcedureUpdate("{call updateCat(?, ?, ?, ?)}",
                cat.getId(),
                cat.getName(),
                birthDateStr,
                cat.getOwner().getId());
    }

    @Override
    public boolean delete(Object id) {
        return DBConnection.executeProcedureUpdate("{call deleteCat(?)}", id.toString());
    }

    @Override
    public Cat findById(Object id) {
        List<Cat> list = DBConnection.executeProcedureQuery("{call getCatById(?)}", defaultMapper, id.toString());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Cat> findAll() {
        return DBConnection.executeProcedureQuery("{call getAllCats()}", defaultMapper);
    }

    @Override
    protected Cat mapResultSetToEntity(ResultSet rs) throws SQLException {
        try {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String birthDateStr = rs.getString("birth_date");
            String ownerId = rs.getString("owner_id");

            java.util.Date birthDay = null;
            if (birthDateStr != null) {
                birthDay = dateFormat.parse(birthDateStr);
            }

            Owner owner = null;
            if (ownerId != null) {
                OwnerDAO ownerDAO = new OwnerDAOImpl();
                owner = ownerDAO.findById(ownerId);
            }

            return new Cat(id, name, birthDay, owner);
        } catch (Exception e) {
            throw new SQLException("Failed to map ResultSet to Cat: " + e.getMessage(), e);
        }
    }
}
