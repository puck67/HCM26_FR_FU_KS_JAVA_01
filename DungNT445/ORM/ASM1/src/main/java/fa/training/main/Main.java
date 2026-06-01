package fa.training.main;

import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.service.CourseService;
import fa.training.service.StudentService;
import fa.training.service.impl.CourseServiceImpl;
import fa.training.service.impl.StudentServiceImpl;
import fa.training.util.HibernateUtil;
import fa.training.util.Validator;
import java.util.List;

public class Main {
    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();

    public static void main(String[] args) {
        System.out.println("🚀 Khởi động ứng dụng Quản lý đào tạo (ASM1)...");
        try {
            // Tự động nạp dữ liệu mẫu nếu database trống để dễ test
            initializeMockData();

            boolean run = true;
            while (run) {
                System.out.println("\n================= MAIN MENU =================");
                System.out.println("1. Quản lý sinh viên (Student Management)");
                System.out.println("2. Quản lý môn học (Course Management)");
                System.out.println("3. Quản lý đăng ký học (Enrollment Management)");
                System.out.println("4. Truy vấn nâng cao & Báo cáo (Queries and Reports)");
                System.out.println("5. Thoát chương trình (Exit)");
                System.out.println("=============================================");

                int choice = Validator.readInt("Nhập lựa chọn của bạn (1-5): ", 1, 5);
                switch (choice) {
                    case 1 -> studentMenu();
                    case 2 -> courseMenu();
                    case 3 -> enrollmentMenu();
                    case 4 -> reportsMenu();
                    case 5 -> {
                        System.out.println("👋 Đang đóng kết nối cơ sở dữ liệu và thoát ứng dụng. Tạm biệt!");
                        HibernateUtil.shutdown();
                        run = false;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Hệ thống gặp sự cố nghiêm trọng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 1. Student Management Menu
    private static void studentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Create a new student");
            System.out.println("2. Update student information");
            System.out.println("3. Delete a student");
            System.out.println("4. View student by ID");
            System.out.println("5. List all students");
            System.out.println("6. List students paginated (Bonus)");
            System.out.println("7. List students not enrolled (Bonus)");
            System.out.println("8. Back to main menu");

            int choice = Validator.readInt("Select an option (1-8): ", 1, 8);
            switch (choice) {
                case 1 -> {
                    String name = Validator.readString("Enter name: ", false);
                    int age = Validator.readInt("Enter age (10-100): ", 10, 100);
                    studentService.createStudent(name, age);
                    System.out.println("✅ Student created successfully.");
                }
                case 2 -> {
                    int id = Validator.readInt("Enter student ID to update: ", 1, Integer.MAX_VALUE);
                    Student s = studentService.getStudentById(id);
                    if (s == null) {
                        System.out.println("❌ Student not found with ID: " + id);
                    } else {
                        String name = Validator.readString("Enter new name: ", false);
                        int age = Validator.readInt("Enter new age (10-100): ", 10, 100);
                        studentService.updateStudent(id, name, age);
                        System.out.println("✅ Student updated successfully.");
                    }
                }
                case 3 -> {
                    int id = Validator.readInt("Enter student ID to delete: ", 1, Integer.MAX_VALUE);
                    Student s = studentService.getStudentById(id);
                    if (s == null) {
                        System.out.println("❌ Student not found with ID: " + id);
                    } else {
                        studentService.deleteStudent(id);
                        System.out.println("✅ Student deleted successfully.");
                    }
                }
                case 4 -> {
                    int id = Validator.readInt("Enter student ID: ", 1, Integer.MAX_VALUE);
                    Student s = studentService.getStudentById(id);
                    if (s != null) {
                        System.out.println(s);
                    } else {
                        System.out.println("❌ Student not found with ID: " + id);
                    }
                }
                case 5 -> {
                    System.out.println("--- All Students ---");
                    List<Student> list = studentService.getAllStudents();
                    if (list.isEmpty()) {
                        System.out.println("📭 No students found.");
                    } else {
                        list.forEach(System.out::println);
                    }
                }
                case 6 -> {
                    int page = Validator.readInt("Enter page number (1+): ", 1, Integer.MAX_VALUE);
                    int size = Validator.readInt("Enter page size (1-100): ", 1, 100);
                    System.out.printf("--- Students Page %d (Size: %d) ---\n", page, size);
                    List<Student> paginated = studentService.findStudentsPaginated(page, size);
                    if (paginated.isEmpty()) {
                        System.out.println("📭 No students found on this page.");
                    } else {
                        paginated.forEach(System.out::println);
                    }
                }
                case 7 -> {
                    System.out.println("--- Students Not Enrolled In Any Course ---");
                    List<Student> notEnrolled = studentService.findStudentsNotEnrolled();
                    if (notEnrolled.isEmpty()) {
                        System.out.println("📭 All students are enrolled in at least one course.");
                    } else {
                        notEnrolled.forEach(System.out::println);
                    }
                }
                case 8 -> back = true;
            }
        }
    }

    // 2. Course Management Menu
    private static void courseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Create a new course");
            System.out.println("2. Update course information");
            System.out.println("3. Delete a course");
            System.out.println("4. View course by ID");
            System.out.println("5. List all courses");
            System.out.println("6. Back to main menu");

            int choice = Validator.readInt("Select an option (1-6): ", 1, 6);
            switch (choice) {
                case 1 -> {
                    String title = Validator.readString("Enter title: ", false);
                    int credit = Validator.readInt("Enter credit (1-10): ", 1, 10);
                    courseService.createCourse(title, credit);
                    System.out.println("✅ Course created successfully.");
                }
                case 2 -> {
                    int id = Validator.readInt("Enter course ID to update: ", 1, Integer.MAX_VALUE);
                    Course c = courseService.getCourseById(id);
                    if (c == null) {
                        System.out.println("❌ Course not found with ID: " + id);
                    } else {
                        String title = Validator.readString("Enter new title: ", false);
                        int credit = Validator.readInt("Enter new credit (1-10): ", 1, 10);
                        courseService.updateCourse(id, title, credit);
                        System.out.println("✅ Course updated successfully.");
                    }
                }
                case 3 -> {
                    int id = Validator.readInt("Enter course ID to delete: ", 1, Integer.MAX_VALUE);
                    Course c = courseService.getCourseById(id);
                    if (c == null) {
                        System.out.println("❌ Course not found with ID: " + id);
                    } else {
                        courseService.deleteCourse(id);
                        System.out.println("✅ Course deleted successfully.");
                    }
                }
                case 4 -> {
                    int id = Validator.readInt("Enter course ID: ", 1, Integer.MAX_VALUE);
                    Course c = courseService.getCourseById(id);
                    if (c != null) {
                        System.out.println(c);
                    } else {
                        System.out.println("❌ Course not found with ID: " + id);
                    }
                }
                case 5 -> {
                    System.out.println("--- All Courses ---");
                    List<Course> list = courseService.getAllCourses();
                    if (list.isEmpty()) {
                        System.out.println("📭 No courses found.");
                    } else {
                        list.forEach(System.out::println);
                    }
                }
                case 6 -> back = true;
            }
        }
    }

    // 3. Enrollment Management Menu
    private static void enrollmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Enrollment Management ---");
            System.out.println("1. Enroll a student in a course");
            System.out.println("2. Remove a student from a course");
            System.out.println("3. View courses of a student");
            System.out.println("4. View students of a course");
            System.out.println("5. Back to main menu");

            int choice = Validator.readInt("Select an option (1-5): ", 1, 5);
            switch (choice) {
                case 1 -> {
                    int sId = Validator.readInt("Enter Student ID: ", 1, Integer.MAX_VALUE);
                    int cId = Validator.readInt("Enter Course ID: ", 1, Integer.MAX_VALUE);
                    studentService.enrollStudentInCourse(sId, cId);
                    System.out.println("✅ Student enrolled in course successfully.");
                }
                case 2 -> {
                    int sId = Validator.readInt("Enter Student ID: ", 1, Integer.MAX_VALUE);
                    int cId = Validator.readInt("Enter Course ID: ", 1, Integer.MAX_VALUE);
                    studentService.removeStudentFromCourse(sId, cId);
                    System.out.println("✅ Student removed from course successfully.");
                }
                case 3 -> {
                    int sId = Validator.readInt("Enter Student ID: ", 1, Integer.MAX_VALUE);
                    List<Course> list = studentService.getCoursesOfStudent(sId);
                    if (list.isEmpty()) {
                        System.out.println("📭 Student has no course enrollments.");
                    } else {
                        System.out.printf("--- Courses of Student ID: %d ---\n", sId);
                        list.forEach(System.out::println);
                    }
                }
                case 4 -> {
                    int cId = Validator.readInt("Enter Course ID: ", 1, Integer.MAX_VALUE);
                    List<Student> list = courseService.getStudentsOfCourse(cId);
                    if (list.isEmpty()) {
                        System.out.println("📭 No students enrolled in this course.");
                    } else {
                        System.out.printf("--- Students in Course ID: %d ---\n", cId);
                        list.forEach(System.out::println);
                    }
                }
                case 5 -> back = true;
            }
        }
    }

    // 4. Queries and Reports Menu
    private static void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Queries and Reports ---");
            System.out.println("1. Find students older than age (HQL)");
            System.out.println("2. Find students by name keyword (Named Query)");
            System.out.println("3. List all students and their courses (HQL Join Query)");
            System.out.println("4. Find courses with credit greater than value (Criteria API)");
            System.out.println("5. Count number of students in each course (Aggregation Query)");
            System.out.println("6. Find students enrolled in a specific course (HQL Parameterized)");
            System.out.println("7. Back to main menu");

            int choice = Validator.readInt("Select an option (1-7): ", 1, 7);
            switch (choice) {
                case 1 -> {
                    int age = Validator.readInt("Enter age threshold: ", 0, 100);
                    List<Student> list = studentService.findOlderThan(age);
                    if (list.isEmpty()) {
                        System.out.println("📭 No students older than " + age + " found.");
                    } else {
                        list.forEach(System.out::println);
                    }
                }
                case 2 -> {
                    String keyword = Validator.readString("Enter name keyword: ", false);
                    List<Student> list = studentService.findByName(keyword);
                    if (list.isEmpty()) {
                        System.out.println("📭 No students matching '" + keyword + "' found.");
                    } else {
                        list.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    System.out.println("--- Students and their Courses ---");
                    List<Object[]> results = studentService.findAllStudentsWithCourses();
                    if (results.isEmpty()) {
                        System.out.println("📭 No student course registrations found.");
                    } else {
                        for (Object[] row : results) {
                            System.out.printf("Student: %s | Course: %s\n", row[0], row[1]);
                        }
                    }
                }
                case 4 -> {
                    int minCredit = Validator.readInt("Enter credit threshold: ", 0, 10);
                    List<Course> list = courseService.findCoursesWithCreditGreaterThan(minCredit);
                    if (list.isEmpty()) {
                        System.out.println("📭 No courses with credit greater than " + minCredit + " found.");
                    } else {
                        list.forEach(System.out::println);
                    }
                }
                case 5 -> {
                    System.out.println("--- Student Enrollment Count Per Course ---");
                    List<Object[]> list = courseService.countStudentsOfCourse();
                    if (list.isEmpty()) {
                        System.out.println("📭 No courses found.");
                    } else {
                        for (Object[] row : list) {
                            System.out.printf("Course: %-25s | Students enrolled: %s\n", row[0], row[1]);
                        }
                    }
                }
                case 6 -> {
                    int cId = Validator.readInt("Enter Course ID: ", 1, Integer.MAX_VALUE);
                    List<Student> list = studentService.findStudentsEnrolledInCourse(cId);
                    if (list.isEmpty()) {
                        System.out.println("📭 No students enrolled in this course.");
                    } else {
                        System.out.printf("--- Students Enrolled in Course ID %d ---\n", cId);
                        list.forEach(System.out::println);
                    }
                }
                case 7 -> back = true;
            }
        }
    }

    // Populate seed data on first run
    private static void initializeMockData() {
        if (studentService.getAllStudents().isEmpty() && courseService.getAllCourses().isEmpty()) {
            System.out.println("📝 Database trống. Đang nạp dữ liệu mẫu...");

            // 1. Tạo 10 khóa học mẫu
            courseService.createCourse("Mathematics", 4);
            courseService.createCourse("Physics", 4);
            courseService.createCourse("Chemistry", 3);
            courseService.createCourse("Biology", 3);
            courseService.createCourse("History", 2);
            courseService.createCourse("Literature", 2);
            courseService.createCourse("Computer Science", 4);
            courseService.createCourse("Database Systems", 3);
            courseService.createCourse("Software Engineering", 4);
            courseService.createCourse("Network Security", 3);

            // 2. Tạo 10 sinh viên mẫu
            studentService.createStudent("Alice Nguyen", 20);
            studentService.createStudent("Bob Tran", 21);
            studentService.createStudent("Charlie Le", 22);
            studentService.createStudent("David Pham", 23);
            studentService.createStudent("Eva Vu", 20);
            studentService.createStudent("Frank Hoang", 21);
            studentService.createStudent("Grace Do", 22);
            studentService.createStudent("Hannah Ngo", 19);
            studentService.createStudent("Ian Dinh", 24);
            studentService.createStudent("Julia Lam", 23);

            // Lấy danh sách để enroll
            List<Student> students = studentService.getAllStudents();
            List<Course> courses = courseService.getAllCourses();

            // 3. Đăng ký một số sinh viên vào khóa học mẫu
            if (students.size() >= 10 && courses.size() >= 10) {
                // Alice -> Math, CS, Database
                studentService.enrollStudentInCourse(students.get(0).getId(), courses.get(0).getId());
                studentService.enrollStudentInCourse(students.get(0).getId(), courses.get(6).getId());
                studentService.enrollStudentInCourse(students.get(0).getId(), courses.get(7).getId());

                // Bob -> Math, Physics
                studentService.enrollStudentInCourse(students.get(1).getId(), courses.get(0).getId());
                studentService.enrollStudentInCourse(students.get(1).getId(), courses.get(1).getId());

                // Charlie -> CS, SE, Network
                studentService.enrollStudentInCourse(students.get(2).getId(), courses.get(6).getId());
                studentService.enrollStudentInCourse(students.get(2).getId(), courses.get(8).getId());
                studentService.enrollStudentInCourse(students.get(2).getId(), courses.get(9).getId());

                // David -> Biology, Chemistry
                studentService.enrollStudentInCourse(students.get(3).getId(), courses.get(2).getId());
                studentService.enrollStudentInCourse(students.get(3).getId(), courses.get(3).getId());

                // Eva -> History, Literature
                studentService.enrollStudentInCourse(students.get(4).getId(), courses.get(4).getId());
                studentService.enrollStudentInCourse(students.get(4).getId(), courses.get(5).getId());

                // Frank -> Math, CS
                studentService.enrollStudentInCourse(students.get(5).getId(), courses.get(0).getId());
                studentService.enrollStudentInCourse(students.get(5).getId(), courses.get(6).getId());

                // Grace -> SE, Database
                studentService.enrollStudentInCourse(students.get(6).getId(), courses.get(8).getId());
                studentService.enrollStudentInCourse(students.get(6).getId(), courses.get(7).getId());

                // Hannah -> Literature (mới đăng ký 1 môn)
                studentService.enrollStudentInCourse(students.get(7).getId(), courses.get(5).getId());

                // Ian & Julia: không đăng ký môn nào (để test chức năng sinh viên chưa enroll môn nào)
            }
            System.out.println("✅ Nạp dữ liệu mẫu hoàn tất!");
        }
    }
}
