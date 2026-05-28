package fa.training.main;

import fa.training.dao.EmployeeDao;
import fa.training.entities.Employee;
import fa.training.utils.HibernateUtils;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final EmployeeDao employeeDao = new EmployeeDao();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== KẾT NỐI HIBERNATE ĐẾN FADB ===");
        try {
            // Kiểm tra kết nối lúc khởi động ứng dụng
            HibernateUtils.getSessionFactory();
            System.out.println("Kết nối Database thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi kết nối Database! Vui lòng kiểm tra lại cấu hình.");
            return;
        }

        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = readIntInput("Lựa chọn của bạn: ");

            // Áp dụng Lambda Switch Case (Java 12+) cực kỳ gọn gàng, sạch sẽ
            switch (choice) {
                case 1 -> handleInsert();
                case 2 -> handleGetById();
                case 3 -> handleGetAll();
                case 4 -> handleUpdate();
                case 5 -> handleDelete();
                case 6 -> {
                    System.out.println("Đang đóng kết nối hệ thống... Tạm biệt!");
                    HibernateUtils.shutdown();
                    exit = true;
                }
                default -> System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn từ 1 đến 6.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n================= HIBERNATE MANAGEMENT =================");
        System.out.println("1. Thêm mới nhân viên (Insert)");
        System.out.println("2. Tìm kiếm nhân viên theo ID (Get By ID)");
        System.out.println("3. Hiển thị danh sách nhân viên (Get All)");
        System.out.println("4. Cập nhật thông tin nhân viên (Update)");
        System.out.println("5. Xóa nhân viên theo ID (Delete)");
        System.out.println("6. Thoát chương trình");
        System.out.println("========================================================");
    }

    private static void handleInsert() {
        System.out.println("\n--- THÊM MỚI NHÂN VIÊN ---");
        String firstName = readStringInput("Nhập First Name: ");
        String lastName = readStringInput("Nhập Last Name: ");

        Employee emp = new Employee(firstName, lastName);
        if (employeeDao.insertEmployee(emp)) {
            System.out.println("Thêm thành công! Thao tác ghi nhận ID tự sinh: " + emp.getId());
        } else {
            System.out.println("Thêm thất bại!");
        }
    }

    private static void handleGetById() {
        System.out.println("\n--- TÌM KIẾM NHÂN VIÊN THEO ID ---");
        int id = readIntInput("Nhập ID nhân viên cần tìm: ");
        Employee emp = employeeDao.getEmployeeById(id);
        if (emp != null) {
            System.out.println("Kết quả tìm thấy: " + emp);
        } else {
            System.out.println("Không tìm thấy nhân viên có ID = " + id);
        }
    }

    private static void handleGetAll() {
        System.out.println("\n--- DANH SÁCH TẤT CẢ NHÂN VIÊN ---");
        List<Employee> list = employeeDao.getAllEmployees();
        if (list.isEmpty()) {
            System.out.println("Danh sách trống!");
        } else {
            list.forEach(System.out::println);
        }
    }

    private static void handleUpdate() {
        System.out.println("\n--- CẬP NHẬT THÔNG TIN NHÂN VIÊN ---");
        int id = readIntInput("Nhập ID nhân viên cần cập nhật: ");
        
        Employee checkEmp = employeeDao.getEmployeeById(id);
        if (checkEmp == null) {
            System.out.println("Không tìm thấy nhân viên mang ID này để cập nhật!");
            return;
        }

        String newFirstName = readStringInput("Nhập First Name mới: ");
        String newLastName = readStringInput("Nhập Last Name mới: ");

        if (employeeDao.updateEmployeeById(id, newFirstName, newLastName)) {
            System.out.println("Cập nhật thông tin thành công!");
        } else {
            System.out.println("Cập nhật thất bại!");
        }
    }

    private static void handleDelete() {
        System.out.println("\n--- XÓA NHÂN VIÊN ---");
        int id = readIntInput("Nhập ID nhân viên cần xóa: ");

        Employee checkEmp = employeeDao.getEmployeeById(id);
        if (checkEmp == null) {
            System.out.println("Không tìm thấy nhân viên mang ID này để xóa!");
            return;
        }

        if (employeeDao.deleteEmployeeById(id)) {
            System.out.println("Xóa nhân viên thành công!");
        } else {
            System.out.println("Xóa thất bại!");
        }
    }

    // ================= VALIDATION UTILS =================

    private static String readStringInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Dữ liệu không được để trống! Vui lòng nhập lại.");
            } else if (input.length() > 50) {
                System.out.println("Độ dài ký tự tối đa là 50! Vui lòng nhập ngắn lại.");
            } else {
                return input;
            }
        }
    }

    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val < 0) {
                    System.out.println("Số nhập vào không được là số âm! Vui lòng thử lại.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Định dạng sai! Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }
}
