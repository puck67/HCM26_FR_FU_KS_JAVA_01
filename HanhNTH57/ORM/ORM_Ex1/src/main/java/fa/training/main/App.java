package fa.training.main;

import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.services.EnrollmentService;
import fa.training.utils.HibernateUtils;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class App {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final EnrollmentService enrollmentService = new EnrollmentService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println(CYAN + "=== Hibernate & H2 Student-Course Management ===" + RESET);
            insertSampleData();
            
            boolean exit = false;
            while (!exit) {
                printMainMenu();
                int choice = readInt("Enter choice: ");
                switch (choice) {
                    case 1 -> studentManagementMenu();
                    case 2 -> courseManagementMenu();
                    case 3 -> runQueries();
                    case 0 -> exit = true;
                    default -> System.out.println(RED + "Invalid choice!" + RESET);
                }
            }
        } finally {
            HibernateUtils.shutdown();
            scanner.close();
        }
    }

    private static void printMainMenu() {
        System.out.println("\n" + YELLOW + "--- MAIN MENU ---" + RESET);
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Hibernate Query Practice (Task 5)");
        System.out.println("0. Exit");
    }

    // === STUDENT MANAGEMENT ===
    private static void studentManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + CYAN + "--- STUDENT MANAGEMENT ---" + RESET);
            System.out.println("1. Create Student");
            System.out.println("2. Update Student");
            System.out.println("3. Delete Student");
            System.out.println("4. Find Student by ID");
            System.out.println("5. List All Students");
            System.out.println("6. Enroll Student in Course");
            System.out.println("7. Remove Student from Course");
            System.out.println("8. List Student's Courses");
            System.out.println("9. List Students Paginated (Bonus)");
            System.out.println("10. List Students Not Enrolled (Bonus)");
            System.out.println("0. Back to Main Menu");
            
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> createStudent();
                case 2 -> updateStudent();
                case 3 -> deleteStudent();
                case 4 -> findStudentById();
                case 5 -> listAllStudents();
                case 6 -> enrollStudent();
                case 7 -> unenrollStudent();
                case 8 -> listStudentCourses();
                case 9 -> listStudentsPaginated();
                case 10 -> listStudentsNotEnrolled();
                case 0 -> back = true;
                default -> System.out.println(RED + "Invalid choice!" + RESET);
            }
        }
    }

    // === COURSE MANAGEMENT ===
    private static void courseManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n" + CYAN + "--- COURSE MANAGEMENT ---" + RESET);
            System.out.println("1. Create Course");
            System.out.println("2. Update Course");
            System.out.println("3. Delete Course");
            System.out.println("4. List All Courses");
            System.out.println("5. List Course's Students");
            System.out.println("0. Back to Main Menu");
            
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> createCourse();
                case 2 -> updateCourse();
                case 3 -> deleteCourse();
                case 4 -> listAllCourses();
                case 5 -> listCourseStudents();
                case 0 -> back = true;
                default -> System.out.println(RED + "Invalid choice!" + RESET);
            }
        }
    }

    // --- Student Actions ---
    private static void createStudent() {
        String name = readString("Enter student name: ");
        int age = readInt("Enter student age: ");
        Student s = new Student();
        s.setName(name);
        s.setAge(age);
        studentDAO.save(s);
        System.out.println(GREEN + "Student created with ID: " + s.getId() + RESET);
    }

    private static void updateStudent() {
        int id = readInt("Enter student ID to update: ");
        Student s = studentDAO.findById(id);
        if (s == null) {
            System.out.println(RED + "Student not found!" + RESET);
            return;
        }
        s.setName(readString("Enter new name (" + s.getName() + "): "));
        s.setAge(readInt("Enter new age (" + s.getAge() + "): "));
        studentDAO.update(s);
        System.out.println(GREEN + "Student updated successfully." + RESET);
    }

    private static void deleteStudent() {
        int id = readInt("Enter student ID to delete: ");
        studentDAO.delete(id);
        System.out.println(GREEN + "Operation completed." + RESET);
    }

    private static void findStudentById() {
        int id = readInt("Enter student ID: ");
        Student s = studentDAO.findById(id);
        if (s != null) {
            System.out.println(GREEN + s + RESET);
        } else {
            System.out.println(RED + "Student not found!" + RESET);
        }
    }

    private static void listAllStudents() {
        List<Student> students = studentDAO.findAll();
        System.out.println(CYAN + "\n--- All Students ---" + RESET);
        students.forEach(System.out::println);
    }

    private static void enrollStudent() {
        int sId = readInt("Enter student ID: ");
        int cId = readInt("Enter course ID: ");
        enrollmentService.enroll(sId, cId);
        System.out.println(GREEN + "Enrollment successful." + RESET);
    }

    private static void unenrollStudent() {
        int sId = readInt("Enter student ID: ");
        int cId = readInt("Enter course ID: ");
        enrollmentService.unenroll(sId, cId);
        System.out.println(GREEN + "Student removed from course." + RESET);
    }

    private static void listStudentCourses() {
        int id = readInt("Enter student ID: ");
        Set<Course> courses = enrollmentService.getStudentCourses(id);
        if (courses != null) {
            System.out.println(CYAN + "Courses for student " + id + ":" + RESET);
            courses.forEach(c -> System.out.println("- " + c.getTitle()));
        } else {
            System.out.println(RED + "Student not found!" + RESET);
        }
    }

    private static void listStudentsPaginated() {
        int page = readInt("Enter page number: ");
        int size = readInt("Enter page size: ");
        List<Student> students = studentDAO.findAllPaginated(page, size);
        System.out.println(CYAN + "--- Students (Page " + page + ") ---" + RESET);
        students.forEach(System.out::println);
    }

    private static void listStudentsNotEnrolled() {
        List<Student> students = studentDAO.findNotEnrolled();
        System.out.println(CYAN + "--- Students Not Enrolled in Any Course ---" + RESET);
        students.forEach(System.out::println);
    }

    // --- Course Actions ---
    private static void createCourse() {
        String title = readString("Enter course title: ");
        int credit = readInt("Enter course credit: ");
        Course c = new Course();
        c.setTitle(title);
        c.setCredit(credit);
        courseDAO.save(c);
        System.out.println(GREEN + "Course created with ID: " + c.getId() + RESET);
    }

    private static void updateCourse() {
        int id = readInt("Enter course ID to update: ");
        Course c = courseDAO.findById(id);
        if (c == null) {
            System.out.println(RED + "Course not found!" + RESET);
            return;
        }
        c.setTitle(readString("Enter new title (" + c.getTitle() + "): "));
        c.setCredit(readInt("Enter new credit (" + c.getCredit() + "): "));
        courseDAO.update(c);
        System.out.println(GREEN + "Course updated successfully." + RESET);
    }

    private static void deleteCourse() {
        int id = readInt("Enter course ID to delete: ");
        courseDAO.delete(id);
        System.out.println(GREEN + "Operation completed." + RESET);
    }

    private static void listAllCourses() {
        List<Course> courses = courseDAO.findAll();
        System.out.println(CYAN + "\n--- All Courses ---" + RESET);
        courses.forEach(System.out::println);
    }

    private static void listCourseStudents() {
        int id = readInt("Enter course ID: ");
        Set<Student> students = enrollmentService.getCourseStudents(id);
        if (students != null) {
            System.out.println(CYAN + "Students in course " + id + ":" + RESET);
            students.forEach(s -> System.out.println("- " + s.getName()));
        } else {
            System.out.println(RED + "Course not found!" + RESET);
        }
    }

    // --- Extra Queries ---
    private static void runQueries() {
        System.out.println(YELLOW + "\n=== Task 5: Hibernate Query Practice ===" + RESET);
        System.out.println(CYAN + "\n1. Students older than 20 (HQL):" + RESET);
        studentDAO.findByAgeGreaterThan(20).forEach(System.out::println);
        
        System.out.println(CYAN + "\n2. Students and courses (HQL Join):" + RESET);
        studentDAO.findAllWithCourses().forEach(row -> 
            System.out.println("Student: " + row[0] + " enrolled in: " + row[1]));
            
        System.out.println(CYAN + "\n3. Find student by name 'Anna Smith' (Named Query):" + RESET);
        studentDAO.findByName("Anna Smith").forEach(System.out::println);
        
        System.out.println(CYAN + "\n4. Courses with credit > 3 (Criteria API):" + RESET);
        courseDAO.findByCreditGreaterThan(3).forEach(c -> 
            System.out.println("Course: " + c.getTitle() + " (Credits: " + c.getCredit() + ")"));
            
        System.out.println(CYAN + "\n5. Student count per course (Aggregation):" + RESET);
        courseDAO.countStudentsPerCourse().forEach(row -> 
            System.out.println(row[0] + ": " + row[1] + " students"));
    }

    private static void insertSampleData() {
        if (!studentDAO.findAll().isEmpty() || !courseDAO.findAll().isEmpty()) {
            return;
        }
        System.out.println(YELLOW + "Inserting sample data..." + RESET);
        Student s1 = new Student(); s1.setName("John Doe"); s1.setAge(21);
        Student s2 = new Student(); s2.setName("Anna Smith"); s2.setAge(22);
        Student s3 = new Student(); s3.setName("Bob Brown"); s3.setAge(19);
        studentDAO.save(s1); studentDAO.save(s2); studentDAO.save(s3);
        
        Course c1 = new Course(); c1.setTitle("Java Programming"); c1.setCredit(4);
        Course c2 = new Course(); c2.setTitle("Database Systems"); c2.setCredit(3);
        Course c3 = new Course(); c3.setTitle("Web Development"); c3.setCredit(3);
        courseDAO.save(c1); courseDAO.save(c2); courseDAO.save(c3);
        
        enrollmentService.enroll(s1.getId(), c1.getId());
        enrollmentService.enroll(s1.getId(), c2.getId());
        enrollmentService.enroll(s2.getId(), c1.getId());
        System.out.println(GREEN + "Sample data inserted successfully." + RESET);
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input. Please enter a number." + RESET);
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
