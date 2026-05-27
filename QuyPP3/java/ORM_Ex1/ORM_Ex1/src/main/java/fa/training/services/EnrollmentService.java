package fa.training.services;

import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.utils.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EnrollmentService {

    // Enroll a student in a course
    public void enrollStudentInCourse(Long studentId, Long courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student != null && course != null) {
                // Using helper method addCourse defined in Student entity
                student.addCourse(course);
                session.merge(student);
                System.out.println(
                        "Successfully Add Student '" + student.getName() + "' into Course: " + course.getTitle());
            } else {
                System.out.println("Error: Not Found Student or Course!");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }

    // Remove a student from a course
    public void removeStudentFromCourse(Long studentId, Long courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student != null && course != null) {
                // Using helper method removeCourse defined in Student entity
                student.removeCourse(course);
                session.merge(student);
                System.out.println(
                        "Successfully Remove Student '" + student.getName() + "' from Course: " + course.getTitle());
            } else {
                System.out.println("Error: Not Found Student or Course!");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }

    // Display all courses of a given student
    public void displayCoursesOfStudent(Long studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Force to load courses data from DB (because default is Lazy Load)
                Hibernate.initialize(student.getCourses());
                System.out.println("--- List of Courses of Student: " + student.getName() + " ---");
                if (student.getCourses().isEmpty()) {
                    System.out.println("No courses registered yet.");
                } else {
                    for (Course course : student.getCourses()) {
                        System.out.println("- " + course.getTitle() + " (" + course.getCredit() + " credits)");
                    }
                }
            } else {
                System.out.println("Error: Not Found Student with ID " + studentId);
            }
        }
    }

    // Display all students of a given course
    public void displayStudentsOfCourse(Long courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                // Force to load students data from DB (because default is Lazy Load)
                Hibernate.initialize(course.getStudents());
                System.out.println("--- List of Students of Course: " + course.getTitle() + " ---");
                if (course.getStudents().isEmpty()) {
                    System.out.println("No students registered yet.");
                } else {
                    for (Student student : course.getStudents()) {
                        System.out.println("- " + student.getName() + " (Age: " + student.getAge() + ")");
                    }
                }
            } else {
                System.out.println("Error: Not Found Course with ID " + courseId);
            }
        }
    }
}
