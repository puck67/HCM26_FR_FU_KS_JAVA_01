package com.example.dao;

import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StudentDAOImpl using an isolated H2 in-memory database.
 * Each test starts with a clean slate by deleting all rows in @BeforeEach.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentDAOTest {

    private StudentDAO studentDAO;

    @BeforeAll
    public static void setUpClass() {
        // Point HibernateUtil to the test H2 in-memory configuration
        System.setProperty("hibernate.config.file", "hibernate-test.cfg.xml");
        HibernateUtil.getSessionFactory(); // Initialize SessionFactory
    }

    @BeforeEach
    public void setUp() {
        studentDAO = new StudentDAOImpl();
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
        Student student = new Student("Alice", 20);
        studentDAO.save(student);

        assertTrue(student.getId() > 0, "Student ID should be generated after save");

        Student found = studentDAO.findById(student.getId());
        assertNotNull(found, "Should find student by ID");
        assertEquals("Alice", found.getName());
        assertEquals(20, found.getAge());
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student("Bob", 22);
        studentDAO.save(student);

        student.setName("Bobby");
        student.setAge(23);
        studentDAO.update(student);

        Student updated = studentDAO.findById(student.getId());
        assertNotNull(updated);
        assertEquals("Bobby", updated.getName());
        assertEquals(23, updated.getAge());
    }

    @Test
    public void testDeleteStudentAndDissociation() {
        Student student = new Student("Charlie", 21);
        Course course = new Course("Math", 3);

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
        Student enrolled = studentDAO.findById(student.getId());
        assertEquals(1, enrolled.getCourses().size(), "Student should be enrolled in 1 course");

        // Delete student
        studentDAO.delete(student.getId());

        // Verify deletion
        assertNull(studentDAO.findById(student.getId()), "Student should be deleted");
    }

    @Test
    public void testFindAll() {
        studentDAO.save(new Student("Alice", 20));
        studentDAO.save(new Student("Bob", 22));
        studentDAO.save(new Student("Charlie", 21));

        List<Student> all = studentDAO.findAll();
        assertEquals(3, all.size(), "Should find all 3 students");
    }

    // -------------------------------------------------------------------------
    // Query Tests
    // -------------------------------------------------------------------------

    @Test
    public void testFindByNameNamedQuery() {
        studentDAO.save(new Student("Alex Mercer", 25));
        studentDAO.save(new Student("Alexander Great", 30));
        studentDAO.save(new Student("John Doe", 22));

        List<Student> results = studentDAO.findByName("Alex");
        assertEquals(2, results.size(), "Should find 2 students matching 'Alex'");
        assertTrue(results.stream().anyMatch(s -> s.getName().equals("Alex Mercer")));
        assertTrue(results.stream().anyMatch(s -> s.getName().equals("Alexander Great")));
    }

    @Test
    public void testFindOlderThan() {
        studentDAO.save(new Student("Young", 18));
        studentDAO.save(new Student("Mid", 22));
        studentDAO.save(new Student("Old", 30));

        List<Student> results = studentDAO.findOlderThan(20);
        assertEquals(2, results.size(), "Should find 2 students older than 20");
    }

    @Test
    public void testFindUnenrolledStudents() {
        Student enrolled = new Student("Enrolled Stu", 20);
        Student unenrolled = new Student("Free Stu", 21);
        Course course = new Course("Physics", 4);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(enrolled);
            session.persist(unenrolled);
            session.persist(course);
            session.getTransaction().commit();
        }

        // Enroll the first student
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Student s = session.get(Student.class, enrolled.getId());
            Course c = session.get(Course.class, course.getId());
            s.setCourses(new HashSet<>(List.of(c)));
            c.setStudents(new HashSet<>(List.of(s)));
            session.merge(s);
            session.getTransaction().commit();
        }

        List<Student> freeStudents = studentDAO.findUnenrolledStudents();
        assertEquals(1, freeStudents.size(), "Should find 1 unenrolled student");
        assertEquals("Free Stu", freeStudents.get(0).getName());
    }

    @Test
    public void testFindAllPaginated() {
        for (int i = 1; i <= 5; i++) {
            studentDAO.save(new Student("Student " + i, 18 + i));
        }

        List<Student> page1 = studentDAO.findAllPaginated(0, 3);
        List<Student> page2 = studentDAO.findAllPaginated(3, 3);

        assertEquals(3, page1.size(), "Page 1 should have 3 students");
        assertEquals(2, page2.size(), "Page 2 should have 2 students");
    }
}
