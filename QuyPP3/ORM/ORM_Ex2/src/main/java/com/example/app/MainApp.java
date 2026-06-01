package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainApp {
    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Dummy Data Mocking để Test ứng dụng dễ dàng hơn
        seedData();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput("Mời chọn chức năng (1-5): ");
            running = switch (choice) {
                case 1 -> { handleStudentMenu(); yield true; }
                case 2 -> { handleCourseMenu(); yield true; }
                case 3 -> { handleEnrollmentMenu(); yield true; }
                case 4 -> { handleQueryMenu(); yield true; }
                case 5 -> {
                    System.out.println("Tắt ứng dụng. Hẹn gặp lại!");
                    HibernateUtil.shutdown();
                    yield false;
                }
                default -> {
                    System.out.println("⚠️ Lựa chọn sai. Hãy chọn lại từ 1 đến 5.");
                    yield true;
                }
            };
        }
    }

    private static void seedData() {
        studentService.createStudent("Nguyen Van A", 20);
        studentService.createStudent("Tran Thi B", 22);
        studentService.createStudent("Le Van C", 19);

        courseService.createCourse("Java Programming", 4);
        courseService.createCourse("Database Systems", 3);
        courseService.createCourse("Web Development", 3);

        studentService.enrollStudentInCourse(1, 1);
        studentService.enrollStudentInCourse(1, 2);
        studentService.enrollStudentInCourse(2, 1);
    }

    // --- PRINTERS MENUS ---
    private static void printMainMenu() {
        System.out.println("\n===== HỆ THỐNG QUẢN LÝ ĐÀO TẠO =====");
        System.out.println("1. Quản lý Học sinh");
        System.out.println("2. Quản lý Môn học");
        System.out.println("3. Quản lý Đăng ký Học phần");
        System.out.println("4. Truy vấn chuyên sâu & Báo cáo");
        System.out.println("5. Thoát chương trình");
        System.out.println("====================================");
    }

    private static void handleStudentMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- QUẢN LÝ HỌC SINH ---");
            System.out.println("1. Tạo mới học sinh");
            System.out.println("2. Cập nhật thông tin");
            System.out.println("3. Xóa học sinh");
            System.out.println("4. Tìm kiếm theo ID");
            System.out.println("5. Danh sách toàn bộ học sinh");
            System.out.println("6. Quay lại Menu chính");
            int choice = readIntInput("Chọn chức năng (1-6): ");

            inLoop = switch (choice) {
                case 1 -> {
                    String name = readStringInput("Nhập tên học sinh: ");
                    int age = readIntInput("Nhập tuổi học sinh: ");
                    studentService.createStudent(name, age);
                    System.out.println("Thêm học sinh thành công.");
                    yield true;
                }
                case 2 -> {
                    int id = readIntInput("Nhập ID học sinh cần sửa: ");
                    String name = readStringInput("Nhập tên mới: ");
                    int age = readIntInput("Nhập tuổi mới: ");
                    studentService.updateStudent(id, name, age);
                    yield true;
                }
                case 3 -> {
                    int id = readIntInput("Nhập ID học sinh cần xóa: ");
                    studentService.deleteStudent(id);
                    yield true;
                }
                case 4 -> {
                    int id = readIntInput("Nhập ID học sinh: ");
                    Student s = studentService.getStudentById(id);
                    System.out.println(s != null ? s : "Không tìm thấy học sinh!");
                    yield true;
                }
                case 5 -> {
                    List<Student> list = studentService.getAllStudents();
                    if (list.isEmpty()) System.out.println("Danh sách trống.");
                    else list.forEach(System.out::println);
                    yield true;
                }
                case 6 -> false;
                default -> { System.out.println("Lựa chọn không hợp lệ!"); yield true; }
            };
        }
    }

    private static void handleCourseMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- QUẢN LÝ MÔN HỌC ---");
            System.out.println("1. Tạo môn học mới");
            System.out.println("2. Cập nhật môn học");
            System.out.println("3. Xóa môn học");
            System.out.println("4. Tìm môn học theo ID");
            System.out.println("5. Danh sách toàn bộ môn học");
            System.out.println("6. Quay lại Menu chính");
            int choice = readIntInput("Chọn chức năng (1-6): ");

            inLoop = switch (choice) {
                case 1 -> {
                    String title = readStringInput("Nhập tên môn học: ");
                    int credit = readIntInput("Nhập số tín chỉ: ");
                    courseService.createCourse(title, credit);
                    System.out.println("Thêm môn học thành công.");
                    yield true;
                }
                case 2 -> {
                    int id = readIntInput("Nhập ID môn học cần sửa: ");
                    String title = readStringInput("Nhập tên môn học mới: ");
                    int credit = readIntInput("Nhập số tín chỉ mới: ");
                    courseService.updateCourse(id, title, credit);
                    yield true;
                }
                case 3 -> {
                    int id = readIntInput("Nhập ID môn học cần xóa: ");
                    courseService.deleteCourse(id);
                    yield true;
                }
                case 4 -> {
                    int id = readIntInput("Nhập ID môn học: ");
                    Course c = courseService.getCourseById(id);
                    System.out.println(c != null ? c : "Không tìm thấy môn học!");
                    yield true;
                }
                case 5 -> {
                    List<Course> list = courseService.getAllCourses();
                    if (list.isEmpty()) System.out.println("Danh sách trống.");
                    else list.forEach(System.out::println);
                    yield true;
                }
                case 6 -> false;
                default -> { System.out.println("Lựa chọn không hợp lệ!"); yield true; }
            };
        }
    }

    private static void handleEnrollmentMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- QUẢN LÝ ĐĂNG KÝ HỌC PHẦN ---");
            System.out.println("1. Đăng ký học sinh vào môn học");
            System.out.println("2. Hủy học phần của học sinh");
            System.out.println("3. Xem các môn học của 1 học sinh");
            System.out.println("4. Xem danh sách học sinh thuộc môn học");
            System.out.println("5. Quay lại Menu chính");
            int choice = readIntInput("Chọn chức năng (1-5): ");

            inLoop = switch (choice) {
                case 1 -> {
                    int sId = readIntInput("Nhập ID Học sinh: ");
                    int cId = readIntInput("Nhập ID Môn học: ");
                    studentService.enrollStudentInCourse(sId, cId);
                    yield true;
                }
                case 2 -> {
                    int sId = readIntInput("Nhập ID Học sinh: ");
                    int cId = readIntInput("Nhập ID Môn học: ");
                    studentService.removeStudentFromCourse(sId, cId);
                    yield true;
                }
                case 3 -> {
                    int sId = readIntInput("Nhập ID Học sinh: ");
                    Set<Course> courses = studentService.getCoursesOfStudent(sId);
                    if (courses.isEmpty()) System.out.println("Học sinh chưa đăng ký môn nào hoặc sai ID.");
                    else courses.forEach(System.out::println);
                    yield true;
                }
                case 4 -> {
                    int cId = readIntInput("Nhập ID Môn học: ");
                    Set<Student> students = courseService.getStudentsOfCourse(cId);
                    if (students.isEmpty()) System.out.println("Môn học chưa có học sinh đăng ký hoặc sai ID.");
                    else students.forEach(System.out::println);
                    yield true;
                }
                case 5 -> false;
                default -> { System.out.println("Lựa chọn không hợp lệ!"); yield true; }
            };
        }
    }

    private static void handleQueryMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- TRUY VẤN VÀ BÁO CÁO NÂNG CAO ---");
            System.out.println("1. Tìm học sinh lớn hơn số tuổi yêu cầu (HQL)");
            System.out.println("2. Tìm học sinh theo tên (Named Query)");
            System.out.println("3. Liệt kê tất cả học sinh kèm tên môn đã đăng ký (HQL Join)");
            System.out.println("4. Tìm môn học có số tín chỉ lớn hơn giá trị yêu cầu (Criteria API)");
            System.out.println("5. Thống kê số lượng học sinh đăng ký mỗi môn (Aggregation Query)");
            System.out.println("6. Tìm danh sách học sinh đăng ký cụ thể theo ID môn học");
            System.out.println("7. Quay lại Menu chính");
            int choice = readIntInput("Chọn chức năng (1-7): ");

            inLoop = switch (choice) {
                case 1 -> {
                    int age = readIntInput("Nhập số tuổi mốc: ");
                    List<Student> students = studentService.findOlderThan(age);
                    students.forEach(System.out::println);
                    yield true;
                }
                case 2 -> {
                    String name = readStringInput("Nhập từ khóa tên cần tìm: ");
                    List<Student> students = studentService.findByName(name);
                    students.forEach(System.out::println);
                    yield true;
                }
                case 3 -> {
                    List<Object[]> report = studentService.getAllStudentsWithCourseTitles();
                    report.forEach(row -> System.out.printf("Học sinh: %s | Môn đăng ký: %s\n", row[0], row[1]));
                    yield true;
                }
                case 4 -> {
                    int credits = readIntInput("Nhập mốc tín chỉ: ");
                    List<Course> courses = courseService.findCoursesWithCreditGreaterThan(credits);
                    courses.forEach(System.out::println);
                    yield true;
                }
                case 5 -> {
                    List<Object[]> stats = courseService.countStudentsPerCourse();
                    stats.forEach(row -> System.out.printf("Môn: %-20s | Tổng số học sinh: %s\n", row[0], row[1]));
                    yield true;
                }
                case 6 -> {
                    int cId = readIntInput("Nhập ID môn học: ");
                    List<Student> students = studentService.findStudentsByCourseId(cId);
                    if (students.isEmpty()) System.out.println("Không tìm thấy học sinh đăng ký học phần này.");
                    else students.forEach(System.out::println);
                    yield true;
                }
                case 7 -> false;
                default -> { System.out.println("Lựa chọn không hợp lệ!"); yield true; }
            };
        }
    }

    // --- INPUT VALIDATORS (PHÒNG VỆ LỖI GÕ SAI KÝ TỰ CỦA NGƯỜI DÙNG) ---
    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value < 0) {
                    System.out.println("Giá trị số không được âm! Vui lòng nhập lại.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Định dạng sai. Vui lòng chỉ nhập số nguyên.");
            }
        }
    }

    private static String readStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Nội dung nhập vào không được để trống!");
                continue;
            }
            return input;
        }
    }
}
