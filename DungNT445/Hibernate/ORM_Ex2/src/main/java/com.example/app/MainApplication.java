package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;
import com.example.util.Validator;
import java.util.List;

public class MainApplication {
    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();

    public static void main(String[] args) {
        try {
            boolean run = true;
            while (run) {
                System.out.println("\n========== HỆ THỐNG QUẢN LÝ ĐÀO TẠO (MAIN MENU) ==========");
                System.out.println("1. Quản lý sinh viên (Student Management)");
                System.out.println("2. Quản lý môn học (Course Management)");
                System.out.println("3. Quản lý đăng ký học (Enrollment Management)");
                System.out.println("4. Truy vấn chuyên sâu & Báo cáo (Queries and Reports)");
                System.out.println("5. Thoát chương trình");
                System.out.println("=========================================================");
                
                int choice = Validator.readInt("Nhập lựa chọn của bạn (1-5): ", 1, 5);
                switch (choice) {
                    case 1 -> studentMenu();
                    case 2 -> courseMenu();
                    case 3 -> enrollmentMenu();
                    case 4 -> reportsMenu();
                    case 5 -> {
                        System.out.println("👋 Đang đóng kết nối dữ liệu và thoát ứng dụng...");
                        HibernateUtil.shutdown();
                        run = false;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Hệ thống gặp sự cố nghiêm trọng: " + e.getMessage());
        }
    }

    // 1. Student Menu
    private static void studentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Quản lý Sinh viên ---");
            System.out.println("1. Thêm mới sinh viên");
            System.out.println("2. Cập nhật thông tin sinh viên");
            System.out.println("3. Xóa sinh viên");
            System.out.println("4. Tìm kiếm sinh viên theo ID");
            System.out.println("5. Danh sách toàn bộ sinh viên");
            System.out.println("6. Quay lại Main Menu");
            
            int choice = Validator.readInt("Chọn tác vụ (1-6): ", 1, 6);
            switch (choice) {
                case 1 -> {
                    String name = Validator.readString("Nhập tên sinh viên: ", false);
                    int age = Validator.readInt("Nhập tuổi sinh viên: ", 10, 100);
                    studentService.createStudent(name, age);
                    System.out.println("✅ Đã thêm sinh viên thành công!");
                }
                case 2 -> {
                    int id = Validator.readInt("Nhập ID sinh viên cần sửa: ", 1, Integer.MAX_VALUE);
                    String name = Validator.readString("Nhập tên mới: ", false);
                    int age = Validator.readInt("Nhập tuổi mới: ", 10, 100);
                    studentService.updateStudent(id, name, age);
                    System.out.println("✅ Thực hiện cập nhật hoàn tất.");
                }
                case 3 -> {
                    int id = Validator.readInt("Nhập ID sinh viên cần xóa: ", 1, Integer.MAX_VALUE);
                    studentService.deleteStudent(id);
                    System.out.println("✅ Thực hiện thao tác xóa hoàn tất.");
                }
                case 4 -> {
                    int id = Validator.readInt("Nhập ID sinh viên: ", 1, Integer.MAX_VALUE);
                    Student s = studentService.getStudentById(id);
                    System.out.println(s != null ? s : "❌ Không tìm thấy sinh viên tương ứng.");
                }
                case 5 -> {
                    System.out.println("--- Danh sách Sinh viên ---");
                    studentService.getAllStudents().forEach(System.out::println);
                }
                case 6 -> back = true;
            }
        }
    }

    // 2. Course Menu
    private static void courseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Quản lý Môn học ---");
            System.out.println("1. Tạo môn học mới");
            System.out.println("2. Sửa thông tin môn học");
            System.out.println("3. Xóa môn học");
            System.out.println("4. Xem môn học theo ID");
            System.out.println("5. Danh sách toàn bộ môn học");
            System.out.println("6. Quay lại Main Menu");

            int choice = Validator.readInt("Chọn tác vụ (1-6): ", 1, 6);
            switch (choice) {
                case 1 -> {
                    String title = Validator.readString("Nhập tiêu đề môn học: ", false);
                    int credit = Validator.readInt("Nhập số tín chỉ (1-10): ", 1, 10);
                    courseService.createCourse(title, credit);
                    System.out.println("✅ Đã tạo môn học mới!");
                }
                case 2 -> {
                    int id = Validator.readInt("Nhập ID môn học cần sửa: ", 1, Integer.MAX_VALUE);
                    String title = Validator.readString("Nhập tiêu đề môn mới: ", false);
                    int credit = Validator.readInt("Nhập số tín chỉ mới (1-10): ", 1, 10);
                    courseService.updateCourse(id, title, credit);
                    System.out.println("✅ Đã cập nhật thông tin môn học.");
                }
                case 3 -> {
                    int id = Validator.readInt("Nhập ID môn học cần xóa: ", 1, Integer.MAX_VALUE);
                    courseService.deleteCourse(id);
                    System.out.println("✅ Thực hiện thao tác xóa hoàn tất.");
                }
                case 4 -> {
                    int id = Validator.readInt("Nhập ID môn học: ", 1, Integer.MAX_VALUE);
                    Course c = courseService.getCourseById(id);
                    System.out.println(c != null ? c : "❌ Không tìm thấy môn học tương ứng.");
                }
                case 5 -> {
                    System.out.println("--- Danh sách Môn học ---");
                    courseService.getAllCourses().forEach(System.out::println);
                }
                case 6 -> back = true;
            }
        }
    }

    // 3. Enrollment Menu
    private static void enrollmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Quản lý Đăng ký môn (Enrollment) ---");
            System.out.println("1. Đăng ký cho sinh viên vào lớp học");
            System.out.println("2. Hủy đăng ký học phần");
            System.out.println("3. Xem danh sách môn học của sinh viên");
            System.out.println("4. Xem danh sách sinh viên tham gia môn học");
            System.out.println("5. Quay lại Main Menu");

            int choice = Validator.readInt("Chọn tác vụ (1-5): ", 1, 5);
            switch (choice) {
                case 1 -> {
                    int sId = Validator.readInt("Nhập ID Sinh viên: ", 1, Integer.MAX_VALUE);
                    int cId = Validator.readInt("Nhập ID Môn học: ", 1, Integer.MAX_VALUE);
                    studentService.enrollStudentInCourse(sId, cId);
                    System.out.println("✅ Thực hiện đăng ký hoàn tất.");
                }
                case 2 -> {
                    int sId = Validator.readInt("Nhập ID Sinh viên: ", 1, Integer.MAX_VALUE);
                    int cId = Validator.readInt("Nhập ID Môn học: ", 1, Integer.MAX_VALUE);
                    studentService.removeStudentFromCourse(sId, cId);
                    System.out.println("✅ Đã hủy học phần.");
                }
                case 3 -> {
                    int sId = Validator.readInt("Nhập ID Sinh viên: ", 1, Integer.MAX_VALUE);
                    List<Course> courses = studentService.getCoursesOfStudent(sId);
                    if(courses.isEmpty()) System.out.println("📭 Sinh viên này hiện chưa đăng ký môn nào.");
                    else courses.forEach(System.out::println);
                }
                case 4 -> {
                    int cId = Validator.readInt("Nhập ID Môn học: ", 1, Integer.MAX_VALUE);
                    List<Student> students = courseService.getStudentsOfCourse(cId);
                    if(students.isEmpty()) System.out.println("📭 Lớp học này chưa có sinh viên đăng ký.");
                    else students.forEach(System.out::println);
                }
                case 5 -> back = true;
            }
        }
    }

    // 4. Queries and Reports Menu
    private static void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Báo cáo & Truy vấn nâng cao ---");
            System.out.println("1. Tìm sinh viên lớn hơn số tuổi yêu cầu (HQL)");
            System.out.println("2. Tìm sinh viên theo tên (Named Query)");
            System.out.println("3. Danh sách tất cả sinh viên kèm môn học tương ứng (HQL Join)");
            System.out.println("4. Lọc môn học theo số tín chỉ lớn hơn giá trị truyền vào (Criteria API)");
            System.out.println("5. Thống kê số lượng sinh viên đăng ký trong từng môn (Aggregation Query)");
            System.out.println("6. Tìm toàn bộ sinh viên đăng ký một môn học cụ thể (Parameterized Query)");
            System.out.println("7. Quay lại Main Menu");

            int choice = Validator.readInt("Chọn tác vụ (1-7): ", 1, 7);
            switch (choice) {
                case 1 -> {
                    int age = Validator.readInt("Nhập điều kiện số tuổi: ", 0, 100);
                    studentService.findOlderThan(age).forEach(System.out::println);
                }
                case 2 -> {
                    String name = Validator.readString("Nhập từ khóa tên cần tìm: ", false);
                    studentService.findByName(name).forEach(System.out::println);
                }
                case 3 -> {
                    System.out.println("--- Kết quả HQL Join Query ---");
                    for (Object[] row : studentService.findAllStudentsWithCourses()) {
                        System.out.printf("Sinh viên: %s | Đã đăng ký môn: %s\n", row[0], row[1]);
                    }
                }
                case 4 -> {
                    int minCredit = Validator.readInt("Nhập mốc tín chỉ tối thiểu cần lọc: ", 0, 10);
                    courseService.findCoursesWithCreditGreaterThan(minCredit).forEach(System.out::println);
                }
                case 5 -> {
                    System.out.println("--- Thống kê số lượng (Aggregation) ---");
                    for (Object[] row : courseService.countStudentsOfCourse()) {
                        System.out.printf("Course: %-25s | Students enrolled: %s\n", row[0], row[1]);
                    }
                }
                case 6 -> {
                    int courseId = Validator.readInt("Nhập ID môn học cụ thể: ", 1, Integer.MAX_VALUE);
                    studentService.findStudentsEnrolledInCourse(courseId).forEach(System.out::println);
                }
                case 7 -> back = true;
            }
        }
    }
}
