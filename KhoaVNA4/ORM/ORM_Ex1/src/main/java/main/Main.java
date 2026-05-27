package main;

import dao.StudentDAO;
import dao.StudentDAOImpl;
import dao.CourseDAO;
import dao.CourseDAOImpl;
import dao.HibernatePracticeDAO;
import dao.HibernatePracticeDAOImpl;
import entities.Student;
import entities.Course;
import utils.InputUtil;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final StudentDAO studentDAO = new StudentDAOImpl();
    private static final CourseDAO courseDAO = new CourseDAOImpl();
    private static final HibernatePracticeDAO hibernatePracticeDAO = new HibernatePracticeDAOImpl();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("               MAIN MENU                 ");
            System.out.println("=========================================");
            System.out.println("1. Management Student");
            System.out.println("2. Management Course");
            System.out.println("3. Enrollment");
            System.out.println("4. Hibernate Query");
            System.out.println("5. Exit");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-5): ", 1, 5,
                    "Invalid choice! Please choose a number between 1 and 5.");

            switch (choice) {
                case 1 -> managementStudent(scanner);
                case 2 -> managementCourse(scanner);
                case 3 -> managementEnrollment(scanner);
                case 4 -> managementHibernateQuery(scanner);
                case 5 -> System.out.println("Exiting... Goodbye!");
            }
        } while (choice != 5);
        scanner.close();
        database.HibernateUtil.shutdown();
    }

    private static void managementStudent(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("            STUDENT MANAGEMENT             ");
            System.out.println("=========================================");
            System.out.println("1. Add new Student");
            System.out.println("2. Display all Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Back to Main Menu");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-6): ", 1, 6,
                    "Invalid choice! Please choose a number between 1 and 6.");

            switch (choice) {
                case 1 -> addNewStudent(scanner);
                case 2 -> displayAllStudents();
                case 3 -> searchStudentById(scanner);
                case 4 -> updateStudent(scanner);
                case 5 -> deleteStudent(scanner);
                case 6 -> System.out.println("Returning to Main Menu...");
            }
        } while (choice != 6);
    }

    private static void addNewStudent(Scanner scanner) {
        System.out.println("\n--- Add New Student ---");
        String studentName = InputUtil.readNonEmptyString(scanner, "Enter Student Name: ",
                "Student Name cannot be empty.");
        int studentAge = InputUtil.readIntInRange(scanner, "Enter Student Age: ", 1, 120,
                "Student Age must be between 1 and 120.");

        Student student = new Student(studentName, studentAge);
        studentDAO.save(student);
    }

    private static void displayAllStudents() {
        System.out.println("\n--- Display All Students ---");
        List<Student> students = studentDAO.findAll();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
                if (s.getCourses() != null && !s.getCourses().isEmpty()) {
                    System.out.print("  Enrolled Courses: ");
                    for (Course c : s.getCourses()) {
                        System.out.print(c.getTitle() + " (ID: " + c.getId() + ") | ");
                    }
                    System.out.println();
                }
            }
        }
    }

    private static void searchStudentById(Scanner scanner) {
        System.out.println("\n--- Search Student by ID ---");
        int id = InputUtil.readInt(scanner, "Enter Student ID to search: ", "ID must be an integer.");
        Student student = studentDAO.findById(id);
        if (student != null) {
            System.out.println("Student found: " + student);
            if (student.getCourses() != null && !student.getCourses().isEmpty()) {
                System.out.println("Enrolled Courses:");
                for (Course c : student.getCourses()) {
                    System.out.println("  - " + c);
                }
            } else {
                System.out.println("No courses enrolled.");
            }
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    private static void updateStudent(Scanner scanner) {
        System.out.println("\n--- Update Student ---");
        int id = InputUtil.readInt(scanner, "Enter Student ID to update: ", "ID must be an integer.");
        Student student = studentDAO.findById(id);
        if (student != null) {
            System.out.println("Current details: " + student);
            String newName = InputUtil.readStringOrKeep(scanner, "Enter new Name", student.getName());

            int newAge = student.getAge();
            while (true) {
                System.out.print("Enter new Age (leave empty to keep '" + student.getAge() + "'): ");
                String ageInput = scanner.nextLine().trim();
                if (ageInput.isEmpty()) {
                    break;
                }
                try {
                    int ageVal = Integer.parseInt(ageInput);
                    if (ageVal >= 1 && ageVal <= 120) {
                        newAge = ageVal;
                        break;
                    } else {
                        System.out.println("Age must be between 1 and 120.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Age must be an integer.");
                }
            }

            student.setName(newName);
            student.setAge(newAge);
            studentDAO.update(student);
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    private static void deleteStudent(Scanner scanner) {
        System.out.println("\n--- Delete Student ---");
        int id = InputUtil.readInt(scanner, "Enter Student ID to delete: ", "ID must be an integer.");
        Student student = studentDAO.findById(id);
        if (student != null) {
            // Unenroll from all courses first to clean database records
            student.getCourses().clear();
            studentDAO.update(student);
            studentDAO.delete(id);
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    private static void managementCourse(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("             COURSE MANAGEMENT              ");
            System.out.println("=========================================");
            System.out.println("1. Add new Course");
            System.out.println("2. Display all Courses");
            System.out.println("3. Search Course by ID");
            System.out.println("4. Update Course");
            System.out.println("5. Delete Course");
            System.out.println("6. Back to Main Menu");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-6): ", 1, 6,
                    "Invalid choice! Please choose a number between 1 and 6.");

            switch (choice) {
                case 1 -> addNewCourse(scanner);
                case 2 -> displayAllCourses();
                case 3 -> searchCourseById(scanner);
                case 4 -> updateCourse(scanner);
                case 5 -> deleteCourse(scanner);
                case 6 -> System.out.println("Returning to Main Menu...");
            }
        } while (choice != 6);
    }

    private static void addNewCourse(Scanner scanner) {
        System.out.println("\n--- Add New Course ---");
        String title = InputUtil.readNonEmptyString(scanner, "Enter Course Title: ", "Course Title cannot be empty.");
        int credit = InputUtil.readIntInRange(scanner, "Enter Credit: ", 1, 10, "Credit must be between 1 and 10.");

        Course course = new Course(title, credit);
        courseDAO.save(course);
        System.out.println("Course added successfully!");
    }

    private static void displayAllCourses() {
        System.out.println("\n--- Display All Courses ---");
        List<Course> courses = courseDAO.findAll();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            for (Course c : courses) {
                System.out.println(c);
                if (c.getStudents() != null && !c.getStudents().isEmpty()) {
                    System.out.print("  Enrolled Students: ");
                    for (Student s : c.getStudents()) {
                        System.out.print(s.getName() + " (ID: " + s.getId() + ") | ");
                    }
                    System.out.println();
                }
            }
        }
    }

    private static void searchCourseById(Scanner scanner) {
        System.out.println("\n--- Search Course by ID ---");
        int id = InputUtil.readInt(scanner, "Enter Course ID to search: ", "ID must be an integer.");
        Course course = courseDAO.findById(id);
        if (course != null) {
            System.out.println("Course found: " + course);
            if (course.getStudents() != null && !course.getStudents().isEmpty()) {
                System.out.println("Enrolled Students:");
                for (Student s : course.getStudents()) {
                    System.out.println("  - " + s.getName() + " (ID: " + s.getId() + ")");
                }
            } else {
                System.out.println("No students enrolled.");
            }
        } else {
            System.out.println("Course with ID " + id + " not found.");
        }
    }

    private static void updateCourse(Scanner scanner) {
        System.out.println("\n--- Update Course ---");
        int id = InputUtil.readInt(scanner, "Enter Course ID to update: ", "ID must be an integer.");
        Course course = courseDAO.findById(id);
        if (course != null) {
            System.out.println("Current details: " + course);
            String newTitle = InputUtil.readStringOrKeep(scanner, "Enter new Title", course.getTitle());

            int newCredit = course.getCredit();
            while (true) {
                System.out.print("Enter new Credit (leave empty to keep '" + course.getCredit() + "'): ");
                String creditInput = scanner.nextLine().trim();
                if (creditInput.isEmpty()) {
                    break;
                }
                try {
                    int credVal = Integer.parseInt(creditInput);
                    if (credVal >= 1 && credVal <= 10) {
                        newCredit = credVal;
                        break;
                    } else {
                        System.out.println("Credit must be between 1 and 10.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Credit must be an integer.");
                }
            }

            course.setTitle(newTitle);
            course.setCredit(newCredit);
            courseDAO.update(course);
            System.out.println("Course updated successfully!");
        } else {
            System.out.println("Course with ID " + id + " not found.");
        }
    }

    private static void deleteCourse(Scanner scanner) {
        System.out.println("\n--- Delete Course ---");
        int id = InputUtil.readInt(scanner, "Enter Course ID to delete: ", "ID must be an integer.");
        Course course = courseDAO.findById(id);
        if (course != null) {
            for (Student s : course.getStudents()) {
                s.getCourses().remove(course);
                studentDAO.update(s);
            }
            course.getStudents().clear();
            courseDAO.update(course);
            courseDAO.delete(id);
            System.out.println("Course deleted successfully!");
        } else {
            System.out.println("Course with ID " + id + " not found.");
        }
    }

    private static void managementEnrollment(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("            ENROLLMENT SYSTEM            ");
            System.out.println("=========================================");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Remove Student from Course");
            System.out.println("3. Display all courses of a student");
            System.out.println("4. Display all students of a course");
            System.out.println("5. Back to Main Menu");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-5): ", 1, 5,
                    "Invalid choice! Please choose a number between 1 and 5.");

            switch (choice) {
                case 1 -> enrollStudent(scanner);
                case 2 -> unenrollStudent(scanner);
                case 3 -> displayCoursesOfStudent(scanner);
                case 4 -> displayStudentsOfCourse(scanner);
                case 5 -> System.out.println("Returning to Main Menu...");
            }
        } while (choice != 5);
    }

    private static void enrollStudent(Scanner scanner) {
        System.out.println("\n--- Enroll Student in Course ---");
        int studentId = InputUtil.readInt(scanner, "Enter Student ID: ", "Student ID must be an integer.");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        int courseId = InputUtil.readInt(scanner, "Enter Course ID: ", "Course ID must be an integer.");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }

        if (student.getCourses().contains(course)) {
            System.out.println("Student is already enrolled in this course!");
            return;
        }

        student.addCourse(course);
        studentDAO.update(student);
        System.out.println("Student enrolled in course successfully!");
    }

    private static void unenrollStudent(Scanner scanner) {
        System.out.println("\n--- Remove Student from Course ---");
        int studentId = InputUtil.readInt(scanner, "Enter Student ID: ", "Student ID must be an integer.");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        int courseId = InputUtil.readInt(scanner, "Enter Course ID: ", "Course ID must be an integer.");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }

        if (!student.getCourses().contains(course)) {
            System.out.println("Student is not enrolled in this course!");
            return;
        }

        student.removeCourse(course);
        studentDAO.update(student);
        System.out.println("Student removed from course successfully!");
    }

    private static void displayCoursesOfStudent(Scanner scanner) {
        System.out.println("\n--- Display All Courses of a Student ---");
        int studentId = InputUtil.readInt(scanner, "Enter Student ID: ", "Student ID must be an integer.");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println(
                "Student: " + student.getName() + " (ID: " + student.getId() + ", Age: " + student.getAge() + ")");
        if (student.getCourses() == null || student.getCourses().isEmpty()) {
            System.out.println("No courses enrolled.");
        } else {
            System.out.println("Enrolled Courses:");
            for (Course c : student.getCourses()) {
                System.out.println("  - " + c.getTitle() + " (ID: " + c.getId() + ", Credits: " + c.getCredit() + ")");
            }
        }
    }

    private static void displayStudentsOfCourse(Scanner scanner) {
        System.out.println("\n--- Display All Students of a Course ---");
        int courseId = InputUtil.readInt(scanner, "Enter Course ID: ", "Course ID must be an integer.");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }

        System.out.println(
                "Course: " + course.getTitle() + " (ID: " + course.getId() + ", Credits: " + course.getCredit() + ")");
        if (course.getStudents() == null || course.getStudents().isEmpty()) {
            System.out.println("No students enrolled.");
        } else {
            System.out.println("Enrolled Students:");
            for (Student s : course.getStudents()) {
                System.out.println("  - " + s.getName() + " (ID: " + s.getId() + ", Age: " + s.getAge() + ")");
            }
        }
    }

    private static void managementHibernateQuery(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("        HIBERNATE QUERY PRACTICE         ");
            System.out.println("=========================================");
            System.out.println("1. Find students older than age");
            System.out.println("2. List students and enrolled courses");
            System.out.println("3. Find students by name");
            System.out.println("4. Find courses with credit greater than");
            System.out.println("5. Count students enrolled in each course");
            System.out.println("6. Back to Main Menu");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-6): ", 1, 6,
                    "Invalid choice! Please choose a number between 1 and 6.");

            switch (choice) {
                case 1 -> findStudentsOlderThan(scanner);
                case 2 -> findStudentsAndCoursesHqlJoin();
                case 3 -> findByNameNamedQuery(scanner);
                case 4 -> findCoursesWithCreditGreaterThan(scanner);
                case 5 -> countStudentsEnrolledInEachCourse();
                case 6 -> System.out.println("Returning to Main Menu...");
            }
        } while (choice != 6);
    }

    private static void findStudentsOlderThan(Scanner scanner) {
        System.out.println("\n--- HQL Query: Find all students older than age ---");
        int age = InputUtil.readIntInRange(scanner, "Enter Age to search students: ", 0, 120,
                "Age must be between 0 and 120.");
        List<Student> studentsOlder = hibernatePracticeDAO.findStudentsOlderThan(age);
        if (studentsOlder.isEmpty()) {
            System.out.println("No students found older than " + age + ".");
        } else {
            System.out.println("Students older than " + age + ":");
            for (Student s : studentsOlder) {
                System.out.println("  - " + s);
            }
        }
    }

    private static void findStudentsAndCoursesHqlJoin() {
        System.out.println("\n--- HQL with Join: List students and enrolled courses ---");
        List<Object[]> joinResults = hibernatePracticeDAO.findStudentsAndCoursesHqlJoin();
        if (joinResults.isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            System.out.println("Student and Course Enrollments:");
            for (Object[] row : joinResults) {
                String studentName = (String) row[0];
                String courseTitle = (String) row[1];
                System.out.println("  - Student: " + studentName + " | Course: " + courseTitle);
            }
        }
    }

    private static void findByNameNamedQuery(Scanner scanner) {
        System.out.println("\n--- Named Query: Find students by name ---");
        String name = InputUtil.readNonEmptyString(scanner, "Enter Student Name to search: ", "Name cannot be empty.");
        List<Student> studentsByName = hibernatePracticeDAO.findByNameNamedQuery(name);
        if (studentsByName.isEmpty()) {
            System.out.println("No students found with name \"" + name + "\".");
        } else {
            System.out.println("Students found:");
            for (Student s : studentsByName) {
                System.out.println("  - " + s);
            }
        }
    }

    private static void findCoursesWithCreditGreaterThan(Scanner scanner) {
        System.out.println("\n--- Criteria API: Find courses with credit greater than value ---");
        int credit = InputUtil.readIntInRange(scanner, "Enter Credit to filter courses: ", 0, 10,
                "Credit must be between 0 and 10.");
        List<Course> coursesFilter = hibernatePracticeDAO.findCoursesWithCreditGreaterThan(credit);
        if (coursesFilter.isEmpty()) {
            System.out.println("No courses found with credit greater than " + credit + ".");
        } else {
            System.out.println("Courses with credit greater than " + credit + ":");
            for (Course c : coursesFilter) {
                System.out.println("  - " + c);
            }
        }
    }

    private static void countStudentsEnrolledInEachCourse() {
        System.out.println("\n--- Aggregation Query: Count students in each course ---");
        List<Object[]> aggResults = hibernatePracticeDAO.countStudentsEnrolledInEachCourse();
        if (aggResults.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println("Student counts per course:");
            for (Object[] row : aggResults) {
                String courseTitle = (String) row[0];
                Long count = (Long) row[1];
                System.out.println("  - Course: " + courseTitle + " | Enrolled Students: " + count);
            }
        }
    }
}
