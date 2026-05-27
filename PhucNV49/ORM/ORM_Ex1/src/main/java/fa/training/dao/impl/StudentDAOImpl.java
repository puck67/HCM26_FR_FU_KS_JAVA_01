package fa.training.dao.impl;

import fa.training.dao.StudentDAO;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void saveStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void updateStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStudent(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                // Remove student associations first
                student.getCourses().clear();
                session.remove(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Student getStudentById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return java.util.Optional.ofNullable(session.get(Student.class, id))
                    .map(student -> {
                        Hibernate.initialize(student.getCourses());
                        return student;
                    })
                    .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Student> getAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Student> students = session.createQuery("from Student", Student.class).list();
            for (Student student : students) {
                Hibernate.initialize(student.getCourses());
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Student> getAllStudentsPaginated(int pageNo, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("from Student", Student.class);
            query.setFirstResult((pageNo - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<Student> students = query.list();
            for (Student student : students) {
                Hibernate.initialize(student.getCourses());
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.addCourse(course);
                session.merge(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.removeCourse(course);
                session.merge(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Student s where s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Object[]> findStudentsAndTheirCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select s, c from Student s join s.courses c", Object[].class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Student> findStudentsByNameNamedQuery(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Student> findStudentsNotEnrolled() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Student s where s.courses is empty", Student.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
