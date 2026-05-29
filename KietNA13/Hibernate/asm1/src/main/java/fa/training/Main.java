package fa.training;

import fa.training.dao.EmployeeDAO;
import fa.training.entities.Employee;
import fa.training.utils.HibernateUtil;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO();

        Employee emp = new Employee("Nguyen Anh", "Kiet");
        dao.save(emp);

        List<Employee> employees = dao.findAll();
        employees.stream()
                .map(Employee::toString)
                .forEach(System.out::println);

        HibernateUtil.shutdown();
    }
}
