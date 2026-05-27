package fa.service.impl;

import fa.dao.CourseDAO;
import fa.dao.StudentDAO;
import fa.entities.Course;
import fa.entities.Student;
import fa.service.StudentCourseService;
import java.util.List;

public class StudentCourseServiceImpl implements StudentCourseService {

    private final StudentDAO studentDAO = new StudentDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    public void createStudent(Student student) {
        studentDAO.save(student);
    }

    @Override
    public void updateStudent(Student student) {
        studentDAO.update(student);
    }

    @Override
    public boolean deleteStudent(int id) {
        return studentDAO.delete(id);
    }

    @Override
    public Student findStudentById(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void createCourse(Course course) {
        courseDAO.save(course);
    }

    @Override
    public void updateCourse(Course course) {
        courseDAO.update(course);
    }

    @Override
    public boolean deleteCourse(int id) {
        return courseDAO.delete(id);
    }

    @Override
    public Course findCourseById(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public boolean enroll(int studentId, int courseId) {
        return studentDAO.enrollStudent(studentId, courseId);
    }

    @Override
    public boolean unenroll(int studentId, int courseId) {
        return studentDAO.removeStudent(studentId, courseId);
    }

    @Override
    public List<Course> getCoursesOfStudent(int studentId) {
        return studentDAO.getCoursesOfStudent(studentId);
    }

    @Override
    public List<Student> getStudentsOfCourse(int courseId) {
        return courseDAO.getStudentsOfCourse(courseId);
    }

    @Override
    public List<Student> getStudentsOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Student> getStudentsWithCourses() {
        return studentDAO.findStudentsWithCourses();
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public List<Course> getCoursesWithCreditGreaterThan(int creditLimit) {
        return courseDAO.findCoursesWithCreditGreaterThan(creditLimit);
    }

    @Override
    public List<Object[]> getStudentCountPerCourse() {
        return courseDAO.getStudentCountPerCourse();
    }

    @Override
    public List<Student> getStudentsPaged(int offset, int limit) {
        return studentDAO.findAllPaged(offset, limit);
    }

    @Override
    public List<Student> getStudentsWithoutCourses() {
        return studentDAO.findStudentsWithoutCourses();
    }

    @Override
    public void executeAutomatedDemo() {
        if (!getAllStudents().isEmpty() || !getAllCourses().isEmpty()) {
            System.out.println("\n[INFO] Database already contains existing records from a previous run.");
            System.out.println("Skipping automated sample data insertion to avoid duplicate records.");
            System.out.println("Existing Students in DB:");
            getAllStudents().forEach(s -> System.out.println(" - " + s));
            System.out.println("Existing Courses in DB:");
            getAllCourses().forEach(c -> System.out.println(" - " + c));
            return;
        }

        System.out.println("\n--- TASK 6.1: Inserting Sample Data ---");
        
        // At least 3 students
        Student s1 = new Student("John", 20);
        Student s2 = new Student("Anna", 22);
        Student s3 = new Student("Bob", 19);
        Student s4 = new Student("Charlie", 25); // For "no course enrolled" check
        
        createStudent(s1);
        createStudent(s2);
        createStudent(s3);
        createStudent(s4);
        
        // At least 3 courses
        Course c1 = new Course("Java Programming", 4);
        Course c2 = new Course("Database Systems", 3);
        Course c3 = new Course("Web Development", 3);
        
        createCourse(c1);
        createCourse(c2);
        createCourse(c3);

        System.out.println("Sample students and courses saved successfully!");

        System.out.println("\n--- TASK 6.2: Enrolling Students in Courses ---");
        
        enroll(s1.getId(), c1.getId()); // John -> Java Programming
        enroll(s1.getId(), c2.getId()); // John -> Database Systems
        
        enroll(s2.getId(), c2.getId()); // Anna -> Database Systems
        enroll(s2.getId(), c3.getId()); // Anna -> Web Dev
        
        enroll(s3.getId(), c1.getId()); // Bob -> Java Programming
        enroll(s3.getId(), c2.getId()); // Bob -> Database Systems
        enroll(s3.getId(), c3.getId()); // Bob -> Web Dev

        System.out.println("Enrollments created successfully!");

        System.out.println("\n--- TASK 6.3: Demonstrating CRUD Operations ---");
        
        // Create new Student
        Student tempStudent = new Student("David", 21);
        createStudent(tempStudent);
        StringBuilder sbCrud1 = new StringBuilder();
        sbCrud1.append("1. Created Student: ").append(tempStudent).append("\n");
        System.out.print(sbCrud1.toString());

        // Get by ID
        Student fetched = findStudentById(tempStudent.getId());
        StringBuilder sbCrud2 = new StringBuilder();
        sbCrud2.append("2. Retrieved Student by ID: ").append(fetched).append("\n");
        System.out.print(sbCrud2.toString());

        // Update
        fetched.setAge(23);
        updateStudent(fetched);
        Student updated = findStudentById(tempStudent.getId());
        StringBuilder sbCrud3 = new StringBuilder();
        sbCrud3.append("3. Updated Student Age to 23: ").append(updated).append("\n");
        System.out.print(sbCrud3.toString());

        // Delete
        deleteStudent(tempStudent.getId());
        Student deleted = findStudentById(tempStudent.getId());
        StringBuilder sbCrud4 = new StringBuilder();
        sbCrud4.append("4. Deleted Student. Retrieval check: ").append(deleted).append(" (Should be null)\n");
        System.out.print(sbCrud4.toString());

        System.out.println("\n--- TASK 6.4: Executing Query Practice (Task 5) ---");

        // 1. HQL: Students older than 20
        List<Student> olderThan20 = getStudentsOlderThan(20);
        StringBuilder sbQ1 = new StringBuilder();
        sbQ1.append("- Students older than 20:\n");
        olderThan20.forEach(s -> sbQ1.append("  ").append(s).append("\n"));
        System.out.print(sbQ1.toString());

        // 2. HQL with Join: List students and enrolled courses
        List<Student> studentsWithCourses = getStudentsWithCourses();
        StringBuilder sbQ2 = new StringBuilder();
        sbQ2.append("- Students and their enrolled courses (HQL with Join):\n");
        studentsWithCourses.forEach(student -> {
            sbQ2.append("  Student: ").append(student.getName()).append(" enrolled in: ");
            if (student.getCourses().isEmpty()) {
                sbQ2.append("None");
            } else {
                student.getCourses().forEach(c -> sbQ2.append(c.getTitle()).append("; "));
            }
            sbQ2.append("\n");
        });
        System.out.print(sbQ2.toString());

        // 3. Named Query: Find students by name 'Anna'
        List<Student> searchAnna = findStudentsByName("Anna");
        StringBuilder sbQ3 = new StringBuilder();
        sbQ3.append("- Named Query search for 'Anna':\n");
        searchAnna.forEach(s -> sbQ3.append("  ").append(s).append("\n"));
        System.out.print(sbQ3.toString());

        // 4. Criteria API: Find courses with credit greater than 3
        List<Course> coursesGreaterThan3 = getCoursesWithCreditGreaterThan(3);
        StringBuilder sbQ4 = new StringBuilder();
        sbQ4.append("- Courses with credit greater than 3 (Criteria API):\n");
        coursesGreaterThan3.forEach(c -> sbQ4.append("  ").append(c).append("\n"));
        System.out.print(sbQ4.toString());

        // 5. Aggregation Query: Count students per course
        List<Object[]> studentCountPerCourse = getStudentCountPerCourse();
        StringBuilder sbQ5 = new StringBuilder();
        sbQ5.append("- Student count per course (Aggregation Query):\n");
        studentCountPerCourse.forEach(record -> 
            sbQ5.append("  ").append(record[0]).append(": ").append(record[1]).append("\n")
        );
        System.out.print(sbQ5.toString());

        System.out.println("\n--- BONUS DEMONSTRATION ---");

        // Bonus 1: Pagination for listing students
        int limit = 2;
        StringBuilder sbBonus1 = new StringBuilder();
        sbBonus1.append("- Pagination Demo (Page Size = 2):\n");
        
        List<Student> page1 = getStudentsPaged(0, limit);
        sbBonus1.append("  Page 1 (offset 0):\n");
        page1.forEach(s -> sbBonus1.append("    ").append(s).append("\n"));
        
        List<Student> page2 = getStudentsPaged(2, limit);
        sbBonus1.append("  Page 2 (offset 2):\n");
        page2.forEach(s -> sbBonus1.append("    ").append(s).append("\n"));
        
        System.out.print(sbBonus1.toString());

        // Bonus 2: Students who are not enrolled in any course
        List<Student> uninrolled = getStudentsWithoutCourses();
        StringBuilder sbBonus2 = new StringBuilder();
        sbBonus2.append("- Students not enrolled in any course (Bonus):\n");
        uninrolled.forEach(s -> sbBonus2.append("  ").append(s).append("\n"));
        System.out.print(sbBonus2.toString());
    }

    @Override
    public void addStudentInteractive() {
        String name = fa.util.Validations.getStringWithRegex(
            "Enter Student Name: ", 
            "^[a-zA-ZÀ-ỹ\\s]{2,50}$", 
            "Error: Name must contain only letters and spaces, between 2 and 50 characters!"
        );
        int age = fa.util.Validations.getPositiveInteger("Enter Student Age: ", "Error: Age must be a positive integer!");
        createStudent(new Student(name, age));
        System.out.println("Student created successfully!");
    }

    @Override
    public void displayAllStudentsInteractive() {
        System.out.println("\n--- List of Students ---");
        getAllStudents().forEach(s -> System.out.println(" - " + s));
    }

    @Override
    public void updateStudentInteractive() {
        int id = fa.util.Validations.getInteger("Enter Student ID to update: ", "Error: ID must be an integer!");
        Student s = findStudentById(id);
        if (s == null) {
            System.out.println("Error: Student ID not found in database!");
            return;
        }
        String name = fa.util.Validations.getStringWithRegex(
            "Enter new Name (" + s.getName() + "): ", 
            "^[a-zA-ZÀ-ỹ\\s]{2,50}$", 
            "Error: Name must contain only letters and spaces, between 2 and 50 characters!"
        );
        int age = fa.util.Validations.getPositiveInteger("Enter new Age (" + s.getAge() + "): ", "Error: Age must be an integer!");
        s.setName(name);
        s.setAge(age);
        updateStudent(s);
        System.out.println("Student updated successfully!");
    }

    @Override
    public void deleteStudentInteractive() {
        int id = fa.util.Validations.getInteger("Enter Student ID to delete: ", "Error: ID must be an integer!");
        boolean deleted = deleteStudent(id);
        if (deleted) {
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Error: Student ID not found in database!");
        }
    }

    @Override
    public void addCourseInteractive() {
        String title = fa.util.Validations.getStringWithRegex(
            "Enter Course Title: ", 
            "^[a-zA-Z0-9\\s+#.-]{2,100}$", 
            "Error: Title must contain letters/numbers/spaces/symbols (+, #, -, .), between 2 and 100 characters!"
        );
        int credit = fa.util.Validations.getPositiveInteger("Enter Course Credit: ", "Error: Credit must be an integer!");
        createCourse(new Course(title, credit));
        System.out.println("Course created successfully!");
    }

    @Override
    public void displayAllCoursesInteractive() {
        System.out.println("\n--- List of Courses ---");
        getAllCourses().forEach(c -> System.out.println(" - " + c));
    }

    @Override
    public void updateCourseInteractive() {
        int id = fa.util.Validations.getInteger("Enter Course ID to update: ", "Error: ID must be an integer!");
        Course c = findCourseById(id);
        if (c == null) {
            System.out.println("Error: Course ID not found in database!");
            return;
        }
        String title = fa.util.Validations.getStringWithRegex(
            "Enter new Title (" + c.getTitle() + "): ", 
            "^[a-zA-Z0-9\\s+#.-]{2,100}$", 
            "Error: Title must contain letters/numbers/spaces/symbols (+, #, -, .), between 2 and 100 characters!"
        );
        int credit = fa.util.Validations.getPositiveInteger("Enter new Credit (" + c.getCredit() + "): ", "Error: Credit must be an integer!");
        c.setTitle(title);
        c.setCredit(credit);
        updateCourse(c);
        System.out.println("Course updated successfully!");
    }

    @Override
    public void deleteCourseInteractive() {
        int id = fa.util.Validations.getInteger("Enter Course ID to delete: ", "Error: ID must be an integer!");
        boolean deleted = deleteCourse(id);
        if (deleted) {
            System.out.println("Course deleted successfully!");
        } else {
            System.out.println("Error: Course ID not found in database!");
        }
    }

    @Override
    public void enrollInteractive() {
        int studentId = fa.util.Validations.getInteger("Enter Student ID: ", "Error: ID must be an integer!");
        int courseId = fa.util.Validations.getInteger("Enter Course ID: ", "Error: ID must be an integer!");
        boolean success = enroll(studentId, courseId);
        if (success) {
            System.out.println("Enrollment executed successfully!");
        } else {
            System.out.println("Error: Student ID or Course ID not found in database!");
        }
    }

    @Override
    public void unenrollInteractive() {
        int studentId = fa.util.Validations.getInteger("Enter Student ID: ", "Error: ID must be an integer!");
        int courseId = fa.util.Validations.getInteger("Enter Course ID: ", "Error: ID must be an integer!");
        boolean success = unenroll(studentId, courseId);
        if (success) {
            System.out.println("Unenrollment executed successfully!");
        } else {
            System.out.println("Error: Student ID or Course ID not found, or they are not enrolled!");
        }
    }

    @Override
    public void displayCoursesOfStudentInteractive() {
        int studentId = fa.util.Validations.getInteger("Enter Student ID: ", "Error: ID must be an integer!");
        List<Course> courses = getCoursesOfStudent(studentId);
        StringBuilder sb = new StringBuilder();
        sb.append("Courses of Student ID ").append(studentId).append(":\n");
        courses.forEach(c -> sb.append(" - ").append(c.getTitle()).append(" (Credits: ").append(c.getCredit()).append(")\n"));
        System.out.print(sb.toString());
    }

    @Override
    public void displayStudentsOfCourseInteractive() {
        int courseId = fa.util.Validations.getInteger("Enter Course ID: ", "Error: ID must be an integer!");
        List<Student> students = getStudentsOfCourse(courseId);
        StringBuilder sb = new StringBuilder();
        sb.append("Students enrolled in Course ID ").append(courseId).append(":\n");
        students.forEach(s -> sb.append(" - ").append(s.getName()).append(" (Age: ").append(s.getAge()).append(")\n"));
        System.out.print(sb.toString());
    }

    @Override
    public void findStudentsOlderThanInteractive() {
        int age = fa.util.Validations.getInteger("Enter age limit: ", "Error: Age must be a valid integer!");
        List<Student> results = getStudentsOlderThan(age);
        StringBuilder sb = new StringBuilder();
        sb.append("Students older than ").append(age).append(":\n");
        results.forEach(s -> sb.append(" - ").append(s).append("\n"));
        System.out.print(sb.toString());
    }

    @Override
    public void searchStudentByNameInteractive() {
        String name = fa.util.Validations.getStringWithRegex(
            "Enter Student Name to search: ", 
            "^[a-zA-ZÀ-ỹ\\s]{1,50}$", 
            "Error: Search query must contain only letters and spaces, maximum 50 characters!"
        );
        List<Student> results = findStudentsByName(name);
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(results.size()).append(" student(s):\n");
        results.forEach(s -> sb.append(" - ").append(s).append("\n"));
        System.out.print(sb.toString());
    }

    @Override
    public void findCoursesWithCreditGreaterThanInteractive() {
        int credit = fa.util.Validations.getInteger("Enter Credit limit: ", "Error: Credit must be an integer!");
        List<Course> results = getCoursesWithCreditGreaterThan(credit);
        StringBuilder sb = new StringBuilder();
        sb.append("Courses with credit > ").append(credit).append(":\n");
        results.forEach(c -> sb.append(" - ").append(c).append("\n"));
        System.out.print(sb.toString());
    }

    @Override
    public void showStudentCountPerCourseInteractive() {
        List<Object[]> counts = getStudentCountPerCourse();
        StringBuilder sb = new StringBuilder();
        sb.append("Student count per course (Aggregation Query):\n");
        counts.forEach(record -> sb.append(" - ").append(record[0]).append(": ").append(record[1]).append(" student(s)\n"));
        System.out.print(sb.toString());
    }

    @Override
    public void findStudentsWithNoEnrollmentsInteractive() {
        List<Student> results = getStudentsWithoutCourses();
        StringBuilder sb = new StringBuilder();
        sb.append("Students not enrolled in any course (Bonus):\n");
        results.forEach(s -> sb.append(" - ").append(s).append("\n"));
        System.out.print(sb.toString());
    }

    @Override
    public void viewStudentsWithPaginationInteractive() {
        int offset = fa.util.Validations.getInteger("Enter offset (start index): ", "Error: Offset must be an integer!");
        int limit = fa.util.Validations.getInteger("Enter page limit (page size): ", "Error: Page limit must be an integer!");
        List<Student> results = getStudentsPaged(offset, limit);
        StringBuilder sb = new StringBuilder();
        sb.append("Paginated Student List (offset ").append(offset).append(", limit ").append(limit).append("):\n");
        results.forEach(s -> sb.append(" - ").append(s).append("\n"));
        System.out.print(sb.toString());
    }
}
