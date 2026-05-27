package com.practice.repository;

import com.practice.config.HibernateUtil;
import com.practice.entity.Course;
import com.practice.entity.Student;
import com.practice.repository.impl.StudentRepositoryImpl;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentRepositoryTest {

    private StudentRepository studentRepository;

    @BeforeAll
    public static void setUpClass() {
        // Force HibernateUtil to load the test H2 configuration
        System.setProperty("hibernate.config.file", "hibernate-test.cfg.xml");
        // Initialize SessionFactory
        HibernateUtil.getSessionFactory();
    }

    @BeforeEach
    public void setUp() {
        studentRepository = new StudentRepositoryImpl();
        // Clear database before each test
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM Student").executeUpdate();
            session.createMutationQuery("DELETE FROM Course").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @AfterAll
    public static void tearDownClass() {
        HibernateUtil.shutdown();
    }

    @Test
    public void testSaveAndFindById() {
        Student student = new Student("Alice", 20);
        studentRepository.save(student);

        assertNotNull(student.getId(), "Student ID should be generated");

        Student found = studentRepository.findById(student.getId());
        assertNotNull(found);
        assertEquals("Alice", found.getName());
        assertEquals(20, found.getAge());
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student("Bob", 22);
        studentRepository.save(student);

        student.setName("Bobby");
        student.setAge(23);
        studentRepository.update(student);

        Student updated = studentRepository.findById(student.getId());
        assertNotNull(updated);
        assertEquals("Bobby", updated.getName());
        assertEquals(23, updated.getAge());
    }

    @Test
    public void testDeleteStudentAndDissociation() {
        // Create student and course
        Student student = new Student("Charlie", 21);
        Course course = new Course("Math", 3);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(student);
            session.persist(course);
            session.getTransaction().commit();
        }

        // Enroll
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Student s = session.get(Student.class, student.getId());
            Course c = session.get(Course.class, course.getId());
            s.setCourses(new HashSet<>(List.of(c)));
            c.setStudents(new HashSet<>(List.of(s)));
            session.merge(s);
            session.getTransaction().commit();
        }

        // Verify enrollment in DB
        Student enrolledStudent = studentRepository.findById(student.getId());
        assertEquals(1, enrolledStudent.getCourses().size());

        // Delete Student
        studentRepository.delete(student.getId());

        // Verify Student is deleted
        assertNull(studentRepository.findById(student.getId()));
    }

    @Test
    public void testFindByNameNamedQuery() {
        Student s1 = new Student("Alex Mercer", 25);
        Student s2 = new Student("Alexander Great", 30);
        Student s3 = new Student("John Doe", 22);

        studentRepository.save(s1);
        studentRepository.save(s2);
        studentRepository.save(s3);

        List<Student> results = studentRepository.findByName("Alex");
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(s -> s.getName().equals("Alex Mercer")));
        assertTrue(results.stream().anyMatch(s -> s.getName().equals("Alexander Great")));
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

        // Enroll enrolled student
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Student s = session.get(Student.class, enrolled.getId());
            Course c = session.get(Course.class, course.getId());
            s.setCourses(new HashSet<>(List.of(c)));
            c.setStudents(new HashSet<>(List.of(s)));
            session.merge(s);
            session.getTransaction().commit();
        }

        List<Student> freeStudents = studentRepository.findUnenrolledStudents();
        assertEquals(1, freeStudents.size());
        assertEquals("Free Stu", freeStudents.get(0).getName());
    }
}
