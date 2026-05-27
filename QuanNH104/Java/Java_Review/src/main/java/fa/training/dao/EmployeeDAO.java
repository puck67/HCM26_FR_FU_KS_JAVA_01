package fa.training.dao;

import fa.training.model.Employee;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO {
    boolean add(Employee employee) throws SQLException;
    List<Employee> getByWarehouseId(String warehouseId) throws SQLException;
    Employee findById(String id) throws SQLException;
    boolean isEmailExists(String email) throws SQLException;
}
