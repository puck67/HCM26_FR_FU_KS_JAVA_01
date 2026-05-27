package org.example;

import org.example.dao.CourseDAO;
import org.example.dao.StudentDAO;
import org.example.model.Course;
import org.example.model.Student;
import org.example.util.HibernateUtils;

import java.util.List;
import java.util.Scanner;

/**
 * Task 6: Test Program
 * Nâng cấp giao diện CLI tương tác sử dụng StringBuilder
 * và các biểu thức Lambda / Stream API để tối ưu hóa xử lý chuỗi.
 */
public class Main {

    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO  courseDAO  = new CourseDAO();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            StringBuilder menuBuilder = new StringBuilder();
            menuBuilder.append("============================================================\n")
                       .append("           HỆ THỐNG QUẢN LÝ ĐÀO TẠ (HIBERNATE)\n")
                       .append("============================================================\n")
                       .append("1. Quản lý Sinh viên\n")
                       .append("2. Quản lý Khóa học\n")
                       .append("3. Chạy chương trình kiểm thử tự động (Task 5 & 6)\n")
                       .append("4. Thoát\n")
                       .append("============================================================");
            System.out.println(menuBuilder.toString());
            int choice = readInt(scanner, "Chọn chức năng (1-4): ");

            switch (choice) {
                case 1:
                    studentMenu(scanner);
                    break;
                case 2:
                    courseMenu(scanner);
                    break;
                case 3:
                    runAutomaticTests();
                    break;
                case 4:
                    StringBuilder exitMsg = new StringBuilder();
                    exitMsg.append("\nĐang đóng kết nối cơ sở dữ liệu và thoát...\n");
                    System.out.print(exitMsg.toString());
                    HibernateUtils.shutdown();
                    System.out.println("Đã thoát chương trình. Tạm biệt!");
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 1 đến 4.");
            }
            System.out.println();
        }
        scanner.close();
    }

    // =====================================================================
    // SUB-MENU: QUẢN LÝ SINH VIÊN
    // =====================================================================
    private static void studentMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            StringBuilder subMenuBuilder = new StringBuilder();
            subMenuBuilder.append("\n============================================================\n")
                          .append("                     QUẢN LÝ SINH VIÊN                      \n")
                          .append("============================================================\n")
                          .append("1. Xem danh sách sinh viên\n")
                          .append("2. Thêm sinh viên mới\n")
                          .append("3. Cập nhật thông tin sinh viên\n")
                          .append("4. Xóa sinh viên\n")
                          .append("5. Đăng ký khóa học cho sinh viên\n")
                          .append("6. Hủy đăng ký khóa học của sinh viên\n")
                          .append("7. Tìm kiếm sinh viên theo tên\n")
                          .append("8. Xem danh sách khóa học của sinh viên\n")
                          .append("9. Quay lại menu chính\n")
                          .append("============================================================");
            System.out.println(subMenuBuilder.toString());
            int choice = readInt(scanner, "Chọn chức năng (1-9): ");

            switch (choice) {
                case 1:
                    listAllStudents();
                    break;
                case 2:
                    addStudent(scanner);
                    break;
                case 3:
                    updateStudent(scanner);
                    break;
                case 4:
                    deleteStudent(scanner);
                    break;
                case 5:
                    enrollCourseForStudent(scanner);
                    break;
                case 6:
                    unenrollCourseForStudent(scanner);
                    break;
                case 7:
                    searchStudentByName(scanner);
                    break;
                case 8:
                    viewStudentCourses(scanner);
                    break;
                case 9:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 1 đến 9.");
            }
        }
    }

    // =====================================================================
    // SUB-MENU: QUẢN LÝ KHÓA HỌC
    // =====================================================================
    private static void courseMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            StringBuilder subMenuBuilder = new StringBuilder();
            subMenuBuilder.append("\n============================================================\n")
                          .append("                     QUẢN LÝ KHÓA HỌC                       \n")
                          .append("============================================================\n")
                          .append("1. Xem danh sách khóa học\n")
                          .append("2. Thêm khóa học mới\n")
                          .append("3. Cập nhật thông tin khóa học\n")
                          .append("4. Xóa khóa học\n")
                          .append("5. Tìm kiếm khóa học theo tên\n")
                          .append("6. Xem danh sách sinh viên đăng ký khóa học\n")
                          .append("7. Quay lại menu chính\n")
                          .append("============================================================");
            System.out.println(subMenuBuilder.toString());
            int choice = readInt(scanner, "Chọn chức năng (1-7): ");

            switch (choice) {
                case 1:
                    listAllCourses();
                    break;
                case 2:
                    addCourse(scanner);
                    break;
                case 3:
                    updateCourse(scanner);
                    break;
                case 4:
                    deleteCourse(scanner);
                    break;
                case 5:
                    searchCourseByTitle(scanner);
                    break;
                case 6:
                    viewCourseStudents(scanner);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 1 đến 7.");
            }
        }
    }

    // =====================================================================
    // CÁC THAO TÁC CHO SINH VIÊN
    // =====================================================================
    private static void listAllStudents() {
        System.out.println("\n--- DANH SÁCH SINH VIÊN ---");
        List<Student> students = studentDAO.findAll();
        if (students == null || students.isEmpty()) {
            System.out.println("Không có sinh viên nào trong hệ thống.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        students.forEach(s -> sb.append(String.format("  [ID: %d] Tên: %-15s | Tuổi: %d%n", s.getId(), s.getName(), s.getAge())));
        System.out.print(sb.toString());
    }

    private static void addStudent(Scanner scanner) {
        System.out.println("\n--- THÊM SINH VIÊN MỚI ---");
        System.out.print("Nhập tên sinh viên: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Tên sinh viên không được để trống!");
            return;
        }
        int age = readInt(scanner, "Nhập tuổi sinh viên: ");
        if (age <= 0) {
            System.out.println("Tuổi sinh viên phải lớn hơn 0!");
            return;
        }
        Student student = new Student(name, age);
        Student saved = studentDAO.save(student);
        if (saved != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thêm sinh viên thành công! [ID: ")
              .append(saved.getId()).append("] Tên: ")
              .append(saved.getName()).append(", Tuổi: ")
              .append(saved.getAge());
            System.out.println(sb.toString());
        } else {
            System.out.println("Thêm sinh viên thất bại!");
        }
    }

    private static void updateStudent(Scanner scanner) {
        System.out.println("\n--- CẬP NHẬT THÔNG TIN SINH VIÊN ---");
        Long id = readLong(scanner, "Nhập ID sinh viên cần cập nhật: ");
        Student student = studentDAO.findById(id);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + id);
            return;
        }
        StringBuilder currentInfo = new StringBuilder();
        currentInfo.append("Thông tin hiện tại: Tên: ").append(student.getName()).append(", Tuổi: ").append(student.getAge());
        System.out.println(currentInfo.toString());

        System.out.print("Nhập tên mới (để trống nếu không đổi): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            student.setName(name);
        }
        System.out.print("Nhập tuổi mới (nhập 0 nếu không đổi): ");
        int age = readInt(scanner, "");
        if (age > 0) {
            student.setAge(age);
        }
        Student updated = studentDAO.update(student);
        if (updated != null) {
            System.out.println("Cập nhật thông tin sinh viên thành công!");
        } else {
            System.out.println("Cập nhật thông tin sinh viên thất bại!");
        }
    }

    private static void deleteStudent(Scanner scanner) {
        System.out.println("\n--- XÓA SINH VIÊN ---");
        Long id = readLong(scanner, "Nhập ID sinh viên cần xóa: ");
        Student student = studentDAO.findById(id);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + id);
            return;
        }
        StringBuilder confirmMsg = new StringBuilder();
        confirmMsg.append("Bạn có chắc chắn muốn xóa sinh viên '")
                  .append(student.getName()).append("' (ID: ")
                  .append(student.getId()).append(")? (Y/N): ");
        System.out.print(confirmMsg.toString());

        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean success = studentDAO.deleteById(id);
            if (success) {
                System.out.println("Xóa sinh viên thành công!");
            } else {
                System.out.println("Xóa sinh viên thất bại!");
            }
        } else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }

    private static void enrollCourseForStudent(Scanner scanner) {
        System.out.println("\n--- ĐĂNG KÝ KHÓA HỌC CHO SINH VIÊN ---");
        Long studentId = readLong(scanner, "Nhập ID sinh viên: ");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + studentId);
            return;
        }
        
        Long courseId = readLong(scanner, "Nhập ID khóa học: ");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học có ID = " + courseId);
            return;
        }

        studentDAO.enrollCourse(studentId, courseId);
        StringBuilder successMsg = new StringBuilder();
        successMsg.append("Đăng ký thành công! Sinh viên '")
                  .append(student.getName()).append("' đã đăng ký khóa học '")
                  .append(course.getTitle()).append("'.");
        System.out.println(successMsg.toString());
    }

    private static void unenrollCourseForStudent(Scanner scanner) {
        System.out.println("\n--- HỦY ĐĂNG KÝ KHÓA HỌC CỦA SINH VIÊN ---");
        Long studentId = readLong(scanner, "Nhập ID sinh viên: ");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + studentId);
            return;
        }

        Long courseId = readLong(scanner, "Nhập ID khóa học: ");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học có ID = " + courseId);
            return;
        }

        studentDAO.unenrollCourse(studentId, courseId);
        StringBuilder successMsg = new StringBuilder();
        successMsg.append("Hủy đăng ký thành công! Sinh viên '")
                  .append(student.getName()).append("' đã hủy đăng ký khóa học '")
                  .append(course.getTitle()).append("'.");
        System.out.println(successMsg.toString());
    }

    private static void searchStudentByName(Scanner scanner) {
        System.out.println("\n--- TÌM KIẾM SINH VIÊN THEO TÊN ---");
        System.out.print("Nhập từ khóa tìm kiếm tên: ");
        String name = scanner.nextLine().trim();
        List<Student> students = studentDAO.findByName(name);
        if (students == null || students.isEmpty()) {
            System.out.println("Không tìm thấy sinh viên nào khớp với từ khóa: " + name);
            return;
        }
        System.out.printf("Tìm thấy %d sinh viên:%n", students.size());
        StringBuilder sb = new StringBuilder();
        students.forEach(s -> sb.append(String.format("  [ID: %d] Tên: %-15s | Tuổi: %d%n", s.getId(), s.getName(), s.getAge())));
        System.out.print(sb.toString());
    }

    private static void viewStudentCourses(Scanner scanner) {
        System.out.println("\n--- XEM DANH SÁCH KHÓA HỌC CỦA SINH VIÊN ---");
        Long id = readLong(scanner, "Nhập ID sinh viên: ");
        Student student = studentDAO.findById(id);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + id);
            return;
        }
        System.out.printf("Danh sách khóa học sinh viên '%s' (ID: %d) đang theo học:%n", student.getName(), student.getId());
        List<Course> courses = courseDAO.findByStudentId(id);
        if (courses == null || courses.isEmpty()) {
            System.out.println("  (Chưa đăng ký khóa học nào)");
            return;
        }
        StringBuilder sb = new StringBuilder();
        courses.forEach(c -> sb.append(String.format("  [ID: %d] %-25s | Số tín chỉ: %d%n", c.getId(), c.getTitle(), c.getCredit())));
        System.out.print(sb.toString());
    }

    // =====================================================================
    // CÁC THAO TÁC CHO KHÓA HỌC
    // =====================================================================
    private static void listAllCourses() {
        System.out.println("\n--- DANH SÁCH KHÓA HỌC ---");
        List<Course> courses = courseDAO.findAll();
        if (courses == null || courses.isEmpty()) {
            System.out.println("Không có khóa học nào trong hệ thống.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        courses.forEach(c -> sb.append(String.format("  [ID: %d] Tên môn: %-25s | Tín chỉ: %d%n", c.getId(), c.getTitle(), c.getCredit())));
        System.out.print(sb.toString());
    }

    private static void addCourse(Scanner scanner) {
        System.out.println("\n--- THÊM KHÓA HỌC MỚI ---");
        System.out.print("Nhập tên khóa học: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Tên khóa học không được để trống!");
            return;
        }
        int credit = readInt(scanner, "Nhập số tín chỉ: ");
        if (credit <= 0) {
            System.out.println("Số tín chỉ phải lớn hơn 0!");
            return;
        }
        Course course = new Course(title, credit);
        Course saved = courseDAO.save(course);
        if (saved != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thêm khóa học thành công! [ID: ")
              .append(saved.getId()).append("] Tên môn: ")
              .append(saved.getTitle()).append(", Tín chỉ: ")
              .append(saved.getCredit());
            System.out.println(sb.toString());
        } else {
            System.out.println("Thêm khóa học thất bại!");
        }
    }

    private static void updateCourse(Scanner scanner) {
        System.out.println("\n--- CẬP NHẬT THÔNG TIN KHÓA HỌC ---");
        Long id = readLong(scanner, "Nhập ID khóa học cần cập nhật: ");
        Course course = courseDAO.findById(id);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học có ID = " + id);
            return;
        }
        StringBuilder currentInfo = new StringBuilder();
        currentInfo.append("Thông tin hiện tại: Tên môn: ").append(course.getTitle()).append(", Tín chỉ: ").append(course.getCredit());
        System.out.println(currentInfo.toString());

        System.out.print("Nhập tên môn mới (để trống nếu không đổi): ");
        String title = scanner.nextLine().trim();
        if (!title.isEmpty()) {
            course.setTitle(title);
        }
        System.out.print("Nhập số tín chỉ mới (nhập 0 nếu không đổi): ");
        int credit = readInt(scanner, "");
        if (credit > 0) {
            course.setCredit(credit);
        }
        Course updated = courseDAO.update(course);
        if (updated != null) {
            System.out.println("Cập nhật thông tin khóa học thành công!");
        } else {
            System.out.println("Cập nhật thông tin khóa học thất bại!");
        }
    }

    private static void deleteCourse(Scanner scanner) {
        System.out.println("\n--- XÓA KHÓA HỌC ---");
        Long id = readLong(scanner, "Nhập ID khóa học cần xóa: ");
        Course course = courseDAO.findById(id);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học có ID = " + id);
            return;
        }
        StringBuilder confirmMsg = new StringBuilder();
        confirmMsg.append("Bạn có chắc chắn muốn xóa khóa học '")
                  .append(course.getTitle()).append("' (ID: ")
                  .append(course.getId()).append(")? (Y/N): ");
        System.out.print(confirmMsg.toString());

        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean success = courseDAO.deleteById(id);
            if (success) {
                System.out.println("Xóa khóa học thành công!");
            } else {
                System.out.println("Xóa khóa học thất bại!");
            }
        } else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }

    private static void searchCourseByTitle(Scanner scanner) {
        System.out.println("\n--- TÌM KIẾM KHÓA HỌC THEO TÊN ---");
        System.out.print("Nhập từ khóa tìm kiếm tên khóa học: ");
        String title = scanner.nextLine().trim();
        List<Course> courses = courseDAO.findByTitle(title);
        if (courses == null || courses.isEmpty()) {
            System.out.println("Không tìm thấy khóa học nào khớp với từ khóa: " + title);
            return;
        }
        System.out.printf("Tìm thấy %d khóa học:%n", courses.size());
        StringBuilder sb = new StringBuilder();
        courses.forEach(c -> sb.append(String.format("  [ID: %d] Tên môn: %-25s | Tín chỉ: %d%n", c.getId(), c.getTitle(), c.getCredit())));
        System.out.print(sb.toString());
    }

    private static void viewCourseStudents(Scanner scanner) {
        System.out.println("\n--- DANH SÁCH SINH VIÊN ĐĂNG KÝ KHÓA HỌC ---");
        Long id = readLong(scanner, "Nhập ID khóa học: ");
        Course course = courseDAO.findById(id);
        if (course == null) {
            System.out.println("Không tìm thấy khóa học có ID = " + id);
            return;
        }
        System.out.printf("Danh sách sinh viên đăng ký học môn '%s' (ID: %d):%n", course.getTitle(), course.getId());
        List<Student> students = studentDAO.findByCourseId(id);
        if (students == null || students.isEmpty()) {
            System.out.println("  (Chưa có sinh viên nào đăng ký)");
            return;
        }
        StringBuilder sb = new StringBuilder();
        students.forEach(s -> sb.append(String.format("  [ID: %d] %-15s | Tuổi: %d%n", s.getId(), s.getName(), s.getAge())));
        System.out.print(sb.toString());
    }

    // =====================================================================
    // CHƯƠNG TRÌNH KIỂM THỬ TỰ ĐỘNG (TASK 5 & 6)
    // =====================================================================
    private static void runAutomaticTests() {
        System.out.println("\n--- BẮT ĐẦU CHẠY KIỂM THỬ TỰ ĐỘNG (TASK 5 & 6) ---");

        cleanDatabase();

        // 1. INSERT SAMPLE DATA
        section("1. INSERT SAMPLE DATA");

        Course java   = courseDAO.save(new Course("Java Programming", 4));
        Course db     = courseDAO.save(new Course("Database Systems", 3));
        Course webDev = courseDAO.save(new Course("Web Development", 2));

        StringBuilder courseLogs = new StringBuilder();
        courseLogs.append("Courses created:\n")
                  .append(String.format("  [OK] [id=%d] %s (%d credits)%n", java.getId(),   java.getTitle(),   java.getCredit()))
                  .append(String.format("  [OK] [id=%d] %s (%d credits)%n", db.getId(),     db.getTitle(),     db.getCredit()))
                  .append(String.format("  [OK] [id=%d] %s (%d credits)%n", webDev.getId(), webDev.getTitle(), webDev.getCredit()));
        System.out.print(courseLogs.toString());

        Student anna = studentDAO.save(new Student("Anna",  22));
        Student john = studentDAO.save(new Student("John",  19));
        Student bob  = studentDAO.save(new Student("Bob",   25));

        StringBuilder studentLogs = new StringBuilder();
        studentLogs.append("\nStudents created:\n")
                   .append(String.format("  [OK] [id=%d] %s (age=%d)%n", anna.getId(), anna.getName(), anna.getAge()))
                   .append(String.format("  [OK] [id=%d] %s (age=%d)%n", john.getId(), john.getName(), john.getAge()))
                   .append(String.format("  [OK] [id=%d] %s (age=%d)%n", bob.getId(),  bob.getName(),  bob.getAge()));
        System.out.print(studentLogs.toString());

        // 2. ENROLLMENT
        section("2. ENROLLMENT (Task 4)");

        studentDAO.enrollCourse(anna.getId(), java.getId());
        studentDAO.enrollCourse(anna.getId(), db.getId());
        studentDAO.enrollCourse(john.getId(), java.getId());
        studentDAO.enrollCourse(john.getId(), webDev.getId());
        studentDAO.enrollCourse(bob.getId(),  db.getId());
        studentDAO.enrollCourse(bob.getId(),  webDev.getId());
        studentDAO.enrollCourse(bob.getId(),  java.getId());

        StringBuilder enrollLogs = new StringBuilder();
        enrollLogs.append("Enrollment complete:\n")
                  .append(String.format("  %-6s -> Java Programming, Database Systems%n", anna.getName()))
                  .append(String.format("  %-6s -> Java Programming, Web Development%n",  john.getName()))
                  .append(String.format("  %-6s -> Java Programming, Database Systems, Web Development%n", bob.getName()));
        System.out.print(enrollLogs.toString());

        System.out.println("\n-- Courses of each student --");
        for (Student s : new Student[]{anna, john, bob}) {
            List<Course> courses = courseDAO.findByStudentId(s.getId());
            StringBuilder sb = new StringBuilder();
            sb.append("  Courses of ").append(s.getName()).append(": ");
            if (courses != null) {
                courses.forEach(c -> sb.append("[").append(c.getTitle()).append("] "));
            }
            System.out.println(sb.toString());
        }

        System.out.println("\n-- Students of each course --");
        for (Course c : new Course[]{java, db, webDev}) {
            List<Student> students = studentDAO.findByCourseId(c.getId());
            StringBuilder sb = new StringBuilder();
            sb.append("  Students in '").append(c.getTitle()).append("': ");
            if (students != null) {
                students.forEach(s -> sb.append("[").append(s.getName()).append("] "));
            }
            System.out.println(sb.toString());
        }

        System.out.println("\n-- Unenroll John from Web Development --");
        studentDAO.unenrollCourse(john.getId(), webDev.getId());
        List<Course> johnCourses = courseDAO.findByStudentId(john.getId());
        
        StringBuilder unenrollBuilder = new StringBuilder();
        unenrollBuilder.append("  Courses of John after unenroll: ");
        if (johnCourses != null) {
            johnCourses.forEach(c -> unenrollBuilder.append("[").append(c.getTitle()).append("] "));
        }
        System.out.println(unenrollBuilder.toString());

        studentDAO.enrollCourse(john.getId(), webDev.getId());

        // 3. CRUD OPERATIONS
        section("3. CRUD OPERATIONS");

        System.out.println("[READ] All students:");
        StringBuilder allStudentsBuilder = new StringBuilder();
        studentDAO.findAll().forEach(s ->
            allStudentsBuilder.append(String.format("  Student{id=%d, name='%s', age=%d}%n", s.getId(), s.getName(), s.getAge()))
        );
        System.out.print(allStudentsBuilder.toString());

        System.out.println("\n[UPDATE] Change Bob's age from 25 -> 26:");
        Student bobFresh = studentDAO.findById(bob.getId());
        bobFresh.setAge(26);
        studentDAO.update(bobFresh);
        Student bobUpdated = studentDAO.findById(bob.getId());
        System.out.printf("  Bob updated -> age=%d%n", bobUpdated.getAge());

        System.out.println("\n[CREATE] Add a temporary student 'Charlie' (age=18):");
        Student charlie = studentDAO.save(new Student("Charlie", 18));
        System.out.printf("  Created: Student{id=%d, name='%s', age=%d}%n",
                charlie.getId(), charlie.getName(), charlie.getAge());

        System.out.println("\n[DELETE] Delete Charlie (id=" + charlie.getId() + "):");
        boolean deleted = studentDAO.deleteById(charlie.getId());
        System.out.println("  Result: " + (deleted ? "[OK] Deleted successfully" : "[FAIL] Delete failed"));

        System.out.println("\n[READ] Remaining students:");
        StringBuilder remainingBuilder = new StringBuilder();
        studentDAO.findAll().forEach(s ->
            remainingBuilder.append(String.format("  Student{id=%d, name='%s', age=%d}%n", s.getId(), s.getName(), s.getAge()))
        );
        System.out.print(remainingBuilder.toString());

        // 4. HIBERNATE QUERIES (Task 5)
        section("4. HIBERNATE QUERIES (Task 5)");

        System.out.println("--- [HQL Query] Students older than 20:");
        List<Student> olderThan20 = studentDAO.findOlderThan(20);
        if (olderThan20 != null && !olderThan20.isEmpty()) {
            StringBuilder olderBuilder = new StringBuilder();
            olderThan20.forEach(s ->
                olderBuilder.append(String.format("  Student{id=%d, name='%s', age=%d}%n", s.getId(), s.getName(), s.getAge()))
            );
            System.out.print(olderBuilder.toString());
        } else {
            System.out.println("  (none)");
        }

        System.out.println("\n--- [HQL with JOIN FETCH] Students and their enrolled courses:");
        List<Student> studentsWithCourses = studentDAO.findStudentsWithCourses();
        if (studentsWithCourses != null) {
            StringBuilder joinFetchBuilder = new StringBuilder();
            studentsWithCourses.forEach(s -> {
                joinFetchBuilder.append(String.format("  %s (age=%d) enrolled in: ", s.getName(), s.getAge()));
                if (s.getCourses().isEmpty()) {
                    joinFetchBuilder.append("(no courses)");
                } else {
                    s.getCourses().forEach(c -> joinFetchBuilder.append("[").append(c.getTitle()).append("] "));
                }
                joinFetchBuilder.append("\n");
            });
            System.out.print(joinFetchBuilder.toString());
        }

        System.out.println("\n--- [Named Query] Find students by name containing 'a' (Student.findByName):");
        List<Student> byNamedQuery = studentDAO.findByNamedQuery("a");
        if (byNamedQuery != null) {
            StringBuilder namedBuilder = new StringBuilder();
            byNamedQuery.forEach(s ->
                namedBuilder.append(String.format("  Student{id=%d, name='%s', age=%d}%n", s.getId(), s.getName(), s.getAge()))
            );
            System.out.print(namedBuilder.toString());
        }

        System.out.println("\n--- [Criteria API] Courses with credit > 2:");
        List<Course> highCredit = courseDAO.findByMinCreditCriteria(2);
        if (highCredit != null) {
            StringBuilder highBuilder = new StringBuilder();
            highCredit.forEach(c ->
                highBuilder.append(String.format("  Course{id=%d, title='%s', credit=%d}%n", c.getId(), c.getTitle(), c.getCredit()))
            );
            System.out.print(highBuilder.toString());
        }

        System.out.println("\n--- [Aggregation] Student count per course:");
        List<Object[]> counts = courseDAO.countStudentsPerCourse();
        if (counts != null) {
            StringBuilder aggBuilder = new StringBuilder();
            counts.forEach(row -> aggBuilder.append(String.format("  %-25s : %d student(s)%n", row[0], (Long) row[1])));
            System.out.print(aggBuilder.toString());
        }

        // 5. BONUS
        section("5. BONUS");

        System.out.println("--- [Pagination] All students (page 0, size 2):");
        List<Student> page0 = studentDAO.findAllPaged(0, 2);
        if (page0 != null) {
            StringBuilder page0Builder = new StringBuilder();
            page0.forEach(s -> page0Builder.append(String.format("  Student{id=%d, name='%s', age=%d}%n", s.getId(), s.getName(), s.getAge())));
            System.out.print(page0Builder.toString());
        }
        System.out.println("[Pagination] All students (page 1, size 2):");
        List<Student> page1 = studentDAO.findAllPaged(1, 2);
        if (page1 != null) {
            StringBuilder page1Builder = new StringBuilder();
            page1.forEach(s -> page1Builder.append(String.format("  Student{id=%d, name='%s', age=%d}%n", s.getId(), s.getName(), s.getAge())));
            System.out.print(page1Builder.toString());
        }

        System.out.println("--- [Not Enrolled] Students with no courses:");
        Student loner = studentDAO.save(new Student("Loner", 30));
        List<Student> notEnrolled = studentDAO.findNotEnrolled();
        if (notEnrolled != null && !notEnrolled.isEmpty()) {
            StringBuilder notEnrolledBuilder = new StringBuilder();
            notEnrolled.forEach(s -> notEnrolledBuilder.append(String.format("  Student{id=%d, name='%s', age=%d}%n", s.getId(), s.getName(), s.getAge())));
            System.out.print(notEnrolledBuilder.toString());
        } else {
            System.out.println("  (all students are enrolled)");
        }
        studentDAO.deleteById(loner.getId());

        System.out.println("\n--- KIỂM THỰ TỰ ĐỘNG HOÀN TẤT [OK] ---");
    }

    // =====================================================================
    // TIỆN ÍCH TRỢ GIÚP
    // =====================================================================
    private static void section(String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--------------------------------------\n")
          .append("  ").append(title).append("\n")
          .append("--------------------------------------");
        System.out.println(sb.toString());
    }

    private static void cleanDatabase() {
        System.out.println("[INIT] Cleaning old data...");
        List<Student> students = studentDAO.findAll();
        if (students != null) {
            students.forEach(s -> studentDAO.deleteById(s.getId()));
        }
        List<Course> courses = courseDAO.findAll();
        if (courses != null) {
            courses.forEach(c -> courseDAO.deleteById(c.getId()));
        }
        System.out.println("[INIT] Database cleared.\n");
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return 0;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ!");
            }
        }
    }

    private static Long readLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập ID hợp lệ (số nguyên)!");
            }
        }
    }
}