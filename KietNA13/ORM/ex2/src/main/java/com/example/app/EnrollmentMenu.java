package com.example.app;

import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.util.InputHelper;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EnrollmentMenu {

    private static final int MENU_MIN    = 1;
    private static final int MENU_MAX    = 5;
    private static final int BACK_OPTION = 5;

    private final StudentService studentService;
    private final CourseService  courseService;
    private final Scanner sc;

    public EnrollmentMenu(StudentService studentService, CourseService courseService, Scanner sc) {
        this.studentService = studentService;
        this.courseService  = courseService;
        this.sc             = sc;
    }

    public void show() {
        Map<Integer, Runnable> actions = Map.of(
                1, this::enrollStudent,
                2, this::removeEnrollment,
                3, this::viewCoursesOfStudent,
                4, this::viewStudentsOfCourse
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
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║       QUẢN LÝ ĐĂNG KÝ HỌC       ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Đăng ký sinh viên vào khóa   ║");
        System.out.println("║  2. Hủy đăng ký khóa học         ║");
        System.out.println("║  3. Xem khóa học của sinh viên   ║");
        System.out.println("║  4. Xem sinh viên trong khóa học ║");
        System.out.println("║  5. Quay lại                     ║");
        System.out.println("╚══════════════════════════════════╝");
    }

    private void enrollStudent() {
        int studentId = InputHelper.readPositiveInt(sc, "  ID sinh viên: ");
        int courseId  = InputHelper.readPositiveInt(sc, "  ID khóa học: ");
        studentService.enrollStudentInCourse(studentId, courseId);
        System.out.println("  [✓] Đăng ký thành công.");
    }

    private void removeEnrollment() {
        int studentId = InputHelper.readPositiveInt(sc, "  ID sinh viên: ");
        int courseId  = InputHelper.readPositiveInt(sc, "  ID khóa học: ");
        studentService.removeStudentFromCourse(studentId, courseId);
        System.out.println("  [✓] Đã hủy đăng ký.");
    }

    private void viewCoursesOfStudent() {
        int studentId = InputHelper.readPositiveInt(sc, "  ID sinh viên: ");
        List<com.example.entity.Course> courses = studentService.getCoursesOfStudent(studentId);
        if (courses.isEmpty()) {
            System.out.println("  Sinh viên chưa đăng ký khóa học nào.");
            return;
        }
        courses.forEach(c -> System.out.println("  " + c));
    }

    private void viewStudentsOfCourse() {
        int courseId = InputHelper.readPositiveInt(sc, "  ID khóa học: ");
        List<com.example.entity.Student> students = courseService.getStudentsOfCourse(courseId);
        if (students.isEmpty()) {
            System.out.println("  Chưa có sinh viên nào đăng ký khóa học này.");
            return;
        }
        students.forEach(s -> System.out.println("  " + s));
    }
}
