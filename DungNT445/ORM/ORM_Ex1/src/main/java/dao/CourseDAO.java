package dao;

import entity.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtils;

import java.util.List;

public class CourseDAO {

    public void save(Course course){
        Transaction transaction = null;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.persist(course);

            transaction.commit();

        } catch (Exception e){
            if(transaction!=null){
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    public Course findById(int id){
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            return session.find(Course.class, id);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public List<Course> findAll(){
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            return session.createQuery("from Course", Course.class).list();

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void update(Course student){
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
            Course student = session.find(Course.class, id);

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

    public Course findByIdWithStudents(int id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Course course = session.find(Course.class, id);
            if (course != null) {
                org.hibernate.Hibernate.initialize(course.getStudents());
            }
            return course;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Task 5.4: Criteria API - Find courses that have credit greater than a given value
    public List<Course> findCoursesWithCreditGreaterThan(int creditValue) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            var builder = session.getCriteriaBuilder();
            var query = builder.createQuery(Course.class);
            var root = query.from(Course.class);
            query.select(root).where(builder.gt(root.get("credit"), creditValue));
            return session.createQuery(query).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Task 5.5: Aggregation Query - Count how many students are enrolled in each course
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery(
                "select c.title, count(s) from Course c left join c.students s group by c.id, c.title", 
                Object[].class
            ).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
