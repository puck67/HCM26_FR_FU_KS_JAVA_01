package dao;

import entity.Student;
import entity.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtils;

import java.util.List;

public class StudentDAO {

   public void save(Student student){
       Transaction transaction = null;
       try(Session session = HibernateUtils.getSessionFactory().openSession()){
           transaction = session.beginTransaction();
           session.persist(student);

           transaction.commit();

       } catch (Exception e){
           if(transaction!=null){
               transaction.rollback();
           }
           e.printStackTrace();
       }
   }


   public Student findById(int id){
       try(Session session = HibernateUtils.getSessionFactory().openSession()){
           return session.find(Student.class, id);
       } catch (Exception e){
           e.printStackTrace();
           return null;
       }
   }


   public List<Student> findAll(){
       try(Session session = HibernateUtils.getSessionFactory().openSession()){
           return session.createQuery("from Student", Student.class).list();

       } catch (Exception e){
           e.printStackTrace();
            return null;
       }
   }

   public void update(Student student){
       Transaction transaction = null;
       try(Session  session = HibernateUtils.getSessionFactory().openSession()) {
           transaction = session.beginTransaction();
           session.merge(student);

           transaction.commit();
       } catch (Exception e){
           if(transaction!=null){
               transaction.rollback();
           }
           e.printStackTrace();
       }
   }

   public void delete(int id){
        Transaction transaction = null;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Student student = session.find(Student.class, id);

            if(student!=null){
                session.remove(student);
                System.out.println("[DAO] Đã xóa thành công ID: " + id);
            } else {
                System.out.println("[DAO] Không tìm thấy ID " + id + " để xóa.");
            }
            transaction.commit();
        } catch (Exception e){
            if(transaction!=null){
                transaction.rollback();
            } e.printStackTrace();
        }
   }

    public Student findByIdWithCourses(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Student student = session.find(Student.class, id);
            if (student != null) {
                org.hibernate.Hibernate.initialize(student.getCourses());
            }
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void enrollStudentInCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Student student = session.find(Student.class, studentId);
            Course course = session.find(Course.class, courseId);

            if (student == null) {
                throw new IllegalArgumentException("Không tìm thấy sinh viên với ID: " + studentId);
            }
            if (course == null) {
                throw new IllegalArgumentException("Không tìm thấy khóa học với ID: " + courseId);
            }

            boolean alreadyEnrolled = student.getCourses().stream()
                    .anyMatch(c -> c.getId().equals(courseId));
            if (alreadyEnrolled) {
                throw new IllegalStateException("Sinh viên đã tham gia khóa học này từ trước.");
            }

            student.getCourses().add(course);
            session.merge(student);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void unenrollStudentFromCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Student student = session.find(Student.class, studentId);
            Course course = session.find(Course.class, courseId);

            if (student == null) {
                throw new IllegalArgumentException("Không tìm thấy sinh viên với ID: " + studentId);
            }
            if (course == null) {
                throw new IllegalArgumentException("Không tìm thấy khóa học với ID: " + courseId);
            }

            boolean removed = student.getCourses().removeIf(c -> c.getId().equals(courseId));
            if (!removed) {
                throw new IllegalStateException("Sinh viên chưa tham gia khóa học này.");
            }

            session.merge(student);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Task 5.1: Find all students older than a given age using HQL Query
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Student s where s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Task 5.2: HQL with Join - List students and the courses they are enrolled in
    public List<Student> findAllStudentsWithCourses() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("select distinct s from Student s left join fetch s.courses", Student.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Task 5.3: Named Query - Find students by name
    public List<Student> findByNameNamedQuery(String name) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
