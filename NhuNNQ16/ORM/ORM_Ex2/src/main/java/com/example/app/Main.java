package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    public static void main(String[] args) {
        try {
            new Main().start();
        } finally {
            HibernateUtil.shutdown();
        }
    }

    public void start() {
        Map<String, Runnable> mainActions = new HashMap<>();
        mainActions.put("1", this::showStudentMenu);
        mainActions.put("2", this::showCourseMenu);
        mainActions.put("3", this::showEnrollmentMenu);
        mainActions.put("4", this::showReportsMenu);
        mainActions.put("5", () -> {
            System.out.println("\nCảm ơn bạn đã sử dụng ứng dụng quản lý đào tạo!");
            running = false;
        });

      
        seedDemoData();

        while (running) {
            System.out.println("\n================ MAIN MENU ================");
            System.out.println("1. Student Management");
            System.out.println("2. Course Management");
            System.out.println("3. Enrollment Management");
            System.out.println("4. Queries and Reports");
            System.out.println("5. Exit");
            System.out.println("===========================================");
            System.out.print("Nhập lựa chọn của bạn (1-5): ");
            String choice = scanner.nextLine().trim();
            
            
            mainActions.getOrDefault(choice, () -> 
                System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn lại từ 1 đến 5.")
            ).run();
        }
    }

    // --- Submenu 1: Student Management ---
    private void showStudentMenu() {
        boolean[] inMenu = {true};
        Map<String, Runnable> actions = new HashMap<>();
        actions.put("1", this::createStudent);
        actions.put("2", this::updateStudent);
        actions.put("3", this::deleteStudent);
        actions.put("4", this::viewStudentById);
        actions.put("5", this::listAllStudents);
        actions.put("6", () -> inMenu[0] = false);

        while (inMenu[0]) {
            System.out.println("\n>>> STUDENT MANAGEMENT <<<");
            System.out.println("1. Create a new student");
            System.out.println("2. Update student information");
            System.out.println("3. Delete a student");
            System.out.println("4. View student by ID");
            System.out.println("5. List all students");
            System.out.println("6. Back to main menu");
            System.out.print("Chọn chức năng (1-6): ");
            String choice = scanner.nextLine().trim();
            actions.getOrDefault(choice, () -> System.out.println("Lựa chọn không hợp lệ!")).run();
        }
    }

    private void createStudent() {
        System.out.print("Nhập tên sinh viên: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Tên không được để trống!");
            return;
        }
        int age = readInt("Nhập tuổi sinh viên: ");
        studentService.createStudent(name, age);
        System.out.println("Đã tạo sinh viên thành công!");
    }

    private void updateStudent() {
        int id = readInt("Nhập ID sinh viên cần cập nhật: ");
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + id);
            return;
        }
        System.out.print("Nhập tên mới (hiện tại: " + student.getName() + " - Nhấn Enter để bỏ qua): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = student.getName();
        }
        int age = readInt("Nhập tuổi mới (hiện tại: " + student.getAge() + "): ");
        studentService.updateStudent(id, name, age);
        System.out.println("Đã cập nhật thông tin sinh viên thành công!");
    }

    private void deleteStudent() {
        int id = readInt("Nhập ID sinh viên cần xóa: ");
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + id);
            return;
        }
        studentService.deleteStudent(id);
        System.out.println("Đã xóa sinh viên thành công!");
    }

    private void viewStudentById() {
        int id = readInt("Nhập ID sinh viên cần xem: ");
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + id);
        } else {
            System.out.println(student);
        }
    }

    private void listAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students == null || students.isEmpty()) {
            System.out.println("Danh sách sinh viên trống!");
        } else {
            System.out.println("\n--- Danh sách toàn bộ sinh viên ---");
            students.forEach(System.out::println);
        }
    }

    // --- Submenu 2: Course Management ---
    private void showCourseMenu() {
        boolean[] inMenu = {true};
        Map<String, Runnable> actions = new HashMap<>();
        actions.put("1", this::createCourse);
        actions.put("2", this::updateCourse);
        actions.put("3", this::deleteCourse);
        actions.put("4", this::viewCourseById);
        actions.put("5", this::listAllCourses);
        actions.put("6", () -> inMenu[0] = false);

        while (inMenu[0]) {
            System.out.println("\n>>> COURSE MANAGEMENT <<<");
            System.out.println("1. Create a new course");
            System.out.println("2. Update course information");
            System.out.println("3. Delete a course");
            System.out.println("4. View course by ID");
            System.out.println("5. List all courses");
            System.out.println("6. Back to main menu");
            System.out.print("Chọn chức năng (1-6): ");
            String choice = scanner.nextLine().trim();
            actions.getOrDefault(choice, () -> System.out.println("Lựa chọn không hợp lệ!")).run();
        }
    }

    private void createCourse() {
        System.out.print("Nhập tiêu đề môn học: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Tiêu đề môn học không được để trống!");
            return;
        }
        int credit = readInt("Nhập số tín chỉ: ");
        courseService.createCourse(title, credit);
        System.out.println("Đã tạo môn học thành công!");
    }

    private void updateCourse() {
        int id = readInt("Nhập ID môn học cần cập nhật: ");
        Course course = courseService.getCourseById(id);
        if (course == null) {
            System.out.println("Không tìm thấy môn học có ID = " + id);
            return;
        }
        System.out.print("Nhập tiêu đề mới (hiện tại: " + course.getTitle() + " - Nhấn Enter để bỏ qua): ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            title = course.getTitle();
        }
        int credit = readInt("Nhập số tín chỉ mới (hiện tại: " + course.getCredit() + "): ");
        courseService.updateCourse(id, title, credit);
        System.out.println("Đã cập nhật môn học thành công!");
    }

    private void deleteCourse() {
        int id = readInt("Nhập ID môn học cần xóa: ");
        Course course = courseService.getCourseById(id);
        if (course == null) {
            System.out.println("Không tìm thấy môn học có ID = " + id);
            return;
        }
        courseService.deleteCourse(id);
        System.out.println("Đã xóa môn học thành công!");
    }

    private void viewCourseById() {
        int id = readInt("Nhập ID môn học cần xem: ");
        Course course = courseService.getCourseById(id);
        if (course == null) {
            System.out.println("Không tìm thấy môn học có ID = " + id);
        } else {
            System.out.println(course);
        }
    }

    private void listAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses == null || courses.isEmpty()) {
            System.out.println("Danh sách môn học trống!");
        } else {
            System.out.println("\n--- Danh sách toàn bộ môn học ---");
            courses.forEach(System.out::println);
        }
    }

    // --- Submenu 3: Enrollment Management ---
    private void showEnrollmentMenu() {
        boolean[] inMenu = {true};
        Map<String, Runnable> actions = new HashMap<>();
        actions.put("1", this::enrollStudent);
        actions.put("2", this::removeStudentFromCourse);
        actions.put("3", this::viewCoursesOfStudent);
        actions.put("4", this::viewStudentsOfCourse);
        actions.put("5", () -> inMenu[0] = false);

        while (inMenu[0]) {
            System.out.println("\n>>> ENROLLMENT MANAGEMENT <<<");
            System.out.println("1. Enroll a student in a course");
            System.out.println("2. Remove a student from a course");
            System.out.println("3. View courses of a student");
            System.out.println("4. View students of a course");
            System.out.println("5. Back to main menu");
            System.out.print("Chọn chức năng (1-5): ");
            String choice = scanner.nextLine().trim();
            actions.getOrDefault(choice, () -> System.out.println("Lựa chọn không hợp lệ!")).run();
        }
    }

    private void enrollStudent() {
        int studentId = readInt("Nhập ID sinh viên: ");
        int courseId = readInt("Nhập ID môn học: ");
        Student student = studentService.getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);

        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + studentId);
            return;
        }
        if (course == null) {
            System.out.println("Không tìm thấy môn học có ID = " + courseId);
            return;
        }

        studentService.enrollStudentInCourse(studentId, courseId);
        System.out.println("Đăng ký môn học [" + course.getTitle() + "] cho sinh viên [" + student.getName() + "] thành công!");
    }

    private void removeStudentFromCourse() {
        int studentId = readInt("Nhập ID sinh viên: ");
        int courseId = readInt("Nhập ID môn học: ");
        Student student = studentService.getStudentById(studentId);
        Course course = courseService.getCourseById(courseId);

        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + studentId);
            return;
        }
        if (course == null) {
            System.out.println("Không tìm thấy môn học có ID = " + courseId);
            return;
        }

        studentService.removeStudentFromCourse(studentId, courseId);
        System.out.println("Đã hủy đăng ký môn học [" + course.getTitle() + "] cho sinh viên [" + student.getName() + "]!");
    }

    private void viewCoursesOfStudent() {
        int studentId = readInt("Nhập ID sinh viên: ");
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có ID = " + studentId);
            return;
        }
        Set<Course> courses = studentService.getCoursesOfStudent(studentId);
        if (courses == null || courses.isEmpty()) {
            System.out.println("Sinh viên " + student.getName() + " chưa đăng ký môn học nào.");
        } else {
            System.out.println("\nSinh viên " + student.getName() + " đã đăng ký các môn học:");
            courses.forEach(c -> System.out.println(" - " + c));
        }
    }

    private void viewStudentsOfCourse() {
        int courseId = readInt("Nhập ID môn học: ");
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Không tìm thấy môn học có ID = " + courseId);
            return;
        }
        Set<Student> students = courseService.getStudentsOfCourse(courseId);
        if (students == null || students.isEmpty()) {
            System.out.println("Chưa có sinh viên nào đăng ký môn học " + course.getTitle());
        } else {
            System.out.println("\nDanh sách sinh viên tham gia môn " + course.getTitle() + ":");
            students.forEach(s -> System.out.println(" - " + s));
        }
    }

    // --- Submenu 4: Queries and Reports ---
    private void showReportsMenu() {
        boolean[] inMenu = {true};
        Map<String, Runnable> actions = new HashMap<>();
        actions.put("1", this::findStudentsOlderThan);
        actions.put("2", this::findStudentsByName);
        actions.put("3", this::listStudentsAndCourses);
        actions.put("4", this::findCoursesWithCreditGreaterThan);
        actions.put("5", this::countStudentsInEachCourse);
        actions.put("6", this::findStudentsEnrolledInSpecificCourse);
        actions.put("7", () -> inMenu[0] = false);

        while (inMenu[0]) {
            System.out.println("\n>>> QUERIES AND REPORTS <<<");
            System.out.println("1. Find students older than a given age (HQL)");
            System.out.println("2. Find students by name (Named Query)");
            System.out.println("3. List students and their courses (HQL Join)");
            System.out.println("4. Find courses with credit greater than a value (Criteria API)");
            System.out.println("5. Count number of students in each course (Aggregation Query)");
            System.out.println("6. Find students enrolled in a specific course");
            System.out.println("7. Back to main menu");
            System.out.print("Chọn chức năng (1-7): ");
            String choice = scanner.nextLine().trim();
            actions.getOrDefault(choice, () -> System.out.println("Lựa chọn không hợp lệ!")).run();
        }
    }

    private void findStudentsOlderThan() {
        int age = readInt("Nhập tuổi: ");
        List<Student> students = studentService.findStudentsOlderThan(age);
        if (students == null || students.isEmpty()) {
            System.out.println("Không tìm thấy sinh viên nào lớn hơn " + age + " tuổi.");
        } else {
            System.out.println("\n--- Sinh viên lớn hơn " + age + " tuổi (HQL) ---");
            students.forEach(System.out::println);
        }
    }

    private void findStudentsByName() {
        System.out.print("Nhập tên sinh viên cần tìm: ");
        String name = scanner.nextLine().trim();
        List<Student> students = studentService.findStudentsByName(name);
        if (students == null || students.isEmpty()) {
            System.out.println("Không tìm thấy sinh viên nào có tên '" + name + "'.");
        } else {
            System.out.println("\n--- Sinh viên có tên '" + name + "' (Named Query) ---");
            students.forEach(System.out::println);
        }
    }

    private void listStudentsAndCourses() {
        List<Object[]> results = studentService.getStudentsWithCourses();
        if (results == null || results.isEmpty()) {
            System.out.println("Chưa có lượt đăng ký nào!");
        } else {
            System.out.println("\n--- Danh sách sinh viên và các môn đã đăng ký (HQL Join) ---");
            results.forEach(row -> System.out.println("Sinh viên: " + row[0] + " | Môn học: " + row[1]));
        }
    }

    private void findCoursesWithCreditGreaterThan() {
        int credit = readInt("Nhập số tín chỉ: ");
        List<Course> courses = courseService.getCoursesWithCreditGreaterThan(credit);
        if (courses == null || courses.isEmpty()) {
            System.out.println("Không tìm thấy môn học nào có số tín chỉ > " + credit);
        } else {
            System.out.println("\n--- Các môn học có tín chỉ > " + credit + " (Criteria API) ---");
            courses.forEach(System.out::println);
        }
    }

    private void countStudentsInEachCourse() {
        List<Object[]> results = courseService.getStudentCountPerCourse();
        if (results == null || results.isEmpty()) {
            System.out.println("Chưa có dữ liệu môn học!");
        } else {
            System.out.println("\n--- Số lượng sinh viên đăng ký mỗi môn học (Aggregation Query) ---");
            results.forEach(row -> System.out.println("Course: " + row[0] + " | Students enrolled: " + row[1]));
        }
    }

    private void findStudentsEnrolledInSpecificCourse() {
        int courseId = readInt("Nhập ID môn học: ");
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Không tìm thấy môn học có ID = " + courseId);
            return;
        }
        List<Student> students = studentService.getStudentsEnrolledInCourse(courseId);
        if (students == null || students.isEmpty()) {
            System.out.println("Không có sinh viên nào đăng ký môn học này.");
        } else {
            System.out.println("\n--- Sinh viên đăng ký môn [" + course.getTitle() + "] (Parameterized Query) ---");
            students.forEach(System.out::println);
        }
    }

    // --- Helpers ---
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên hợp lệ!");
            }
        }
    }

    private void seedDemoData() {
        try {
            //sample data student
            studentService.createStudent("Anh Tuan", 20);
            studentService.createStudent("Bich Phuong", 22);
            studentService.createStudent("Chi Dan", 19);

            // sample data course
            courseService.createCourse("Java Programming", 4);
            courseService.createCourse("Database Systems", 3);
            courseService.createCourse("Web Development", 2);

            // enroll student in course sample data
            studentService.enrollStudentInCourse(1, 1); // Anh Tuan -> Java Programming
            studentService.enrollStudentInCourse(1, 2); // Anh Tuan -> Database Systems
            studentService.enrollStudentInCourse(2, 1); // Bich Phuong -> Java Programming
            studentService.enrollStudentInCourse(3, 3); // Chi Dan -> Web Development
            
            System.out.println("Đã nạp thành công dữ liệu mẫu (Demo Data) để dễ dàng thử nghiệm!");
        } catch (Exception e) {
            System.out.println("Khởi tạo dữ liệu mẫu thất bại: " + e.getMessage());
        }
    }
}
