package fa.training.daoImpl;

import fa.training.config.HibernateUtil;
import fa.training.dao.StudentDAO;
import fa.training.entities.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StudentImpl implements StudentDAO {

    @Override
    public boolean saveStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
            return true;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    @Override
    public boolean updateStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student existing = session.get(Student.class, student.getId());
            if (existing == null) {
                transaction.rollback();
                return false;
            }
            existing.setName(student.getName());
            existing.setAge(student.getAge());
            session.update(existing);
            transaction.commit();
            return true;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    @Override
    public boolean deleteStudent(int studentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            if (student == null) {
                transaction.rollback();
                return false;
            }
            session.delete(student);
            transaction.commit();
            return true;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    @Override
    public Student getStudentById(int studentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            transaction.commit();
            return student;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Student> getAllStudents() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<Student> students = session.createQuery("from Student order by id", Student.class).list();
            transaction.commit();
            return students;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(ex);
        }
    }
}

