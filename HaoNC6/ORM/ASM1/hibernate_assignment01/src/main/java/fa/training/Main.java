package fa.training;

import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Employee employee = new Employee("Nguyen", "Van A");
            session.save(employee);
            transaction.commit();
            System.out.println("Lưu nhân viên thành công vào cơ sở dữ liệu!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtils.shutdown();
        }
    }
}