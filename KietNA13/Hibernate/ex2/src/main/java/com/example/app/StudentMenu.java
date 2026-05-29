package com.example.app;

import com.example.service.StudentService;
import com.example.util.InputHelper;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StudentMenu {

    private static final int MENU_MIN    = 1;
    private static final int MENU_MAX    = 6;
    private static final int BACK_OPTION = 6;

    private final StudentService studentService;
    private final Scanner sc;

    public StudentMenu(StudentService studentService, Scanner sc) {
        this.studentService = studentService;
        this.sc             = sc;
    }

    public void show() {
        Map<Integer, Runnable> actions = Map.of(
                1, this::createStudent,
                2, this::updateStudent,
                3, this::deleteStudent,
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
        System.out.println("║      QUẢN LÝ SINH VIÊN       ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Thêm sinh viên mới       ║");
        System.out.println("║  2. Cập nhật sinh viên       ║");
        System.out.println("║  3. Xóa sinh viên            ║");
        System.out.println("║  4. Xem sinh viên theo ID    ║");
        System.out.println("║  5. Danh sách sinh viên      ║");
        System.out.println("║  6. Quay lại                 ║");
        System.out.println("╚══════════════════════════════╝");
    }

    private void createStudent() {
        String name = InputHelper.readString(sc, "  Tên sinh viên: ");
        int age     = InputHelper.readPositiveInt(sc, "  Tuổi: ");
        studentService.createStudent(name, age);
        System.out.println("  [✓] Đã thêm sinh viên thành công.");
    }

    private void updateStudent() {
        int id      = InputHelper.readPositiveInt(sc, "  ID sinh viên cần cập nhật: ");
        String name = InputHelper.readString(sc, "  Tên mới: ");
        int age     = InputHelper.readPositiveInt(sc, "  Tuổi mới: ");
        studentService.updateStudent(id, name, age);
        System.out.println("  [✓] Đã cập nhật sinh viên thành công.");
    }

    private void deleteStudent() {
        int id = InputHelper.readPositiveInt(sc, "  ID sinh viên cần xóa: ");
        studentService.deleteStudent(id);
        System.out.println("  [✓] Đã xóa sinh viên thành công.");
    }

    private void viewById() {
        int id = InputHelper.readPositiveInt(sc, "  ID sinh viên: ");
        studentService.getStudentById(id)
                .ifPresentOrElse(
                        s -> System.out.println("  " + s),
                        () -> System.out.println("  [!] Không tìm thấy sinh viên.")
                );
    }

    private void listAll() {
        List<com.example.entity.Student> list = studentService.getAllStudents();
        if (list.isEmpty()) {
            System.out.println("  Chưa có sinh viên nào.");
            return;
        }
        list.forEach(s -> System.out.println("  " + s));
    }
}
