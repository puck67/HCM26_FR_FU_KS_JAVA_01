package com.training;

import com.training.entity.Course;
import com.training.entity.Student;
import com.training.service.TrainingService;
import com.training.util.HibernateUtil;

import java.util.List;

public class MainApplication {
    public static void main(String[] args) {
        TrainingService service = new TrainingService();

        System.out.println("--- Bootstrapping Sample Data ---");
        Student s1 = new Student("John Doe", 21);
        Student s2 = new Student("Anna Smith", 22);
        Student s3 = new Student("Bob Johnson", 19);
        Student s4 = new Student("Charlie Brown", 25);

        service.saveStudent(s1);
        service.saveStudent(s2);
        service.saveStudent(s3);
        service.saveStudent(s4);

        Course c1 = new Course("Java Programming", 4);
        Course c2 = new Course("Database Systems", 3);
        Course c3 = new Course("Web Development", 2);

        service.saveCourse(c1);
        service.saveCourse(c2);
        service.saveCourse(c3);

        System.out.println("--- Enrolling Students ---");
        service.enrollStudentInCourse(s1.getId(), c1.getId());
        service.enrollStudentInCourse(s1.getId(), c2.getId());
        service.enrollStudentInCourse(s2.getId(), c1.getId());
        service.enrollStudentInCourse(s2.getId(), c2.getId());
        service.enrollStudentInCourse(s2.getId(), c3.getId());
        service.enrollStudentInCourse(s3.getId(), c2.getId());

        System.out.println("\n=== Task 3 & 4 Verification ===");
        // Test Read Specific Student
        System.out.println("Fetched Student ID 1: " + service.getStudentById(s1.getId()));

        // Test Update
        s1.setAge(22);
        service.updateStudent(s1);
        System.out.println("Updated Student 1 Age: " + service.getStudentById(s1.getId()).getAge());

        // Test display logic
        System.out.println("\nCourses of Student John Doe:");
        service.getCoursesByStudent(s1.getId()).forEach(c -> System.out.println(" - " + c.getTitle()));

        System.out.println("\nStudents in Database Systems:");
        service.getStudentsByCourse(c2.getId()).forEach(s -> System.out.println(" - " + s.getName()));

        System.out.println("\n=== Task 5 Query Practice ===");

        // 1. HQL: Students older than 20
        System.out.println("\nStudents older than 20:");
        service.findStudentsOlderThan(20).forEach(System.out::println);

        // 2. HQL with Join
        System.out.println("\nList of students and their courses (Eager Fetch):");
        for (Student student : service.getStudentsWithCourses()) {
            System.out.print(student.getName() + " is enrolled in: ");
            if(student.getCourses().isEmpty()) System.out.print("No Courses");
            student.getCourses().forEach(c -> System.out.print(c.getTitle() + " | "));
            System.out.println();
        }

        // 3. Named Query
        System.out.println("\nNamed Query: Finding student named 'Anna Smith':");
        service.findStudentsByName("Anna Smith").forEach(System.out::println);

        // 4. Criteria API
        System.out.println("\nCriteria API: Courses with credits > 2:");
        service.findCoursesWithCreditGreaterThan(2).forEach(System.out::println);

        // 5. Aggregation Query
        System.out.println("\nStudent count per course:");
        List<Object[]> counts = service.getStudentCountPerCourse();
        for (Object[] row : counts) {
            System.out.println(row[0] + ": " + row[1]);
        }

        System.out.println("\n=== Bonus Implementations ===");

        // Bonus 1: Pagination Example (Offset 0, Limit 2)
        System.out.println("\nPaginated Student List (Page 1 - size 2):");
        service.getAllStudents(0, 2).forEach(System.out::println);

        // Bonus 2: Unenrolled Students
        System.out.println("\nStudents not enrolled in any course:");
        service.findUnenrolledStudents().forEach(System.out::println);

        // Test Delete Operations Cleanliness
        System.out.println("\n--- Testing Safe Deletions ---");
        service.deleteStudent(s1.getId());
        System.out.println("Total Students remaining after deleting John: " + service.getAllStudents(null, null).size());

        // Cleanup resources
        HibernateUtil.shutdown();
    }
}