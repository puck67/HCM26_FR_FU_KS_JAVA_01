package fa.training.daoImpl;

import fa.training.config.HibernateUtil;
import fa.training.dao.CourseDAO;
import fa.training.entities.Course;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CourseImpl implements CourseDAO {

	@Override
	public boolean saveCourse(Course course) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(course);
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
	public boolean updateCourse(Course course) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Course existing = session.get(Course.class, course.getId());
			if (existing == null) {
				transaction.rollback();
				return false;
			}
			existing.setTitle(course.getTitle());
			existing.setCredit(course.getCredit());
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
	public boolean deleteCourse(int courseId) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Course course = session.get(Course.class, courseId);
			if (course == null) {
				transaction.rollback();
				return false;
			}
			session.delete(course);
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
	public List<Course> getAllCourses() {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			List<Course> courses = session.createQuery("from Course order by id", Course.class).list();
			transaction.commit();
			return courses;
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(ex);
		}
	}
}
