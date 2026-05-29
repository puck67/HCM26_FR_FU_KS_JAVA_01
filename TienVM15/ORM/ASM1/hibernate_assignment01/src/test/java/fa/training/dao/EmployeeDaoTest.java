package fa.training.dao;

import fa.training.dao.impl.EmployeeDaoImpl;
import fa.training.entities.Employee;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeDaoTest {

    private EmployeeDao employeeDao;

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("hibernate.config.file", "hibernate-test.cfg.xml");
        HibernateUtil.getSessionFactory();
    }

    @BeforeEach
    public void setUp() {
        employeeDao = new EmployeeDaoImpl();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM Employee").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    public void testInsertAndGetEmployeeByID() {
        Employee emp = new Employee("Alice", "Smith");
        employeeDao.insertEmployee(emp);

        assertTrue(emp.getId() > 0);

        Employee fetched = employeeDao.getEmployeeByID(emp.getId());
        assertNotNull(fetched);
        assertEquals("Alice", fetched.getFirstName());
        assertEquals("Smith", fetched.getLastName());
    }

    @Test
    public void testUpdateEmployeeByID() {
        Employee emp = new Employee("Bob", "Johnson");
        employeeDao.insertEmployee(emp);

        emp.setFirstName("Robert");
        emp.setLastName("Johnston");
        employeeDao.updateEmployeeByID(emp);

        Employee fetched = employeeDao.getEmployeeByID(emp.getId());
        assertEquals("Robert", fetched.getFirstName());
        assertEquals("Johnston", fetched.getLastName());
    }

    @Test
    public void testDeleteEmployeeById() {
        Employee emp = new Employee("Temporary", "Employee");
        employeeDao.insertEmployee(emp);

        int id = emp.getId();
        employeeDao.deleteEmployeeById(id);

        assertNull(employeeDao.getEmployeeByID(id));
    }

    @Test
    public void testGetAllEmployees() {
        employeeDao.insertEmployee(new Employee("Emp 1", "Last 1"));
        employeeDao.insertEmployee(new Employee("Emp 2", "Last 2"));

        List<Employee> list = employeeDao.getAllEmployees();
        assertEquals(2, list.size());
    }
}
