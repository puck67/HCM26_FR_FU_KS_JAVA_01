package com.example.main;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.util.ConsoleMenu;

import java.util.List;

public class Main {
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();

    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("          KHỞI CHẠY HỆ THỐNG AUTOMATED TEST DEMO              ");
        System.out.println("==============================================================");

        runAutomatedDemo();

        System.out.println("\n==============================================================");
        System.out.println("          BẮT ĐẦU MENU ĐIỀU KHIỂN TƯƠNG TÁC                 ");
        System.out.println("==============================================================");

        ConsoleMenu menu = new ConsoleMenu("Hệ Thống Quản Lý Đào Tạo (Hibernate ORM)");

        menu.addOption("Chạy lại toàn bộ Demo tự động (CRUD & Queries)", Main::runAutomatedDemo);

        menu.addOption("Liệt kê tất cả Sinh viên", () -> {
            System.out.println("\n--- DANH SÁCH TẤT CẢ SINH VIÊN ---");
            studentDAO.findAll().forEach(System.out::println);
        });

        menu.addOption("Liệt kê tất cả Khóa học", () -> {
            System.out.println("\n--- DANH SÁCH TẤT CẢ KHÓA HỌC ---");
            courseDAO.findAll().forEach(System.out::println);
        });

        menu.addOption("Tìm sinh viên lớn hơn 20 tuổi (HQL Query)", () -> {
            System.out.println("\n--- SINH VIÊN > 20 TUỔI ---");
            studentDAO.findStudentsOlderThan(20).forEach(System.out::println);
        });

        menu.addOption("In sinh viên và khóa học đã đăng ký (HQL Join)", () -> {
            System.out.println("\n--- DANH SÁCH SINH VIÊN VÀ KHÓA HỌC ---");
            studentDAO.printStudentsAndCourses();
        });

        menu.addOption("Tìm sinh viên theo tên (Named Query)", () -> {
            System.out.println("\n--- TÌM KIẾM THEO TÊN 'Anna' ---");
            studentDAO.findByName("Anna").forEach(System.out::println);
        });

        menu.addOption("Tìm khóa học có tín chỉ > 2 (Criteria API)", () -> {
            System.out.println("\n--- KHÓA HỌC CÓ TÍN CHỈ > 2 ---");
            courseDAO.findCoursesWithCreditGreaterThan(2).forEach(System.out::println);
        });

        menu.addOption("Đếm số lượng sinh viên mỗi khóa học (Aggregation)", () -> {
            System.out.println("\n--- SỐ LƯỢNG SINH VIÊN MỖI KHÓA ---");
            courseDAO.countStudentsPerCourse();
        });

        menu.display();
    }

    private static void runAutomatedDemo() {
        System.out.println("\n>>> BƯỚC 1: KHỞI TẠO DỮ LIỆU MẪU (Task 6.1 & 6.2) <<<");
        
        // Tạo sinh viên
        Student john = new Student("John", 20);
        Student anna = new Student("Anna", 22);
        Student mike = new Student("Mike", 19);
        
        studentDAO.save(john);
        studentDAO.save(anna);
        studentDAO.save(mike);
        
        System.out.println("Đã lưu 3 sinh viên mẫu:");
        System.out.println(" - " + john);
        System.out.println(" - " + anna);
        System.out.println(" - " + mike);

        // Tạo khóa học
        Course java = new Course("Java Programming", 4);
        Course db = new Course("Database Systems", 3);
        Course web = new Course("Web Development", 2);
        
        courseDAO.save(java);
        courseDAO.save(db);
        courseDAO.save(web);

        System.out.println("Đã lưu 3 khóa học mẫu:");
        System.out.println(" - " + java);
        System.out.println(" - " + db);
        System.out.println(" - " + web);

        // Ghi danh sinh viên vào khóa học (Task 6.2)
        System.out.println("\n>>> BƯỚC 2: GHI DANH SINH VIÊN (Enrollment Logic - Task 4) <<<");
        studentDAO.enrollStudent(john.getId(), java.getId());
        studentDAO.enrollStudent(john.getId(), db.getId());
        studentDAO.enrollStudent(anna.getId(), java.getId());
        studentDAO.enrollStudent(mike.getId(), db.getId());
        studentDAO.enrollStudent(mike.getId(), web.getId());
        System.out.println("Đã ghi danh: ");
        System.out.println(" - John -> Java Programming & Database Systems");
        System.out.println(" - Anna -> Java Programming");
        System.out.println(" - Mike -> Database Systems & Web Development");

        // Kiểm tra hiển thị danh sách khóa học của sinh viên / sinh viên của khóa học (Task 4)
        System.out.println("\n>>> BƯỚC 3: KIỂM TRA HIỂN THỊ CHI TIẾT GHI DANH (Task 4) <<<");
        System.out.println("Các khóa học của sinh viên John:");
        List<Course> johnCourses = studentDAO.getCoursesByStudentId(john.getId());
        johnCourses.forEach(c -> System.out.println(" + " + c.getTitle() + " (" + c.getCredit() + " tín chỉ)"));

        System.out.println("Các sinh viên đăng ký khóa Java Programming:");
        List<Student> javaStudents = courseDAO.getStudentsByCourseId(java.getId());
        javaStudents.forEach(s -> System.out.println(" + " + s.getName() + " (Tuổi: " + s.getAge() + ")"));

        // Minh họa thao tác CRUD (Task 3 & Task 6.3)
        System.out.println("\n>>> BƯỚC 4: MINH HỌA CÁC THAO TÁC CRUD (Task 3) <<<");
        // Create
        Student tempStudent = new Student("David", 21);
        studentDAO.save(tempStudent);
        System.out.println("[CREATE] Đã tạo sinh viên tạm: " + tempStudent);

        // Read
        Student readStudent = studentDAO.findById(tempStudent.getId());
        System.out.println("[READ] Đọc sinh viên tạm từ Database bằng ID: " + readStudent);

        // Update
        readStudent.setAge(25);
        readStudent.setName("David Updated");
        studentDAO.update(readStudent);
        System.out.println("[UPDATE] Đã cập nhật thông tin sinh viên tạm thành: " + studentDAO.findById(readStudent.getId()));

        // Delete
        int tempId = readStudent.getId();
        studentDAO.delete(tempId);
        System.out.println("[DELETE] Đã xóa sinh viên tạm có ID: " + tempId);
        
        Student deletedVerify = studentDAO.findById(tempId);
        System.out.println("[VERIFY DELETE] Tìm lại sinh viên đã xóa bằng ID: " + (deletedVerify == null ? "Không tìm thấy (Thành công)" : "Vẫn tồn tại (Thất bại)"));

        // Thực thi các truy vấn nâng cao (Task 5 & Task 6.4)
        System.out.println("\n>>> BƯỚC 5: THỰC THI TRUY VẤN NÂNG CAO (Task 5 Practice) <<<");
        
        // 5.1 HQL Query: Students older than 20
        System.out.println("\n[5.1 HQL Query] Tìm các sinh viên lớn hơn 20 tuổi:");
        studentDAO.findStudentsOlderThan(20).forEach(s -> System.out.println(" -> " + s));

        // 5.2 HQL with Join
        System.out.println("\n[5.2 HQL with Join] Danh sách sinh viên và các khóa học đã ghi danh:");
        studentDAO.printStudentsAndCourses();

        // 5.3 Named Query: Find by name
        System.out.println("\n[5.3 Named Query] Tìm kiếm sinh viên theo tên 'Anna':");
        studentDAO.findByName("Anna").forEach(s -> System.out.println(" -> " + s));

        // 5.4 Criteria API: Course credits > 2
        System.out.println("\n[5.4 Criteria API] Khóa học có tín chỉ lớn hơn 2:");
        courseDAO.findCoursesWithCreditGreaterThan(2).forEach(c -> System.out.println(" -> " + c));

        // 5.5 Aggregation Query: Count students per course
        System.out.println("\n[5.5 Aggregation] Số lượng sinh viên đã đăng ký mỗi khóa học:");
        courseDAO.countStudentsPerCourse();

        // Minh họa hủy ghi danh (Unenrollment - Task 4)
        System.out.println("\n>>> BƯỚC 6: MINH HỌA HỦY GHI DANH (Task 4) <<<");
        System.out.println("Hủy đăng ký khóa học Database Systems cho John...");
        studentDAO.unenrollStudent(john.getId(), db.getId());
        
        System.out.println("Các khóa học còn lại của John sau khi hủy:");
        studentDAO.getCoursesByStudentId(john.getId()).forEach(c -> System.out.println(" + " + c.getTitle()));
    }
}