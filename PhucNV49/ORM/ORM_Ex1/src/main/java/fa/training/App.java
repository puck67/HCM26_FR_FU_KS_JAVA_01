package fa.training;

import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.dao.impl.StudentDAOImpl;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    private static final StudentDAO studentDAO = new StudentDAOImpl();
    private static final CourseDAO courseDAO = new CourseDAOImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Clear database on startup for clean slate
        clearDatabase();

        // Main menu actions defined using Java 8 Lambdas
        Map<Integer, Runnable> mainMenuActions = new HashMap<>();
        mainMenuActions.put(1, App::runAutoDemo);
        mainMenuActions.put(2, App::studentMenu);
        mainMenuActions.put(3, App::courseMenu);
        mainMenuActions.put(4, App::enrollmentMenu);
        mainMenuActions.put(5, App::queriesMenu);
        mainMenuActions.put(6, () -> {
            clearDatabase();
            System.out.println("Database cleared successfully!");
        });
        mainMenuActions.put(7, () -> {
            System.out.println("\nShutting down Hibernate...");
            HibernateUtil.shutdown();
            System.out.println("Goodbye!");
        });

        while (true) {
            System.out.println("\n==================================================");
            System.out.println("       HIBERNATE CRUD & QUERY APPLICATION         ");
            System.out.println("==================================================");
            System.out.println("1. Run Automated Test Demo (Required Assignment Output)");
            System.out.println("2. Student Management (CRUD)");
            System.out.println("3. Course Management (CRUD)");
            System.out.println("4. Enrollment Management");
            System.out.println("5. Hibernate Queries (Task 5 & Bonus)");
            System.out.println("6. Reset / Clear Database");
            System.out.println("7. Exit");
            int choice = readInt("Choose an option (1-7): ");

            Runnable action = mainMenuActions.get(choice);
            if (action != null) {
                action.run();
                if (choice == 7) {
                    return; // Terminate program
                }
            } else {
                System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static void clearDatabase() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createNativeMutationQuery("DELETE FROM student_course").executeUpdate();
            session.createMutationQuery("DELETE FROM Student").executeUpdate();
            session.createMutationQuery("DELETE FROM Course").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.err.println("Warning: Database cleanup failed: " + e.getMessage());
        }
    }

    // --- STUDENT SUB-MENU (Java 8 Lambda-based mapping) ---
    private static void studentMenu() {
        Map<Integer, Runnable> actions = new HashMap<>();

        // Create Student Lambda
        actions.put(1, () -> {
            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine().trim();
            while (name.isEmpty()) {
                System.out.print("Name cannot be empty. Enter Student Name: ");
                name = scanner.nextLine().trim();
            }
            int age = readInt("Enter Student Age: ");
            Student student = new Student(name, age);
            studentDAO.saveStudent(student);
            System.out.println("Student created successfully: " + student);
        });

        // Update Student Lambda
        actions.put(2, () -> {
            int updateId = readInt("Enter Student ID to update: ");
            java.util.Optional.ofNullable(studentDAO.getStudentById(updateId))
                    .ifPresentOrElse(
                        toUpdate -> {
                            System.out.print("Enter new name (current: " + toUpdate.getName() + "): ");
                            String newName = scanner.nextLine().trim();
                            int newAge = readInt("Enter new age (current: " + toUpdate.getAge() + "): ");
                            toUpdate.setName(newName);
                            toUpdate.setAge(newAge);
                            studentDAO.updateStudent(toUpdate);
                            System.out.println("Student updated successfully: " + toUpdate);
                        },
                        () -> System.out.println("Student not found with ID: " + updateId)
                    );
        });

        // Delete Student Lambda
        actions.put(3, () -> {
            int deleteId = readInt("Enter Student ID to delete: ");
            java.util.Optional.ofNullable(studentDAO.getStudentById(deleteId))
                    .ifPresentOrElse(
                        studentToDelete -> {
                            studentDAO.deleteStudent(deleteId);
                            System.out.println("Student '" + studentToDelete.getName() + "' deleted successfully.");
                        },
                        () -> System.out.println("Student not found with ID: " + deleteId)
                    );
        });

        // Find Student by ID Lambda
        actions.put(4, () -> {
            int findId = readInt("Enter Student ID to view: ");
            java.util.Optional.ofNullable(studentDAO.getStudentById(findId))
                    .ifPresentOrElse(
                        found -> {
                            System.out.println("Found: " + found);
                            System.out.print("Enrolled Courses: ");
                            if (found.getCourses().isEmpty()) {
                                System.out.println("None");
                            } else {
                                System.out.println();
                                found.getCourses().forEach(c -> System.out.println(" - " + c));
                            }
                        },
                        () -> System.out.println("Student not found with ID: " + findId)
                    );
        });

        // View All Students Lambda
        actions.put(5, () -> {
            System.out.println("All Students list:");
            List<Student> allStudents = studentDAO.getAllStudents();
            if (allStudents.isEmpty()) {
                System.out.println("No students found.");
            } else {
                allStudents.forEach(System.out::println);
            }
        });

        // View Paginated Students Lambda
        actions.put(6, () -> {
            int pageNo = readInt("Enter page number (1-based): ");
            int pageSize = readInt("Enter page size: ");
            System.out.println("Students on page " + pageNo + " (size " + pageSize + "):");
            List<Student> pagedStudents = studentDAO.getAllStudentsPaginated(pageNo, pageSize);
            if (pagedStudents.isEmpty()) {
                System.out.println("No students found on this page.");
            } else {
                pagedStudents.forEach(System.out::println);
            }
        });

        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Create Student");
            System.out.println("2. Update Student Information");
            System.out.println("3. Delete Student");
            System.out.println("4. Find Student by ID");
            System.out.println("5. View All Students");
            System.out.println("6. View All Students (Paginated Bonus)");
            System.out.println("7. Back to Main Menu");
            int choice = readInt("Choose an option (1-7): ");

            if (choice == 7) {
                return; // Return to main menu
            }

            Runnable action = actions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Enter 1-7.");
            }
        }
    }

    // --- COURSE SUB-MENU (Java 8 Lambda-based mapping) ---
    private static void courseMenu() {
        Map<Integer, Runnable> actions = new HashMap<>();

        // Create Course Lambda
        actions.put(1, () -> {
            System.out.print("Enter Course Title: ");
            String title = scanner.nextLine().trim();
            while (title.isEmpty()) {
                System.out.print("Title cannot be empty. Enter Course Title: ");
                title = scanner.nextLine().trim();
            }
            int credits = readInt("Enter Course Credits: ");
            Course course = new Course(title, credits);
            courseDAO.saveCourse(course);
            System.out.println("Course created successfully: " + course);
        });

        // Update Course Lambda
        actions.put(2, () -> {
            int updateId = readInt("Enter Course ID to update: ");
            java.util.Optional.ofNullable(courseDAO.getCourseById(updateId))
                    .ifPresentOrElse(
                        toUpdate -> {
                            System.out.print("Enter new title (current: " + toUpdate.getTitle() + "): ");
                            String newTitle = scanner.nextLine().trim();
                            int newCredits = readInt("Enter new credits (current: " + toUpdate.getCredit() + "): ");
                            toUpdate.setTitle(newTitle);
                            toUpdate.setCredit(newCredits);
                            courseDAO.updateCourse(toUpdate);
                            System.out.println("Course updated successfully: " + toUpdate);
                        },
                        () -> System.out.println("Course not found with ID: " + updateId)
                    );
        });

        // Delete Course Lambda
        actions.put(3, () -> {
            int deleteId = readInt("Enter Course ID to delete: ");
            java.util.Optional.ofNullable(courseDAO.getCourseById(deleteId))
                    .ifPresentOrElse(
                        courseToDelete -> {
                            courseDAO.deleteCourse(deleteId);
                            System.out.println("Course '" + courseToDelete.getTitle() + "' deleted successfully.");
                        },
                        () -> System.out.println("Course not found with ID: " + deleteId)
                    );
        });

        // View All Courses Lambda
        actions.put(4, () -> {
            System.out.println("All Courses list:");
            List<Course> allCourses = courseDAO.getAllCourses();
            if (allCourses.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                allCourses.forEach(System.out::println);
            }
        });

        while (true) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Create Course");
            System.out.println("2. Update Course Information");
            System.out.println("3. Delete Course");
            System.out.println("4. View All Courses");
            System.out.println("5. Back to Main Menu");
            int choice = readInt("Choose an option (1-5): ");

            if (choice == 5) {
                return; // Return to main menu
            }

            Runnable action = actions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Enter 1-5.");
            }
        }
    }

    // --- ENROLLMENT SUB-MENU (Java 8 Lambda-based mapping) ---
    private static void enrollmentMenu() {
        Map<Integer, Runnable> actions = new HashMap<>();

        // Enroll Student Lambda
        actions.put(1, () -> {
            int sId = readInt("Enter Student ID: ");
            int cId = readInt("Enter Course ID: ");
            java.util.Optional<Student> studentOpt = java.util.Optional.ofNullable(studentDAO.getStudentById(sId));
            java.util.Optional<Course> courseOpt = java.util.Optional.ofNullable(courseDAO.getCourseById(cId));

            if (studentOpt.isEmpty()) {
                System.out.println("Error: Student with ID " + sId + " does not exist.");
            } else if (courseOpt.isEmpty()) {
                System.out.println("Error: Course with ID " + cId + " does not exist.");
            } else {
                studentDAO.enrollStudentInCourse(sId, cId);
                System.out.println("Successfully enrolled Student '" + studentOpt.get().getName() + "' in Course '" + courseOpt.get().getTitle() + "'.");
            }
        });

        // Remove Student from Course Lambda
        actions.put(2, () -> {
            int removeSId = readInt("Enter Student ID: ");
            int removeCId = readInt("Enter Course ID: ");
            java.util.Optional<Student> studentOpt = java.util.Optional.ofNullable(studentDAO.getStudentById(removeSId));
            java.util.Optional<Course> courseOpt = java.util.Optional.ofNullable(courseDAO.getCourseById(removeCId));

            if (studentOpt.isEmpty()) {
                System.out.println("Error: Student with ID " + removeSId + " does not exist.");
            } else if (courseOpt.isEmpty()) {
                System.out.println("Error: Course with ID " + removeCId + " does not exist.");
            } else {
                studentDAO.removeStudentFromCourse(removeSId, removeCId);
                System.out.println("Successfully removed Student '" + studentOpt.get().getName() + "' from Course '" + courseOpt.get().getTitle() + "'.");
            }
        });

        // View Student's Courses Lambda
        actions.put(3, () -> {
            int viewSId = readInt("Enter Student ID: ");
            java.util.Optional.ofNullable(studentDAO.getStudentById(viewSId))
                    .ifPresentOrElse(
                        student -> {
                            System.out.println("Courses of Student '" + student.getName() + "':");
                            if (student.getCourses().isEmpty()) {
                                System.out.println(" - Not enrolled in any course.");
                            } else {
                                student.getCourses().forEach(c -> System.out.println(" - " + c.getTitle() + " (" + c.getCredit() + " credits)"));
                            }
                        },
                        () -> System.out.println("Student not found.")
                    );
        });

        // View Course's Students Lambda
        actions.put(4, () -> {
            int viewCId = readInt("Enter Course ID: ");
            java.util.Optional.ofNullable(courseDAO.getCourseById(viewCId))
                    .ifPresentOrElse(
                        course -> {
                            System.out.println("Students enrolled in Course '" + course.getTitle() + "':");
                            List<Student> students = courseDAO.getStudentsByCourse(viewCId);
                            if (students.isEmpty()) {
                                System.out.println(" - No students enrolled.");
                            } else {
                                students.forEach(s -> System.out.println(" - " + s.getName() + " (Age: " + s.getAge() + ")"));
                            }
                        },
                        () -> System.out.println("Course not found.")
                    );
        });

        while (true) {
            System.out.println("\n--- Enrollment Management ---");
            System.out.println("1. Enroll Student in a Course");
            System.out.println("2. Remove Student from a Course");
            System.out.println("3. Display All Courses of a Student");
            System.out.println("4. Display All Students of a Course");
            System.out.println("5. Back to Main Menu");
            int choice = readInt("Choose an option (1-5): ");

            if (choice == 5) {
                return; // Return to main menu
            }

            Runnable action = actions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Enter 1-5.");
            }
        }
    }

    // --- QUERIES SUB-MENU (Java 8 Lambda-based mapping) ---
    private static void queriesMenu() {
        Map<Integer, Runnable> actions = new HashMap<>();

        // Find students older than age Lambda
        actions.put(1, () -> {
            int age = readInt("Enter age threshold: ");
            System.out.println("Students older than " + age + ":");
            List<Student> olderStudents = studentDAO.findStudentsOlderThan(age);
            if (olderStudents.isEmpty()) {
                System.out.println(" - No students found older than " + age);
            } else {
                olderStudents.forEach(s -> System.out.println(" - " + s));
            }
        });

        // List students and courses (Join) Lambda
        actions.put(2, () -> {
            System.out.println("Student-Course Enrollments (HQL Join):");
            List<Object[]> enrollments = studentDAO.findStudentsAndTheirCourses();
            if (enrollments.isEmpty()) {
                System.out.println(" - No enrollments found.");
            } else {
                for (Object[] row : enrollments) {
                    Student s = (Student) row[0];
                    Course c = (Course) row[1];
                    System.out.println(" - Student: " + s.getName() + " -> Enrolled in: " + c.getTitle());
                }
            }
        });

        // Find students by name (Named Query) Lambda
        actions.put(3, () -> {
            System.out.print("Enter name to search (Named Query): ");
            String nameSearch = scanner.nextLine().trim();
            System.out.println("Search results for '" + nameSearch + "':");
            List<Student> studentsByName = studentDAO.findStudentsByNameNamedQuery(nameSearch);
            if (studentsByName.isEmpty()) {
                System.out.println(" - No students found with name: " + nameSearch);
            } else {
                studentsByName.forEach(s -> System.out.println(" - Found: " + s));
            }
        });

        // Find courses with credits > threshold Lambda
        actions.put(4, () -> {
            int creditVal = readInt("Enter credit threshold: ");
            System.out.println("Courses with credits > " + creditVal + " (Criteria API):");
            List<Course> criteriaCourses = courseDAO.findCoursesWithCreditGreaterThan(creditVal);
            if (criteriaCourses.isEmpty()) {
                System.out.println(" - No courses found with credits > " + creditVal);
            } else {
                criteriaCourses.forEach(c -> System.out.println(" - " + c));
            }
        });

        // Aggregation count Lambda
        actions.put(5, () -> {
            System.out.println("Student count per course (Aggregation Query):");
            Map<String, Long> studentCounts = courseDAO.getStudentCountPerCourse();
            if (studentCounts.isEmpty()) {
                System.out.println(" - No courses found.");
            } else {
                studentCounts.forEach((title, count) -> System.out.println(" - " + title + ": " + count));
            }
        });

        // Unenrolled students Lambda
        actions.put(6, () -> {
            System.out.println("Students not enrolled in any course (Bonus):");
            List<Student> unenrolled = studentDAO.findStudentsNotEnrolled();
            if (unenrolled.isEmpty()) {
                System.out.println(" - All students are enrolled in at least one course.");
            } else {
                unenrolled.forEach(s -> System.out.println(" - " + s));
            }
        });

        while (true) {
            System.out.println("\n--- Hibernate Query Practice ---");
            System.out.println("1. Find students older than a given age (HQL)");
            System.out.println("2. List students and their enrolled courses (HQL with Join)");
            System.out.println("3. Find students by name (Named Query)");
            System.out.println("4. Find courses with credit greater than a given value (Criteria API)");
            System.out.println("5. Count how many students are enrolled in each course (Aggregation)");
            System.out.println("6. Find students who are not enrolled in any course (Bonus Query)");
            System.out.println("7. Back to Main Menu");
            int choice = readInt("Choose an option (1-7): ");

            if (choice == 7) {
                return; // Return to main menu
            }

            Runnable action = actions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Enter 1-7.");
            }
        }
    }

    // --- AUTO TEST DEMONSTRATION RUN ---
    private static void runAutoDemo() {
        System.out.println("\n==================================================");
        System.out.println("       STARTING HIBERNATE CRUD & QUERY TEST       ");
        System.out.println("==================================================");

        clearDatabase();

        // 1. SEED DATA (3 Students, 3 Courses + 1 Unenrolled Student)
        System.out.println("\n--- Seeding Initial Data ---");
        Student john = new Student("John", 21);
        Student anna = new Student("Anna", 22);
        Student bob = new Student("Bob", 19);
        Student alice = new Student("Alice", 25);

        studentDAO.saveStudent(john);
        studentDAO.saveStudent(anna);
        studentDAO.saveStudent(bob);
        studentDAO.saveStudent(alice);

        Course java = new Course("Java Programming", 4);
        Course db = new Course("Database Systems", 3);
        Course web = new Course("Web Development", 2);

        courseDAO.saveCourse(java);
        courseDAO.saveCourse(db);
        courseDAO.saveCourse(web);

        System.out.println("Initial Students:");
        studentDAO.getAllStudents().forEach(System.out::println);
        System.out.println("Initial Courses:");
        courseDAO.getAllCourses().forEach(System.out::println);

        // 2. ENROLLMENT LOGIC
        System.out.println("\n--- Enrolling Students into Courses ---");
        // John -> Java, Database Systems
        studentDAO.enrollStudentInCourse(john.getId(), java.getId());
        studentDAO.enrollStudentInCourse(john.getId(), db.getId());

        // Anna -> Java, Database Systems, Web Development
        studentDAO.enrollStudentInCourse(anna.getId(), java.getId());
        studentDAO.enrollStudentInCourse(anna.getId(), db.getId());
        studentDAO.enrollStudentInCourse(anna.getId(), web.getId());

        // Bob -> Web Development
        studentDAO.enrollStudentInCourse(bob.getId(), web.getId());

        // Display Courses for John
        System.out.println("\nCourses of Student 'John':");
        Student johnWithCourses = studentDAO.getStudentById(john.getId());
        if (johnWithCourses != null) {
            johnWithCourses.getCourses().forEach(c -> System.out.println(" - " + c.getTitle() + " (" + c.getCredit() + " credits)"));
        }

        // Display Students in Java Programming
        System.out.println("\nStudents enrolled in 'Java Programming':");
        List<Student> javaStudents = courseDAO.getStudentsByCourse(java.getId());
        javaStudents.forEach(s -> System.out.println(" - " + s.getName() + " (Age: " + s.getAge() + ")"));

        // 3. DEMONSTRATE CRUD OPERATIONS
        System.out.println("\n--- Demonstrating CRUD Operations ---");
        
        // Read Student by ID
        System.out.println("Getting student with ID " + anna.getId() + ":");
        System.out.println("Found: " + studentDAO.getStudentById(anna.getId()));

        // Update Student
        System.out.println("\nUpdating Bob's age from 19 to 20:");
        bob.setAge(20);
        studentDAO.updateStudent(bob);
        System.out.println("Bob's updated info: " + studentDAO.getStudentById(bob.getId()));

        // Update Course
        System.out.println("\nUpdating 'Web Development' credits from 2 to 3:");
        web.setCredit(3);
        courseDAO.updateCourse(web);
        System.out.println("Web Course updated info: " + courseDAO.getCourseById(web.getId()));

        // Create, enroll and Delete a Student (To prove Delete CRUD and join table cleanup works)
        System.out.println("\nCreating temporary student 'Temp Student'...");
        Student tempStudent = new Student("Temp Student", 30);
        studentDAO.saveStudent(tempStudent);
        studentDAO.enrollStudentInCourse(tempStudent.getId(), java.getId());
        System.out.println("Enrolled count for Java: " + courseDAO.getStudentsByCourse(java.getId()).size());
        
        System.out.println("Deleting 'Temp Student'...");
        studentDAO.deleteStudent(tempStudent.getId());
        System.out.println("Enrolled count for Java after deletion: " + courseDAO.getStudentsByCourse(java.getId()).size());

        // Create, enroll and Delete a Course (To prove Course deletion cleanups)
        System.out.println("\nCreating temporary course 'Temp Course'...");
        Course tempCourse = new Course("Temp Course", 1);
        courseDAO.saveCourse(tempCourse);
        studentDAO.enrollStudentInCourse(bob.getId(), tempCourse.getId());
        System.out.println("Bob's course count: " + studentDAO.getStudentById(bob.getId()).getCourses().size());

        System.out.println("Deleting 'Temp Course'...");
        courseDAO.deleteCourse(tempCourse.getId());
        System.out.println("Bob's course count after deletion: " + studentDAO.getStudentById(bob.getId()).getCourses().size());

        // 4. TASK 5 QUERY PRACTICES
        System.out.println("\n--- Query Practices ---");

        // 4.1 HQL Query: Find all students older than a given age (e.g. 20)
        System.out.println("\n[HQL Query] Students older than 20:");
        List<Student> olderThan20 = studentDAO.findStudentsOlderThan(20);
        olderThan20.forEach(s -> System.out.println(" - " + s));

        // 4.2 HQL with Join: List students and the courses they are enrolled in
        System.out.println("\n[HQL Join Query] Student-Course Enrollments:");
        List<Object[]> enrollments = studentDAO.findStudentsAndTheirCourses();
        for (Object[] row : enrollments) {
            Student s = (Student) row[0];
            Course c = (Course) row[1];
            System.out.println(" - Student: " + s.getName() + " -> Enrolled in: " + c.getTitle());
        }

        // 4.3 Named Query: Find students by name (e.g. "Anna")
        System.out.println("\n[Named Query] Find students by name 'Anna':");
        List<Student> annas = studentDAO.findStudentsByNameNamedQuery("Anna");
        annas.forEach(s -> System.out.println(" - Found: " + s));

        // 4.4 Criteria API: Find courses that have credit greater than a given value (e.g. 2)
        System.out.println("\n[Criteria API Query] Courses with credits > 2:");
        List<Course> highCreditCourses = courseDAO.findCoursesWithCreditGreaterThan(2);
        highCreditCourses.forEach(c -> System.out.println(" - " + c));

        // 4.5 Aggregation Query: Count how many students are enrolled in each course
        System.out.println("\n[Aggregation Query] Student count per course:");
        Map<String, Long> studentCounts = courseDAO.getStudentCountPerCourse();
        studentCounts.forEach((title, count) -> System.out.println(" - " + title + ": " + count));

        // 5. BONUS QUERIES
        System.out.println("\n--- Bonus Exercises ---");

        // 5.1 Pagination for "list all students" (page size = 2)
        System.out.println("\n[Pagination] Listing all students (Page 1, Size 2):");
        List<Student> page1 = studentDAO.getAllStudentsPaginated(1, 2);
        page1.forEach(s -> System.out.println(" - " + s));

        System.out.println("[Pagination] Listing all students (Page 2, Size 2):");
        List<Student> page2 = studentDAO.getAllStudentsPaginated(2, 2);
        page2.forEach(s -> System.out.println(" - " + s));

        // 5.2 Find students who are not enrolled in any course
        System.out.println("\n[Bonus Query] Students not enrolled in any course:");
        List<Student> unenrolled = studentDAO.findStudentsNotEnrolled();
        if (unenrolled.isEmpty()) {
            System.out.println(" - None found.");
        } else {
            unenrolled.forEach(s -> System.out.println(" - " + s));
        }

        System.out.println("\n==================================================");
        System.out.println("          AUTOMATED DEMO RUN COMPLETED            ");
        System.out.println("==================================================");
    }
}
