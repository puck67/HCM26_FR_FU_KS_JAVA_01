package fa.training.view;

import fa.training.entities.Employee;
import fa.training.service.EmployeeService;
import fa.training.util.InputUtils;
import java.util.List;
import java.util.Optional;

public class MenuView {

    private final EmployeeService employeeService = new EmployeeService();

    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("=== QUAN LY NHAN VIEN ===");
            System.out.println("1. Hien thi tat ca nhan vien");
            System.out.println("2. Tim kiem nhan vien theo ID");
            System.out.println("3. Them moi nhan vien");
            System.out.println("4. Cap nhat thong tin nhan vien");
            System.out.println("5. Xoa nhan vien");
            System.out.println("0. Thoat chuong trinh");
            System.out.println("=========================");
            
            int choice = InputUtils.readInt("Nhap lua chon cua ban: ");
            switch (choice) {
                case 1:
                    listAllEmployees();
                    break;
                case 2:
                    findEmployeeById();
                    break;
                case 3:
                    addEmployee();
                    break;
                case 4:
                    updateEmployee();
                    break;
                case 5:
                    deleteEmployee();
                    break;
                case 0:
                    System.out.println("Cam on ban da su dung chuong trinh!");
                    exit = true;
                    break;
                default:
                    System.out.println("Lua chon khong hop le! Vui long nhap tu 0 den 5.");
            }
        }
    }

    private void listAllEmployees() {
        System.out.println("\n--- DANH SACH NHAN VIEN ---");
        List<Employee> list = employeeService.getAllEmployees();
        if (list.isEmpty()) {
            System.out.println("Khong co nhan vien nao trong he thong.");
            return;
        }

        System.out.printf("%-10s %-25s %-25s%n", "ID", "First Name", "Last Name");
        System.out.println("------------------------------------------------------------");
        for (Employee emp : list) {
            System.out.printf("%-10d %-25s %-25s%n", emp.getId(), emp.getFirstName(), emp.getLastName());
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("Tong so: " + list.size() + " nhan vien.");
    }

    private void findEmployeeById() {
        System.out.println("\n--- TIM KIEM NHAN VIEN ---");
        int id = InputUtils.readPositiveInt("Nhap ID nhan vien can tim: ");
        try {
            Optional<Employee> empOpt = employeeService.getEmployeeById(id);
            if (empOpt.isPresent()) {
                Employee emp = empOpt.get();
                System.out.println("Nhan vien duoc tim thay:");
                System.out.println(" - ID: " + emp.getId());
                System.out.println(" - First Name: " + emp.getFirstName());
                System.out.println(" - Last Name: " + emp.getLastName());
            } else {
                System.out.println("Khong tim thay nhan vien nao voi ID = " + id);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    private void addEmployee() {
        System.out.println("\n--- THEM MOI NHAN VIEN ---");
        String firstName = InputUtils.readString("Nhap First Name: ");
        String lastName = InputUtils.readString("Nhap Last Name: ");
        
        try {
            Employee saved = employeeService.addEmployee(firstName, lastName);
            System.out.println("Them nhan vien thanh cong! ID moi: " + saved.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Them that bai. Loi: " + e.getMessage());
        }
    }

    private void updateEmployee() {
        System.out.println("\n--- CAP NHAT NHAN VIEN ---");
        int id = InputUtils.readPositiveInt("Nhap ID nhan vien muon cap nhat: ");
        
        try {
            Optional<Employee> empOpt = employeeService.getEmployeeById(id);
            if (empOpt.isEmpty()) {
                System.out.println("Khong tim thay nhan vien voi ID = " + id);
                return;
            }
            
            Employee emp = empOpt.get();
            System.out.println("Thong tin hien tai: " + emp.getFirstName() + " " + emp.getLastName());
            
            String newFirstName = InputUtils.readString("Nhap First Name moi: ");
            String newLastName = InputUtils.readString("Nhap Last Name moi: ");
            
            boolean ok = employeeService.updateEmployee(id, newFirstName, newLastName);
            if (ok) {
                System.out.println("Cap nhat nhan vien thanh cong!");
            } else {
                System.out.println("Cap nhat that bai.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    private void deleteEmployee() {
        System.out.println("\n--- XOA NHAN VIEN ---");
        int id = InputUtils.readPositiveInt("Nhap ID nhan vien muon xoa: ");
        
        try {
            Optional<Employee> empOpt = employeeService.getEmployeeById(id);
            if (empOpt.isEmpty()) {
                System.out.println("Khong tim thay nhan vien voi ID = " + id);
                return;
            }
            
            String confirm = InputUtils.readString("Ban co chac chan muon xoa nhan vien nay? (Y/N): ");
            if ("y".equalsIgnoreCase(confirm) || "yes".equalsIgnoreCase(confirm)) {
                boolean ok = employeeService.deleteEmployee(id);
                if (ok) {
                    System.out.println("Xoa nhan vien thanh cong!");
                } else {
                    System.out.println("Xoa that bai.");
                }
            } else {
                System.out.println("Da huy thao tac xoa.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }
}
