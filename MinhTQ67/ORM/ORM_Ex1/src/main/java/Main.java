import dao.CourseDao;
import dao.StudentDao;
import entity.Course;
import entity.Student;
import service.EnrollmentService;
import ultil.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDao studentDao = new StudentDao();
    private static final CourseDao courseDao = new CourseDao();
    private static final EnrollmentService svc = new EnrollmentService();

    public static void main(String[] args) {
        try {
            boolean running = true;
            while (running) {
                printMainMenu();
                int choice = getIntInput("Choose an option: ");
                switch (choice) {
                    case 1: insertSampleData(); break;
                    case 2: studentMenu(); break;
                    case 3: courseMenu(); break;
                    case 4: enrollmentMenu(); break;
                    case 5: runQueries(); break;
                    case 0: running = false; System.out.println("Exiting..."); break;
                    default: System.out.println("Invalid option. Please choose a valid number from the menu.");
                }
            }
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
        }
    }

    private static void printMainMenu() {
        System.out.println("\n=== HIBERNATE CRUD & QUERY PRACTICE ===");
        System.out.println("1. Setup & Sample Data");
        System.out.println("2. Student Management");
        System.out.println("3. Course Management");
        System.out.println("4. Enrollment Management");
        System.out.println("5. Run Task 5 & Bonus Queries");
        System.out.println("0. Exit");
    }

    private static void studentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Create Student");
            System.out.println("2. Update Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Show all Students");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Choose an option: ");
            switch (choice) {
                case 1: createStudent(); break;
                case 2: updateStudent(); break;
                case 3: deleteStudent(); break;
                case 4: showAllStudents(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void courseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Create Course");
            System.out.println("2. Update Course");
            System.out.println("3. Delete Course");
            System.out.println("4. Show all Courses");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Choose an option: ");
            switch (choice) {
                case 1: createCourse(); break;
                case 2: updateCourse(); break;
                case 3: deleteCourse(); break;
                case 4: showAllCourses(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void enrollmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Enrollment Management ---");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Unenroll Student from Course");
            System.out.println("3. Show Courses of Student");
            System.out.println("4. Show Students of Course");
            System.out.println("0. Back to Main Menu");
            int choice = getIntInput("Choose an option: ");
            switch (choice) {
                case 1: enrollStudent(); break;
                case 2: unenrollStudent(); break;
                case 3: showCoursesOfStudent(); break;
                case 4: showStudentsOfCourse(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static int getIntInput(String prompt, boolean allowEmpty, int emptyValue) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                if (allowEmpty) {
                    return emptyValue;
                }
                System.out.println("Input cannot be empty. Please enter a number.");
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid integer.");
            }
        }
    }

    private static int getIntInput(String prompt) {
        return getIntInput(prompt, false, -1);
    }

    private static int getPositiveIntInput(String prompt) {
        while (true) {
            int val = getIntInput(prompt);
            if (val <= 0) {
                System.out.println("Value must be greater than 0. Please try again.");
            } else {
                return val;
            }
        }
    }

    private static String getStringInput(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String val = scanner.nextLine().trim();
            if (!allowEmpty && val.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            } else {
                return val;
            }
        }
    }

    private static void insertSampleData() {
        System.out.println("\n--- Inserting Sample Data ---");
        if (!studentDao.findAll().isEmpty() || !courseDao.findAll().isEmpty()) {
            System.out.println("Database already contains data. Skipping sample data insertion to prevent duplicates.");
            return;
        }
        Student john = studentDao.save(new Student("John", 19));
        Student anna = studentDao.save(new Student("Anna", 22));
        Student peter = studentDao.save(new Student("Peter", 25));
        Student lisa = studentDao.save(new Student("Lisa", 18));

        Course java = courseDao.save(new Course("Java Programming", 4));
        Course db = courseDao.save(new Course("Database Systems", 3));
        Course spring = courseDao.save(new Course("Spring Framework", 5));

        svc.enroll(john.getId(), java.getId());
        svc.enroll(john.getId(), db.getId());
        
        svc.enroll(anna.getId(), java.getId());
        svc.enroll(anna.getId(), db.getId());
        svc.enroll(anna.getId(), spring.getId());
        
        svc.enroll(peter.getId(), db.getId());
        svc.enroll(peter.getId(), spring.getId());

        System.out.println("Sample data inserted and enrolled successfully.");
    }

    private static void createStudent() {
        System.out.println("\n--- Create Student ---");
        String name;
        while (true) {
            name = getStringInput("Enter student name: ", false);
            String finalName = name;
            boolean exists = studentDao.findAll().stream().anyMatch(s -> s.getName().equalsIgnoreCase(finalName));
            if (exists) {
                System.out.println("A student with this name already exists. Please enter a different name.");
            } else {
                break;
            }
        }
        int age = getPositiveIntInput("Enter student age: ");
        Student s = studentDao.save(new Student(name, age));
        System.out.println("Created: " + s);
    }

    private static void updateStudent() {
        System.out.println("\n--- Update Student ---");
        int id = getPositiveIntInput("Enter student ID to update: ");
        Student s = studentDao.findById(id);
        if (s == null) {
            System.out.println("Student not found.");
            return;
        }
        
        String name;
        while (true) {
            name = getStringInput("Enter new name (leave blank to keep current '" + s.getName() + "'): ", true);
            if (!name.isEmpty()) {
                String finalName = name;
                boolean exists = studentDao.findAll().stream()
                        .anyMatch(other -> other.getId() != id && other.getName().equalsIgnoreCase(finalName));
                if (exists) {
                    System.out.println("Another student with this name already exists. Please enter a different name.");
                    continue;
                }
                s.setName(name);
            }
            break;
        }
        
        int age = getIntInput("Enter new age (leave blank to keep current '" + s.getAge() + "'): ", true, -1);
        if (age != -1 && age > 0) {
            s.setAge(age);
        } else if (age != -1) {
            System.out.println("Invalid age provided, keeping current age.");
        }

        studentDao.update(s);
        System.out.println("Updated: " + s);
    }

    private static void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        int id = getPositiveIntInput("Enter student ID to delete: ");
        studentDao.delete(id);
        System.out.println("Deleted student with ID " + id);
    }

    private static void showAllStudents() {
        System.out.println("\n--- All Students ---");
        studentDao.findAll().forEach(System.out::println);
    }

    private static void createCourse() {
        System.out.println("\n--- Create Course ---");
        String title;
        while (true) {
            title = getStringInput("Enter course title: ", false);
            String finalTitle = title;
            boolean exists = courseDao.findAll().stream().anyMatch(c -> c.getTitle().equalsIgnoreCase(finalTitle));
            if (exists) {
                System.out.println("A course with this title already exists. Please enter a different title.");
            } else {
                break;
            }
        }
        int credit = getPositiveIntInput("Enter course credit: ");
        Course c = courseDao.save(new Course(title, credit));
        System.out.println("Created: " + c);
    }

    private static void updateCourse() {
        System.out.println("\n--- Update Course ---");
        int id = getPositiveIntInput("Enter course ID to update: ");
        Course c = courseDao.findById(id);
        if (c == null) {
            System.out.println("Course not found.");
            return;
        }
        
        String title;
        while (true) {
            title = getStringInput("Enter new title (leave blank to keep current '" + c.getTitle() + "'): ", true);
            if (!title.isEmpty()) {
                String finalTitle = title;
                boolean exists = courseDao.findAll().stream()
                        .anyMatch(other -> other.getId() != id && other.getTitle().equalsIgnoreCase(finalTitle));
                if (exists) {
                    System.out.println("Another course with this title already exists. Please enter a different title.");
                    continue;
                }
                c.setTitle(title);
            }
            break;
        }
        
        int credit = getIntInput("Enter new credit (leave blank to keep current '" + c.getCredit() + "'): ", true, -1);
        if (credit != -1 && credit > 0) {
            c.setCredit(credit);
        } else if (credit != -1) {
            System.out.println("Invalid credit provided, keeping current credit.");
        }

        courseDao.update(c);
        System.out.println("Updated: " + c);
    }

    private static void deleteCourse() {
        System.out.println("\n--- Delete Course ---");
        int id = getPositiveIntInput("Enter course ID to delete: ");
        courseDao.delete(id);
        System.out.println("Deleted course with ID " + id);
    }

    private static void showAllCourses() {
        System.out.println("\n--- All Courses ---");
        courseDao.findAll().forEach(System.out::println);
    }

    private static void enrollStudent() {
        System.out.println("\n--- Enroll Student in Course ---");
        while (true) {
            int studentId = getPositiveIntInput("Enter Student ID: ");
            Student s = studentDao.findById(studentId);
            if (s == null) {
                System.out.println("Student not found. Please try again.");
                continue;
            }
            
            int courseId = getPositiveIntInput("Enter Course ID: ");
            Course c = courseDao.findById(courseId);
            if (c == null) {
                System.out.println("Course not found. Please try again.");
                continue;
            }
            
            try {
                List<Course> enrolledCourses = svc.getCoursesOfStudent(studentId);
                boolean alreadyEnrolled = enrolledCourses.stream().anyMatch(course -> course.getId() == courseId);
                if (alreadyEnrolled) {
                    System.out.println("This student is ALREADY enrolled in this course! Please enter a different enrollment.");
                    continue;
                }
                
                svc.enroll(studentId, courseId);
                System.out.println("Enrolled student '" + s.getName() + "' in course '" + c.getTitle() + "'.");
                break;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                break;
            }
        }
    }

    private static void unenrollStudent() {
        System.out.println("\n--- Unenroll Student from Course ---");
        int studentId = getPositiveIntInput("Enter Student ID: ");
        int courseId = getPositiveIntInput("Enter Course ID: ");
        try {
            svc.unenroll(studentId, courseId);
            System.out.println("Unenrolled student " + studentId + " from course " + courseId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showCoursesOfStudent() {
        System.out.println("\n--- Courses of Student ---");
        int id = getPositiveIntInput("Enter Student ID: ");
        try {
            List<Course> courses = svc.getCoursesOfStudent(id);
            if (courses.isEmpty()) System.out.println("No courses found for this student.");
            courses.forEach(c -> System.out.println(c.getTitle()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showStudentsOfCourse() {
        System.out.println("\n--- Students in Course ---");
        int id = getPositiveIntInput("Enter Course ID: ");
        try {
            List<Student> students = svc.getStudentsOfCourse(id);
            if (students.isEmpty()) System.out.println("No students found in this course.");
            students.forEach(s -> System.out.println(s.getName()));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void runQueries() {
        System.out.println("\n--- Task 5 Queries ---");
        
        System.out.println("- Students older than 20:");
        svc.findStudentsOlderThan(20).forEach(System.out::println);

        // Required to match example output exactly: "Courses of student John:"
        // We will just find student with name "John" to demonstrate, or use ID 1.
        System.out.println("- Courses of student John (assuming ID=1):");
        try {
            svc.getCoursesOfStudent(1).forEach(c -> System.out.println(c.getTitle()));
        } catch(Exception ignored) {}

        System.out.println("- Student count per course:");
        List<Object[]> counts = svc.countStudentsPerCourse();
        for (Object[] row : counts) {
            System.out.println(row[0] + ": " + row[1]);
        }

        System.out.println("- Students and their enrolled courses (HQL with Join):");
        List<Object[]> rows = svc.listStudentsWithCourses();
        for (Object[] row : rows) {
            Student s = (Student) row[0];
            Course c = (Course) row[1];
            System.out.println(s.getName() + " -> " + c.getTitle());
        }

        System.out.println("- Find student by name 'Anna' (Named Query):");
        svc.findStudentsByName("Anna").forEach(System.out::println);

        System.out.println("- Courses with credit greater than 3 (Criteria API):");
        svc.findCoursesByMinCredit(3).forEach(c -> System.out.println(c.getTitle()));

        System.out.println("\n--- Bonus Queries ---");
        System.out.println("- Pagination (Page 1, size 2):");
        studentDao.findAllPaged(1, 2).forEach(s -> System.out.println(s.getName()));
        
        System.out.println("- Students NOT enrolled in any course:");
        svc.findStudentsWithNoCourses().forEach(s -> System.out.println(s.getName()));
    }
}
