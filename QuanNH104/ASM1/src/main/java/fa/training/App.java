package fa.training;

import fa.training.controller.EmployeeController;
import fa.training.entities.Employee;
import fa.training.service.EmployeeService;
import fa.training.util.HibernateUtil;
import fa.training.view.EmployeeView;
import fa.training.view.Menu;
import org.hibernate.Session;
import java.util.Scanner;

public final class App {
    private static final EmployeeService employeeService = new EmployeeService();
    private static final EmployeeView employeeView = new EmployeeView();
    private static final EmployeeController employeeController = new EmployeeController(employeeService, employeeView);

    public static void main(String[] args) {
        // Tắt log dư thừa của Hibernate để dễ nhìn trên Console
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
        System.setProperty("org.slf4j.simpleLogger.log.org.hibernate", "error");

        // Yêu cầu đề bài (Problem 2 & 3): Kiểm tra cấu hình và kết nối cơ sở dữ liệu fadb (thực hiện âm thầm)
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Kết nối thành công, không in gì ra Console
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.exit(1);
        }

        // Tự động chèn dữ liệu mẫu nếu bảng trống
        insertSampleData();

        var scanner = new Scanner(System.in);

        // Thiết lập Submenu 1: Quản lý xem và thêm nhân viên
        Menu viewAddMenu = new Menu("Employee Management Submenu");
        viewAddMenu.addItem("Display all Employees", employeeController::listEmployees);
        viewAddMenu.addItem("Add new Employee", () -> employeeController.addEmployee(scanner));
        viewAddMenu.addItem("Search Employee by ID", () -> employeeController.searchEmployee(scanner));
        viewAddMenu.addItem("Back to Main Menu", viewAddMenu::exitMenu);

        // Thiết lập Submenu 2: Bảo trì (Cập nhật / Xóa) nhân viên
        Menu maintenanceMenu = new Menu("Employee Maintenance Submenu");
        maintenanceMenu.addItem("Update Employee", () -> employeeController.updateEmployee(scanner));
        maintenanceMenu.addItem("Delete Employee", () -> employeeController.deleteEmployee(scanner));
        maintenanceMenu.addItem("Back to Main Menu", maintenanceMenu::exitMenu);

        // Thiết lập Menu chính (Main Menu) điều hướng sang các Submenu
        Menu mainMenu = new Menu("Main Menu");
        mainMenu.addItem("Employee Management", () -> viewAddMenu.show(scanner));
        mainMenu.addItem("Employee Maintenance", () -> maintenanceMenu.show(scanner));
        mainMenu.addItem("Exit Application", () -> {
            System.out.println("Exiting the system. Thank you!");
            scanner.close();
            HibernateUtil.shutdown();
            System.exit(0);
        });

        // Hiển thị menu chính
        mainMenu.show(scanner);
    }

    private static void insertSampleData() {
        try {
            if (employeeService.getAllEmployees().isEmpty()) {
                System.out.println("Inserting sample employee data...");
                employeeService.addEmployee(new Employee("John", "Doe"));
                employeeService.addEmployee(new Employee("Jane", "Smith"));
                employeeService.addEmployee(new Employee("Mike", "Johnson"));
                System.out.println("Sample data inserted successfully!");
            }
        } catch (Exception e) {
            System.out.println("Failed to insert sample data: " + e.getMessage());
        }
    }
}
