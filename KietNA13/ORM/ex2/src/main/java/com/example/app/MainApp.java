package com.example.app;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;
import com.example.util.InputHelper;

import java.util.Map;
import java.util.Scanner;

public class MainApp {

    private static final int MENU_MIN    = 1;
    private static final int MENU_MAX    = 5;
    private static final int EXIT_OPTION = 5;

    public static void main(String[] args) {
        StudentDAO     studentDAO     = new StudentDAOImpl();
        CourseDAO      courseDAO      = new CourseDAOImpl();
        StudentService studentService = new StudentServiceImpl(studentDAO, courseDAO);
        CourseService  courseService  = new CourseServiceImpl(courseDAO);

        Scanner sc = new Scanner(System.in);

        StudentMenu   studentMenu   = new StudentMenu(studentService, sc);
        CourseMenu    courseMenu    = new CourseMenu(courseService, sc);
        EnrollmentMenu enrollMenu   = new EnrollmentMenu(studentService, courseService, sc);
        QueryMenu     queryMenu     = new QueryMenu(studentService, courseService, sc);

        Map<Integer, Runnable> mainActions = Map.of(
                1, studentMenu::show,
                2, courseMenu::show,
                3, enrollMenu::show,
                4, queryMenu::show
        );

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputHelper.readIntInRange(sc, "  Chọn: ", MENU_MIN, MENU_MAX);
            if (choice == EXIT_OPTION) {
                running = false;
            } else {
                Runnable action = mainActions.get(choice);
                if (action != null) {
                    action.run();
                }
            }
        }

        System.out.println("\n  Tạm biệt!");
        sc.close();
        HibernateUtil.shutdown();
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║     HỆ THỐNG QUẢN LÝ KHÓA HỌC   ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Quản lý Sinh Viên            ║");
        System.out.println("║  2. Quản lý Khóa Học             ║");
        System.out.println("║  3. Quản lý Đăng Ký              ║");
        System.out.println("║  4. Truy Vấn & Báo Cáo           ║");
        System.out.println("║  5. Thoát                        ║");
        System.out.println("╚══════════════════════════════════╝");
    }
}
