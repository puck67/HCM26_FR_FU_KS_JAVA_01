package fa.training.dao.impl;

import fa.training.dao.EmployeeDAO;
import fa.training.dao.ProductDAO;
import fa.training.dao.WarehouseDAO;
import fa.training.db.DBConnection;
import fa.training.model.Warehouse;
import fa.training.validation.Validator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarehouseDAOImpl implements WarehouseDAO {

    private final EmployeeDAO employeeDAO = new EmployeeDAOImpl();
    private final ProductDAO productDAO = new ProductDAOImpl();

    @Override
    public boolean add(Warehouse warehouse) throws SQLException {
        // Validate input data
        Validator.validateWarehouse(warehouse.getId(), warehouse.getName(), warehouse.getAddress(), warehouse.getCapacity());

        // Check if ID already exists
        if (findById(warehouse.getId()) != null) {
            throw new IllegalArgumentException("Warehouse ID '" + warehouse.getId() + "' already exists.");
        }

        String sql = "{call insert_warehouse(?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, warehouse.getId());
            stmt.setString(2, warehouse.getName());
            stmt.setString(3, warehouse.getAddress());
            stmt.setInt(4, warehouse.getCapacity());
            
            stmt.execute();
            return true;
        }
    }

    @Override
    public List<Warehouse> getAll() throws SQLException {
        List<Warehouse> list = new ArrayList<>();
        String sql = "{call get_all_warehouses()}";
        
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                var id = rs.getString("id");
                var name = rs.getString("name");
                var address = rs.getString("address");
                var capacity = rs.getInt("capacity");
                
                var warehouse = new Warehouse(id, name, address, capacity);
                
                // Load linked objects
                warehouse.setEmployees(employeeDAO.getByWarehouseId(id));
                warehouse.setProducts(productDAO.getByWarehouseId(id));
                
                list.add(warehouse);
            }
        }
        return list;
    }

    @Override
    public boolean update(Warehouse warehouse) throws SQLException {
        // Validate input data
        Validator.validateWarehouse(warehouse.getId(), warehouse.getName(), warehouse.getAddress(), warehouse.getCapacity());

        // Check if warehouse exists
        var existing = findById(warehouse.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Warehouse ID '" + warehouse.getId() + "' does not exist.");
        }

        // Check if the new capacity is less than the current total quantity of products
        int currentQty = productDAO.getTotalQuantityByWarehouseId(warehouse.getId());
        if (warehouse.getCapacity() < currentQty) {
            throw new IllegalArgumentException("Cannot decrease capacity to " + warehouse.getCapacity() 
                    + " because the warehouse currently contains " + currentQty + " products.");
        }

        String sql = "{call update_warehouse(?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, warehouse.getId());
            stmt.setString(2, warehouse.getName());
            stmt.setString(3, warehouse.getAddress());
            stmt.setInt(4, warehouse.getCapacity());
            
            stmt.execute();
            return true;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Warehouse ID to delete cannot be empty.");
        }

        var existing = findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Warehouse ID '" + id + "' does not exist.");
        }

        String sql = "{call delete_warehouse(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, id);
            stmt.execute();
            return true;
        }
    }

    @Override
    public Warehouse findById(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        String sql = "{call find_warehouse_by_id(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    var whId = rs.getString("id");
                    var name = rs.getString("name");
                    var address = rs.getString("address");
                    var capacity = rs.getInt("capacity");
                    
                    var warehouse = new Warehouse(whId, name, address, capacity);
                    
                    // Load linked objects
                    warehouse.setEmployees(employeeDAO.getByWarehouseId(whId));
                    warehouse.setProducts(productDAO.getByWarehouseId(whId));
                    
                    return warehouse;
                }
            }
        }
        return null;
    }
}
