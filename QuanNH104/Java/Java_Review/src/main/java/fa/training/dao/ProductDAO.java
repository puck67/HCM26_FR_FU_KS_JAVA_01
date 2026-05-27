package fa.training.dao;

import fa.training.model.Product;
import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean add(Product product) throws SQLException;
    List<Product> getByWarehouseId(String warehouseId) throws SQLException;
    Product findById(String id) throws SQLException;
    int getTotalQuantityByWarehouseId(String warehouseId) throws SQLException;
}
