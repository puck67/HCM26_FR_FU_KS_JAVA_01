package fa.training.dao;

import fa.training.entities.Employee;
import java.util.List;

public interface EmployeeDAO {
    
    /**
     * Retrieves an Employee by its primary key ID.
     * 
     * @param id the employee ID
     * @return the Employee entity if found, otherwise null
     */
    Employee getEmployeeByID(int id);

    /**
     * Retrieves all employees from the database.
     * 
     * @return a list of all Employee entities
     */
    List<Employee> getAllEmployee();

    /**
     * Updates an existing employee in the database.
     * 
     * @param employee the Employee entity to update
     * @return true if update succeeded, false otherwise
     */
    boolean updateEmployeeByID(Employee employee);

    /**
     * Deletes an employee by their ID.
     * 
     * @param id the employee ID to delete
     * @return true if deletion succeeded, false otherwise
     */
    boolean deleteEmployeeByID(int id);

    /**
     * Inserts a new employee into the database.
     * 
     * @param employee the Employee entity to insert
     * @return true if insertion succeeded, false otherwise
     */
    boolean insertEmployee(Employee employee);
}
