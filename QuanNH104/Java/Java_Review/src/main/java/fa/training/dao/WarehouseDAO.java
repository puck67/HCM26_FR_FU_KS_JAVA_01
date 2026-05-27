package fa.training.dao;

import fa.training.model.Warehouse;
import java.sql.SQLException;
import java.util.List;

public interface WarehouseDAO {
    boolean add(Warehouse warehouse) throws SQLException;
    List<Warehouse> getAll() throws SQLException;
    boolean update(Warehouse warehouse) throws SQLException;
    boolean delete(String id) throws SQLException;
    Warehouse findById(String id) throws SQLException;
}
