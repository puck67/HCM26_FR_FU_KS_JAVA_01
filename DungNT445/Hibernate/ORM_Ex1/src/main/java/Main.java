
import dao.CourseDAO;
import dao.StudentDAO;
import entity.Course;
import entity.Student;
import utils.HibernateUtils;
import utils.InputValidator;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=================================================");
        System.out.println("  KHỞI ĐỘNG HỆ THỐNG QUẢN LÝ ĐÀO TẠO (HIBERNATE) ");
        System.out.println("=================================================");

        // Thử kết nối đến cơ sở dữ liệu để tạo SessionFactory trước khi chạy menu
        try {
            HibernateUtils.getSessionFactory();
            System.out.println("[INFO] Kết nối cơ sở dữ liệu thành công!");
        } catch (Exception e) {
            System.err.println("[FATAL ERROR] Không thể khởi động Hibernate. Vui lòng kiểm tra lại cấu hình DB!");
            e.printStackTrace();
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("\n=================================================");
            System.out.println("        HỆ THỐNG QUẢN LÝ ĐÀO TẠO (HIBERNATE)     ");
            System.out.println("=================================================");
            System.out.println("1. Quản lý Sinh viên");
            System.out.println("2. Quản lý Khóa học");
            System.out.println("3. Chạy chương trình kiểm thử tự động (Task 5 & 6)");
            System.out.println("4. Thoát");
            System.out.println("=================================================");

            int choice = InputValidator.getValidOption(scanner, "Chọn chức năng (1-4): ", 1, 4);

            switch (choice) {
                case 1:
                    runStudentMenu(scanner);
                    break;
                case 2:
                    runCourseMenu(scanner);
                    break;
                case 3:
                    runAutomaticDemo();
                    break;
                case 4:
                    running = false;
                    System.out.println("[INFO] Đang đóng các kết nối cơ sở dữ liệu...");
                    HibernateUtils.shutdown();
                    System.out.println("=================================================");
                    System.out.println("       CẢM ƠN BẠN ĐÃ SỬ DỤNG CHƯƠNG TRÌNH!       ");
                    System.out.println("=================================================");
                    break;
            }
        }
        scanner.close();
    }

    private static void runStudentMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=================================================");
            System.out.println("            QUẢN LÝ SINH VIÊN (CRUD)             ");
            System.out.println("=================================================");
            System.out.println("1. Thêm mới sinh viên");
            System.out.println("2. Hiển thị danh sách sinh viên");
            System.out.println("3. Tìm kiếm sinh viên theo ID");
            System.out.println("4. Cập nhật thông tin sinh viên");
            System.out.println("5. Xóa sinh viên");
            System.out.println("6. Quay lại menu chính");
            System.out.println("=================================================");

            int choice = InputValidator.getValidOption(scanner, "Chọn chức năng (1-6): ", 1, 6);

            switch (choice) {
                case 1:
                    addNewStudent(scanner);
                    break;
                case 2:
                    listAllStudents();
                    break;
                case 3:
                    searchStudentById(scanner);
                    break;
                case 4:
                    updateStudent(scanner);
                    break;
                case 5:
                    deleteStudent(scanner);
                    break;
                case 6:
                    back = true;
                    break;
            }
        }
    }

    private static void runCourseMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=================================================");
            System.out.println("            QUẢN LÝ KHÓA HỌC (CRUD)              ");
            System.out.println("=================================================");
            System.out.println("1. Thêm mới khóa học");
            System.out.println("2. Hiển thị danh sách khóa học");
            System.out.println("3. Tìm kiếm khóa học theo ID");
            System.out.println("4. Cập nhật thông tin khóa học");
            System.out.println("5. Xóa khóa học");
            System.out.println("----------------------------------");
            System.out.println("6. Thêm sinh viên vào khóa học");
            System.out.println("7. Xóa sinh viên khỏi khóa học");
            System.out.println("8. Xem danh sách khóa học của 1 sinh viên");
            System.out.println("9. Xem danh sách sinh viên trong 1 khóa học");
            System.out.println("10. Quay lại menu chính");
            System.out.println("=================================================");

            int choice = InputValidator.getValidOption(scanner, "Chọn chức năng (1-10): ", 1, 10);

            switch (choice) {
                case 1:
                    addNewCourse(scanner);
                    break;
                case 2:
                    listAllCourses();
                    break;
                case 3:
                    searchCourseById(scanner);
                    break;
                case 4:
                    updateCourse(scanner);
                    break;
                case 5:
                    deleteCourse(scanner);
                    break;
                case 6:
                    addStudentToCourse(scanner);
                    break;
                case 7:
                    removeStudentFromCourse(scanner);
                    break;
                case 8:
                    listCoursesOfStudent(scanner);
                    break;
                case 9:
                    listStudentsInCourse(scanner);
                    break;
                case 10:
                    back = true;
                    break;
            }
        }
    }

    // --- CÁC THAO TÁC QUẢN LÝ SINH VIÊN ---

    private static void addNewStudent(Scanner scanner) {
        System.out.println("\n--- THÊM MỚI SINH VIÊN ---");
        String name = InputValidator.getValidName(scanner);
        int age = InputValidator.getValidAge(scanner);

        Student student = new Student(name, age);
        try {
            studentDAO.save(student);
            System.out.println("[SUCCESS] Đã lưu sinh viên thành công!");
        } catch (Exception e) {
            System.err.println("[ERROR] Lưu sinh viên thất bại: " + e.getMessage());
        }
    }

    private static void listAllStudents() {
        System.out.println("\n--- DANH SÁCH SINH VIÊN ---");
        List<Student> students = studentDAO.findAll();
        if (students == null || students.isEmpty()) {
            System.out.println("[INFO] Không có sinh viên nào trong danh sách.");
            return;
        }

        System.out.println("=================================================================================");
        System.out.printf("| %-6s | %-30s | %-6s |\n", "ID", "Họ và Tên", "Tuổi");
        System.out.println("=================================================================================");
        for (Student s : students) {
            System.out.printf("| %-6d | %-30s | %-6d |\n",
                    s.getId(), s.getName(), s.getAge());
        }
        System.out.println("=================================================================================");
    }

    private static void searchStudentById(Scanner scanner) {
        System.out.println("\n--- TÌM KIẾM SINH VIÊN ---");
        int id = InputValidator.getValidId(scanner, "Nhập ID sinh viên cần tìm: ");
        Student student = studentDAO.findById(id);

        if (student == null) {
            System.out.println("[INFO] Không tìm thấy sinh viên có ID: " + id);
        } else {
            System.out.println("Thông tin sinh viên tìm thấy:");
            System.out.println("--------------------------------");
            System.out.println("ID        : " + student.getId());
            System.out.println("Họ và Tên : " + student.getName());
            System.out.println("Tuổi      : " + student.getAge());
            System.out.println("--------------------------------");
        }
    }

    private static void updateStudent(Scanner scanner) {
        System.out.println("\n--- CẬP NHẬT THÔNG TIN SINH VIÊN ---");
        int id = InputValidator.getValidId(scanner, "Nhập ID sinh viên cần cập nhật: ");
        Student student = studentDAO.findById(id);

        if (student == null) {
            System.out.println("[INFO] Không tìm thấy sinh viên có ID: " + id);
            return;
        }

        System.out.println("Thông tin hiện tại:");
        System.out.println("Vui lòng nhập thông tin mới (hoặc ấn Enter để bỏ qua):");

        String newName = InputValidator.getUpdateName(scanner, student.getName());
        int newAge = InputValidator.getUpdateAge(scanner, student.getAge());

        student.setName(newName);
        student.setAge(newAge);
        try {
            studentDAO.update(student);
            System.out.println("[SUCCESS] Cập nhật thông tin sinh viên thành công!");
        } catch (Exception e) {
            System.err.println("[ERROR] Cập nhật thất bại: " + e.getMessage());
        }
    }

    private static void deleteStudent(Scanner scanner) {
        System.out.println("\n--- XÓA SINH VIÊN ---");
        int id = InputValidator.getValidId(scanner, "Nhập ID sinh viên cần xóa: ");
        Student student = studentDAO.findById(id);

        if (student == null) {
            System.out.println("[INFO] Không tìm thấy sinh viên có ID: " + id);
            return;
        }

        System.out.printf("Tìm thấy sinh viên: %s (ID: %d)\n", student.getName(), student.getId());
        System.out.print("Bạn có chắc chắn muốn xóa sinh viên này? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            try {
                studentDAO.delete(id);
                System.out.println("[SUCCESS] Đã xóa sinh viên khỏi hệ thống.");
            } catch (Exception e) {
                System.err.println("[ERROR] Xóa sinh viên thất bại: " + e.getMessage());
            }
        } else {
            System.out.println("[INFO] Đã hủy thao tác xóa.");
        }
    }

    // --- CÁC THAO TÁC QUẢN LÝ KHÓA HỌC ---

    private static void addNewCourse(Scanner scanner) {
        System.out.println("\n--- THÊM MỚI KHÓA HỌC ---");
        String title = InputValidator.getValidCourseTitle(scanner);
        int credit = InputValidator.getValidCourseCredit(scanner);

        Course course = new Course();
        course.setTitle(title);
        course.setCredit(credit);
        try {
            courseDAO.save(course);
            System.out.println("[SUCCESS] Đã lưu khóa học thành công!");
        } catch (Exception e) {
            System.err.println("[ERROR] Lưu khóa học thất bại: " + e.getMessage());
        }
    }

    private static void listAllCourses() {
        System.out.println("\n--- DANH SÁCH KHÓA HỌC ---");
        List<Course> courses = courseDAO.findAll();
        if (courses == null || courses.isEmpty()) {
            System.out.println("[INFO] Không có khóa học nào trong danh sách.");
            return;
        }

        System.out.println("=================================================================================");
        System.out.printf("| %-6s | %-50s | %-12s |\n", "ID", "Tên Khóa Học", "Số Tín Chỉ");
        System.out.println("=================================================================================");
        for (Course c : courses) {
            System.out.printf("| %-6d | %-50s | %-12d |\n",
                    c.getId(), c.getTitle(), c.getCredit());
        }
        System.out.println("=================================================================================");
    }

    private static void searchCourseById(Scanner scanner) {
        System.out.println("\n--- TÌM KIẾM KHÓA HỌC ---");
        int id = InputValidator.getValidId(scanner, "Nhập ID khóa học cần tìm: ");
        Course course = courseDAO.findById(id);

        if (course == null) {
            System.out.println("[INFO] Không tìm thấy khóa học có ID: " + id);
        } else {
            System.out.println("Thông tin khóa học tìm thấy:");
            System.out.println("--------------------------------");
            System.out.println("ID           : " + course.getId());
            System.out.println("Tên Khóa Học : " + course.getTitle());
            System.out.println("Số Tín Chỉ   : " + course.getCredit());
            System.out.println("--------------------------------");
        }
    }

    private static void updateCourse(Scanner scanner) {
        System.out.println("\n--- CẬP NHẬT THÔNG TIN KHÓA HỌC ---");
        int id = InputValidator.getValidId(scanner, "Nhập ID khóa học cần cập nhật: ");
        Course course = courseDAO.findById(id);

        if (course == null) {
            System.out.println("[INFO] Không tìm thấy khóa học có ID: " + id);
            return;
        }

        System.out.println("Thông tin hiện tại:");
        System.out.println("Vui lòng nhập thông tin mới (hoặc ấn Enter để bỏ qua):");

        String newTitle = InputValidator.getUpdateCourseTitle(scanner, course.getTitle());
        int newCredit = InputValidator.getUpdateCourseCredit(scanner, course.getCredit());

        course.setTitle(newTitle);
        course.setCredit(newCredit);
        try {
            courseDAO.update(course);
            System.out.println("[SUCCESS] Cập nhật thông tin khóa học thành công!");
        } catch (Exception e) {
            System.err.println("[ERROR] Cập nhật thất bại: " + e.getMessage());
        }
    }

    private static void deleteCourse(Scanner scanner) {
        System.out.println("\n--- XÓA KHÓA HỌC ---");
        int id = InputValidator.getValidId(scanner, "Nhập ID khóa học cần xóa: ");
        Course course = courseDAO.findById(id);

        if (course == null) {
            System.out.println("[INFO] Không tìm thấy khóa học có ID: " + id);
            return;
        }

        System.out.printf("Tìm thấy khóa học: %s (ID: %d)\n", course.getTitle(), course.getId());
        System.out.print("Bạn có chắc chắn muốn xóa khóa học này? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            try {
                courseDAO.delete(id);
                System.out.println("[SUCCESS] Đã xóa khóa học khỏi hệ thống.");
            } catch (Exception e) {
                System.err.println("[ERROR] Xóa khóa học thất bại: " + e.getMessage());
            }
        } else {
            System.out.println("[INFO] Đã hủy thao tác xóa.");
        }
    }

    private static void addStudentToCourse(Scanner scanner) {
        System.out.println("\n--- THÊM SINH VIÊN VÀO KHÓA HỌC ---");
        int studentId = InputValidator.getValidId(scanner, "Nhập ID sinh viên: ");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            System.out.println("[INFO] Không tìm thấy sinh viên có ID: " + studentId);
            return;
        }

        int courseId = InputValidator.getValidId(scanner, "Nhập ID khóa học: ");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            System.out.println("[INFO] Không tìm thấy khóa học có ID: " + courseId);
            return;
        }

        try {
            studentDAO.enrollStudentInCourse(studentId, courseId);
            System.out.printf("[SUCCESS] Đã đăng ký thành công cho sinh viên '%s' vào khóa học '%s'!\n",
                    student.getName(), course.getTitle());
        } catch (IllegalStateException e) {
            System.out.println("[WARN] " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[ERROR] Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private static void removeStudentFromCourse(Scanner scanner) {
        System.out.println("\n--- XÓA SINH VIÊN KHỎI KHÓA HỌC ---");
        int studentId = InputValidator.getValidId(scanner, "Nhập ID sinh viên: ");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            System.out.println("[INFO] Không tìm thấy sinh viên có ID: " + studentId);
            return;
        }

        int courseId = InputValidator.getValidId(scanner, "Nhập ID khóa học: ");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            System.out.println("[INFO] Không tìm thấy khóa học có ID: " + courseId);
            return;
        }

        System.out.printf("Bạn có chắc chắn muốn xóa sinh viên '%s' khỏi khóa học '%s'? (Y/N): ",
                student.getName(), course.getTitle());
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            try {
                studentDAO.unenrollStudentFromCourse(studentId, courseId);
                System.out.printf("[SUCCESS] Đã xóa sinh viên '%s' khỏi khóa học '%s' thành công!\n",
                        student.getName(), course.getTitle());
            } catch (IllegalStateException e) {
                System.out.println("[WARN] " + e.getMessage());
            } catch (Exception e) {
                System.err.println("[ERROR] Có lỗi xảy ra: " + e.getMessage());
            }
        } else {
            System.out.println("[INFO] Đã hủy thao tác xóa.");
        }
    }

    private static void listCoursesOfStudent(Scanner scanner) {
        System.out.println("\n--- XEM DANH SÁCH KHÓA HỌC CỦA SINH VIÊN ---");
        int studentId = InputValidator.getValidId(scanner, "Nhập ID sinh viên: ");
        Student student = studentDAO.findByIdWithCourses(studentId);

        if (student == null) {
            System.out.println("[INFO] Không tìm thấy sinh viên có ID: " + studentId);
            return;
        }

        System.out.printf("Sinh viên: %s (ID: %d, Tuổi: %d)\n", student.getName(), student.getId(), student.getAge());
        java.util.Set<Course> courses = student.getCourses();
        if (courses == null || courses.isEmpty()) {
            System.out.println("[INFO] Sinh viên này chưa đăng ký khóa học nào.");
            return;
        }

        System.out.println("Các khóa học đã đăng ký:");
        System.out.println("=================================================================================");
        System.out.printf("| %-6s | %-50s | %-12s |\n", "ID", "Tên Khóa Học", "Số Tín Chỉ");
        System.out.println("=================================================================================");
        for (Course c : courses) {
            System.out.printf("| %-6d | %-50s | %-12d |\n", c.getId(), c.getTitle(), c.getCredit());
        }
        System.out.println("=================================================================================");
    }

    private static void listStudentsInCourse(Scanner scanner) {
        System.out.println("\n--- XEM DANH SÁCH SINH VIÊN TRONG KHÓA HỌC ---");
        int courseId = InputValidator.getValidId(scanner, "Nhập ID khóa học: ");
        Course course = courseDAO.findByIdWithStudents(courseId);

        if (course == null) {
            System.out.println("[INFO] Không tìm thấy khóa học có ID: " + courseId);
            return;
        }

        System.out.printf("Khóa học: %s (ID: %d, Số tín chỉ: %d)\n", course.getTitle(), course.getId(), course.getCredit());
        java.util.Set<Student> students = course.getStudents();
        if (students == null || students.isEmpty()) {
            System.out.println("[INFO] Chưa có sinh viên nào đăng ký khóa học này.");
            return;
        }

        System.out.println("Danh sách sinh viên đăng ký:");
        System.out.println("=================================================================================");
        System.out.printf("| %-6s | %-30s | %-6s |\n", "ID", "Họ và Tên", "Tuổi");
        System.out.println("=================================================================================");
        for (Student s : students) {
            System.out.printf("| %-6d | %-30s | %-6d |\n", s.getId(), s.getName(), s.getAge());
        }
        System.out.println("=================================================================================");
    }

    private static void runAutomaticDemo() {
        System.out.println("\n=================================================");
        System.out.println("   CHẠY CHƯƠNG TRÌNH KIỂM THỬ TỰ ĐỘNG (TASK 5 & 6)  ");
        System.out.println("=================================================");

        // --- Step 1: Insert sample data ---
        System.out.println("\n[1] Thêm dữ liệu mẫu (Ít nhất 3 sinh viên và 3 khóa học):");
        
        Student s1 = new Student("Alice Nguyen", 21);
        Student s2 = new Student("Bob Tran", 19);
        Student s3 = new Student("Charlie Le", 22);
        
        studentDAO.save(s1);
        studentDAO.save(s2);
        studentDAO.save(s3);
        System.out.println("-> Đã lưu sinh viên: " + s1.getName() + " (Tuổi " + s1.getAge() + "), " 
                           + s2.getName() + " (Tuổi " + s2.getAge() + "), " 
                           + s3.getName() + " (Tuổi " + s3.getAge() + ")");

        Course c1 = new Course("Lap trinh Java", 4, new java.util.HashSet<>());
        Course c2 = new Course("Co so du lieu", 3, new java.util.HashSet<>());
        Course c3 = new Course("Phat trien Web", 5, new java.util.HashSet<>());
        
        courseDAO.save(c1);
        courseDAO.save(c2);
        courseDAO.save(c3);
        System.out.println("-> Đã lưu khóa học: " + c1.getTitle() + " (Tín chỉ " + c1.getCredit() + "), " 
                           + c2.getTitle() + " (Tín chỉ " + c2.getCredit() + "), " 
                           + c3.getTitle() + " (Tín chỉ " + c3.getCredit() + ")");

        // --- Step 2: Enroll students in courses ---
        System.out.println("\n[2] Đăng ký khóa học cho sinh viên:");
        try {
            studentDAO.enrollStudentInCourse(s1.getId(), c1.getId());
            studentDAO.enrollStudentInCourse(s1.getId(), c3.getId());
            studentDAO.enrollStudentInCourse(s2.getId(), c1.getId());
            studentDAO.enrollStudentInCourse(s2.getId(), c2.getId());
            studentDAO.enrollStudentInCourse(s3.getId(), c3.getId());
            System.out.println("-> Đăng ký thành công!");
        } catch (Exception e) {
            System.err.println("-> Đăng ký thất bại: " + e.getMessage());
        }

        // --- Step 3: Demonstrate CRUD operations ---
        System.out.println("\n[3] Demo các thao tác CRUD:");
        
        // CREATE: (already done above)
        System.out.println("a. CREATE: Đã thêm các sinh viên và khóa học mẫu.");

        // READ:
        System.out.println("b. READ: Tìm sinh viên có ID = " + s1.getId() + ":");
        Student retrieved = studentDAO.findById(s1.getId());
        if (retrieved != null) {
            System.out.println("   Tên: " + retrieved.getName() + ", Tuổi: " + retrieved.getAge());
        }

        // UPDATE:
        System.out.println("c. UPDATE: Cập nhật tuổi của sinh viên Bob Tran từ 19 thành 20.");
        Student bob = studentDAO.findById(s2.getId());
        if (bob != null) {
            bob.setAge(20);
            studentDAO.update(bob);
            System.out.println("   Sau khi cập nhật tuổi của " + bob.getName() + ": " + studentDAO.findById(bob.getId()).getAge());
        }

        // DELETE:
        System.out.println("d. DELETE: Tạo sinh viên tạm thời rồi xóa sinh viên đó.");
        Student tempStudent = new Student("Sinh vien Tam", 25);
        studentDAO.save(tempStudent);
        int tempId = tempStudent.getId();
        System.out.println("   Đã lưu sinh viên tạm thời, ID: " + tempId);
        System.out.println("   Danh sách sinh viên trước khi xóa chứa Sinh vien Tam? " 
                           + studentDAO.findAll().stream().anyMatch(s -> s.getId() == tempId));
        studentDAO.delete(tempId);
        System.out.println("   Danh sách sinh viên sau khi xóa chứa Sinh vien Tam? " 
                           + studentDAO.findAll().stream().anyMatch(s -> s.getId() == tempId));


        // --- Step 4: Execute queries implemented in Task 5 ---
        System.out.println("\n=================================================");
        System.out.println("       KẾT QUẢ TRUY VẤN (TASK 5 QUERIES)        ");
        System.out.println("=================================================");

        // Task 5.1: HQL Query
        System.out.println("\n1. HQL Query: Tìm tất cả sinh viên lớn hơn 20 tuổi:");
        List<Student> olderStudents = studentDAO.findStudentsOlderThan(20);
        if (olderStudents != null) {
            for (Student s : olderStudents) {
                System.out.println("   - ID: " + s.getId() + ", Tên: " + s.getName() + ", Tuổi: " + s.getAge());
            }
        }

        // Task 5.2: HQL with Join
        System.out.println("\n2. HQL with Join: Danh sách sinh viên và các khóa học đăng ký:");
        List<Student> studentsWithCourses = studentDAO.findAllStudentsWithCourses();
        if (studentsWithCourses != null) {
            for (Student s : studentsWithCourses) {
                System.out.print("   - Sinh viên: " + s.getName() + " -> Khóa học: ");
                if (s.getCourses().isEmpty()) {
                    System.out.println("Chưa đăng ký");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (Course c : s.getCourses()) {
                        sb.append(c.getTitle()).append(" (ID: ").append(c.getId()).append("), ");
                    }
                    if (sb.length() > 0) {
                        sb.setLength(sb.length() - 2);
                    }
                    System.out.println(sb.toString());
                }
            }
        }

        // Task 5.3: Named Query
        System.out.println("\n3. Named Query: Tìm sinh viên bằng tên 'Alice Nguyen':");
        List<Student> namedQueryResults = studentDAO.findByNameNamedQuery("Alice Nguyen");
        if (namedQueryResults != null) {
            for (Student s : namedQueryResults) {
                System.out.println("   - ID: " + s.getId() + ", Tên: " + s.getName() + ", Tuổi: " + s.getAge());
            }
        }

        // Task 5.4: Criteria API
        System.out.println("\n4. Criteria API: Tìm các khóa học có tín chỉ lớn hơn 3:");
        List<Course> coursesWithCredit = courseDAO.findCoursesWithCreditGreaterThan(3);
        if (coursesWithCredit != null) {
            for (Course c : coursesWithCredit) {
                System.out.println("   - ID: " + c.getId() + ", Tên: " + c.getTitle() + ", Tín chỉ: " + c.getCredit());
            }
        }

        // Task 5.5: Aggregation Query
        System.out.println("\n5. Aggregation Query: Thống kê số lượng sinh viên đăng ký mỗi khóa học:");
        List<Object[]> counts = courseDAO.countStudentsPerCourse();
        if (counts != null) {
            for (Object[] row : counts) {
                System.out.println("   - Khóa học: " + row[0] + " -> Số sinh viên: " + row[1]);
            }
        }
        System.out.println("=================================================");
        System.out.println("          KẾT THÚC CHƯƠNG TRÌNH KIỂM THỬ         ");
        System.out.println("=================================================");
    }
}