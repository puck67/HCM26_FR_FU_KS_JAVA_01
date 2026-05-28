package com.example.dao;

import com.example.dao.impl.CourseDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CourseDAOImpl using an isolated H2 in-memory database.
 * Each test starts with a clean slate via @BeforeEach.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseDAOTest {

    private CourseDAO courseDAO;

    @BeforeAll
    public static void setUpClass() {
        // Point HibernateUtil to the test H2 in-memory configuration
        System.setProperty("hibernate.config.file", "hibernate-test.cfg.xml");
        HibernateUtil.getSessionFactory(); // Initialize SessionFactory
    }

    @BeforeEach
    public void setUp() {
        courseDAO = new CourseDAOImpl();
        // Clear all data before each test for isolation
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM Student").executeUpdate();
            session.createMutationQuery("DELETE FROM Course").executeUpdate();
            session.getTransaction().commit();
        }
    }



    // -------------------------------------------------------------------------
    // CRUD Tests
    // -------------------------------------------------------------------------

    @Test
    public void testSaveAndFindById() {
        Course course = new Course("Java Programming", 4);
        courseDAO.save(course);

        assertTrue(course.getId() > 0, "Course ID should be generated after save");

        Course found = courseDAO.findById(course.getId());
        assertNotNull(found, "Should find course by ID");
        assertEquals("Java Programming", found.getTitle());
        assertEquals(4, found.getCredit());
    }

    @Test
    public void testUpdateCourse() {
        Course course = new Course("Old Title", 2);
        courseDAO.save(course);

        course.setTitle("New Title");
        course.setCredit(5);
        courseDAO.update(course);

        Course updated = courseDAO.findById(course.getId());
        assertNotNull(updated);
        assertEquals("New Title", updated.getTitle());
        assertEquals(5, updated.getCredit());
    }

    @Test
    public void testDeleteCourseAndDissociation() {
        Student student = new Student("Alice", 20);
        Course course = new Course("Physics", 3);

        // Persist both
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(student);
            session.persist(course);
            session.getTransaction().commit();
        }

        // Enroll student in course
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Student s = session.get(Student.class, student.getId());
            Course c = session.get(Course.class, course.getId());
            s.setCourses(new HashSet<>(List.of(c)));
            c.setStudents(new HashSet<>(List.of(s)));
            session.merge(s);
            session.getTransaction().commit();
        }

        // Verify enrollment
        Course enrolled = courseDAO.findById(course.getId());
        assertEquals(1, enrolled.getStudents().size(), "Course should have 1 student enrolled");

        // Delete course — should dissociate without FK error
        courseDAO.delete(course.getId());

        // Verify deletion
        assertNull(courseDAO.findById(course.getId()), "Course should be deleted");
    }

    @Test
    public void testFindAll() {
        courseDAO.save(new Course("Java", 4));
        courseDAO.save(new Course("Database", 3));
        courseDAO.save(new Course("Web Dev", 2));

        List<Course> all = courseDAO.findAll();
        assertEquals(3, all.size(), "Should find all 3 courses");
    }

    // -------------------------------------------------------------------------
    // Query Tests
    // -------------------------------------------------------------------------

    @Test
    public void testFindWithCreditGreaterThan() {
        courseDAO.save(new Course("Low Credit", 1));
        courseDAO.save(new Course("Mid Credit", 3));
        courseDAO.save(new Course("High Credit", 5));

        List<Course> results = courseDAO.findWithCreditGreaterThan(2);
        assertEquals(2, results.size(), "Should find 2 courses with credit > 2");
        assertTrue(results.stream().anyMatch(c -> c.getTitle().equals("Mid Credit")));
        assertTrue(results.stream().anyMatch(c -> c.getTitle().equals("High Credit")));
    }

    @Test
    public void testGetStudentCountPerCourse() {
        Course c1 = new Course("Java", 4);
        Course c2 = new Course("Math", 3);
        Student s1 = new Student("Alice", 20);
        Student s2 = new Student("Bob", 22);

        // Persist all
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(c1);
            session.persist(c2);
            session.persist(s1);
            session.persist(s2);
            session.getTransaction().commit();
        }

        // Enroll s1 in both courses, s2 in only c1
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Student sa = session.get(Student.class, s1.getId());
            Student sb = session.get(Student.class, s2.getId());
            Course ca = session.get(Course.class, c1.getId());
            Course cb = session.get(Course.class, c2.getId());
            sa.addCourse(ca);
            sa.addCourse(cb);
            sb.addCourse(ca);
            session.merge(sa);
            session.merge(sb);
            session.getTransaction().commit();
        }

        Map<String, Long> counts = courseDAO.getStudentCountPerCourse();
        assertFalse(counts.isEmpty(), "Count map should not be empty");
        assertEquals(2L, counts.get("Java"), "Java course should have 2 students");
        assertEquals(1L, counts.get("Math"), "Math course should have 1 student");
    }
}
