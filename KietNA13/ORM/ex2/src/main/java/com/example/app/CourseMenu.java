package com.example.app;

import com.example.service.CourseService;
import com.example.util.InputHelper;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CourseMenu {

    private static final int MENU_MIN    = 1;
    private static final int MENU_MAX    = 6;
    private static final int BACK_OPTION = 6;

    private final CourseService courseService;
    private final Scanner sc;

    public CourseMenu(CourseService courseService, Scanner sc) {
        this.courseService = courseService;
        this.sc            = sc;
    }

    public void show() {
        Map<Integer, Runnable> actions = Map.of(
                1, this::createCourse,
                2, this::updateCourse,
                3, this::deleteCourse,
                4, this::viewById,
                5, this::listAll
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
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║       QUẢN LÝ KHÓA HỌC      ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Thêm khóa học mới        ║");
        System.out.println("║  2. Cập nhật khóa học        ║");
        System.out.println("║  3. Xóa khóa học             ║");
        System.out.println("║  4. Xem khóa học theo ID     ║");
        System.out.println("║  5. Danh sách khóa học       ║");
        System.out.println("║  6. Quay lại                 ║");
        System.out.println("╚══════════════════════════════╝");
    }

    private void createCourse() {
        String title = InputHelper.readString(sc, "  Tên khóa học: ");
        int credit   = InputHelper.readPositiveInt(sc, "  Số tín chỉ: ");
        courseService.createCourse(title, credit);
        System.out.println("  [✓] Đã thêm khóa học thành công.");
    }

    private void updateCourse() {
        int id       = InputHelper.readPositiveInt(sc, "  ID khóa học cần cập nhật: ");
        String title = InputHelper.readString(sc, "  Tên mới: ");
        int credit   = InputHelper.readPositiveInt(sc, "  Số tín chỉ mới: ");
        courseService.updateCourse(id, title, credit);
        System.out.println("  [✓] Đã cập nhật khóa học thành công.");
    }

    private void deleteCourse() {
        int id = InputHelper.readPositiveInt(sc, "  ID khóa học cần xóa: ");
        courseService.deleteCourse(id);
        System.out.println("  [✓] Đã xóa khóa học thành công.");
    }

    private void viewById() {
        int id = InputHelper.readPositiveInt(sc, "  ID khóa học: ");
        courseService.getCourseById(id)
                .ifPresentOrElse(
                        c -> System.out.println("  " + c),
                        () -> System.out.println("  [!] Không tìm thấy khóa học.")
                );
    }

    private void listAll() {
        List<com.example.entity.Course> list = courseService.getAllCourses();
        if (list.isEmpty()) {
            System.out.println("  Chưa có khóa học nào.");
            return;
        }
        list.forEach(c -> System.out.println("  " + c));
    }
}
