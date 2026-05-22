package fa.training.dao.impl;

import fa.training.dao.EmployeeDAO;
import fa.training.db.DBConnection;
import fa.training.model.Employee;
import fa.training.validation.Validator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public boolean add(Employee employee) throws SQLException {
        // Validate input data
        Validator.validateEmployee(employee.getId(), employee.getName(), employee.getEmail(), employee.getPhone());

        // Check if employee ID already exists
        if (findById(employee.getId()) != null) {
            throw new IllegalArgumentException("Employee ID '" + employee.getId() + "' already exists.");
        }

        // Check if email already exists
        if (isEmailExists(employee.getEmail())) {
            throw new IllegalArgumentException("Employee email '" + employee.getEmail() + "' is already in use.");
        }

        String sql = "{call insert_employee(?, ?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, employee.getId());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getWarehouseId());
            
            stmt.execute();
            return true;
        }
    }

    @Override
    public List<Employee> getByWarehouseId(String warehouseId) throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "{call get_employees_by_warehouse(?)}";
        
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, warehouseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    var emp = new Employee(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("warehouse_id")
                    );
                    list.add(emp);
                }
            }
        }
        return list;
    }

    @Override
    public Employee findById(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("warehouse_id")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
