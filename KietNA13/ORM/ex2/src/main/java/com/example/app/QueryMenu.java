package com.example.app;

import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.util.InputHelper;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class QueryMenu {

    private static final int MENU_MIN    = 1;
    private static final int MENU_MAX    = 7;
    private static final int BACK_OPTION = 7;

    private final StudentService studentService;
    private final CourseService  courseService;
    private final Scanner sc;

    public QueryMenu(StudentService studentService, CourseService courseService, Scanner sc) {
        this.studentService = studentService;
        this.courseService  = courseService;
        this.sc             = sc;
    }

    public void show() {
        Map<Integer, Runnable> actions = Map.of(
                1, this::queryOlderThan,
                2, this::queryByName,
                3, this::queryStudentsWithCourses,
                4, this::queryCoursesByCredit,
                5, this::queryCountPerCourse,
                6, this::queryStudentsByCourse
        );

        boolean running = true;
        while (running) {
            printHeader();
            int choice = InputHelper.readIntInRange(sc, "  Chọn: ", MENU_MIN, MENU_MAX);
            if (choice == BACK_OPTION) {
                running = false;
            } else {
                Runnable action = actions.get(choice);
                if (action != null) {
                    try {
                        action.run();
                    } catch (Exception e) {
                        System.out.println("  [!] " + e.getMessage());
                    }
                }
            }
        }
    }

    private void printHeader() {
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║          TRUY VẤN & BÁO CÁO              ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.println("║  1. Tìm sinh viên lớn hơn độ tuổi (HQL)  ║");
        System.out.println("║  2. Tìm sinh viên theo tên (Named Query)  ║");
        System.out.println("║  3. Sinh viên và khóa học (HQL Join)      ║");
        System.out.println("║  4. Khóa học theo tín chỉ (Criteria API)  ║");
        System.out.println("║  5. Số SV mỗi khóa học (Aggregation)     ║");
        System.out.println("║  6. Sinh viên trong khóa học cụ thể       ║");
        System.out.println("║  7. Quay lại                              ║");
        System.out.println("╚═══════════════════════════════════════════╝");
    }

    private void queryOlderThan() {
        int age = InputHelper.readPositiveInt(sc, "  Nhập độ tuổi tối thiểu: ");
        List<com.example.entity.Student> result = studentService.findOlderThan(age);
        printStudentList(result, "Không có sinh viên nào lớn hơn " + age + " tuổi.");
    }

    private void queryByName() {
        String name = InputHelper.readString(sc, "  Nhập tên (hoặc một phần tên): ");
        List<com.example.entity.Student> result = studentService.findByName(name);
        printStudentList(result, "Không tìm thấy sinh viên nào có tên \"" + name + "\".");
    }

    private void queryStudentsWithCourses() {
        List<Object[]> result = studentService.findAllWithCourses();
        if (result.isEmpty()) {
            System.out.println("  Chưa có dữ liệu đăng ký nào.");
            return;
        }
        result.forEach(row -> {
            String studentName = (String) row[0];
            String courseTitle = (String) row[1];
            System.out.println(new StringBuilder()
                    .append("  Sinh viên: ").append(studentName)
                    .append(" | Khóa học: ").append(courseTitle));
        });
    }

    private void queryCoursesByCredit() {
        int minCredit = InputHelper.readPositiveInt(sc, "  Số tín chỉ tối thiểu (lớn hơn): ");
        List<com.example.entity.Course> result = courseService.findByMinCredit(minCredit);
        if (result.isEmpty()) {
            System.out.println("  Không có khóa học nào có tín chỉ > " + minCredit + ".");
            return;
        }
        result.forEach(c -> System.out.println("  " + c));
    }

    private void queryCountPerCourse() {
        List<Object[]> result = studentService.countPerCourse();
        if (result.isEmpty()) {
            System.out.println("  Không có dữ liệu.");
            return;
        }
        result.forEach(row -> {
            String courseTitle   = (String) row[0];
            Long   studentCount  = (Long) row[1];
            System.out.println(new StringBuilder()
                    .append("  Khóa học: ").append(courseTitle)
                    .append(" | Số sinh viên: ").append(studentCount));
        });
    }

    private void queryStudentsByCourse() {
        int courseId = InputHelper.readPositiveInt(sc, "  ID khóa học: ");
        List<com.example.entity.Student> result = studentService.findByCourseId(courseId);
        printStudentList(result, "Không có sinh viên nào trong khóa học ID=" + courseId + ".");
    }

    private void printStudentList(List<com.example.entity.Student> list, String emptyMessage) {
        if (list.isEmpty()) {
            System.out.println("  " + emptyMessage);
            return;
        }
        list.forEach(s -> System.out.println("  " + s));
    }
}
