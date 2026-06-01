package fa.training.dao.impl;

import fa.training.dao.EmployeeDao;
import fa.training.entities.Employee;
import java.util.List;

public class EmployeeDaoImpl extends GenericDAOImpl<Employee, Integer> implements EmployeeDao {

    public EmployeeDaoImpl() {
        super(Employee.class);
    }

    @Override
    public Employee getEmployeeByID(int id) {
        return findById(id);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return findAll();
    }

    @Override
    public void updateEmployeeByID(Employee employee) {
        update(employee);
    }

    @Override
    public void deleteEmployeeById(int id) {
        delete(id);
    }

    @Override
    public void insertEmployee(Employee employee) {
        save(employee);
    }
}
