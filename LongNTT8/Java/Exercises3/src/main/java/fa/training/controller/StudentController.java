package fa.training.controller;

import fa.training.handler.ConsoleInputUtil;
import fa.training.handler.ValidationUtil;
import fa.training.model.Student;
import fa.training.service.StudentService;
import java.util.List;

public class StudentController {

    private static final StudentService studentService = new StudentService();

    public static void createStudent() {
        System.out.println("\n--- Create New Student ---");
        String id;
        while (true) {
            id = ConsoleInputUtil.readString("Enter Student ID (format ST-XXXXXXXX, e.g. ST-ABCD1234): ");
            if (!ValidationUtil.isValidId(id, "ST")) {
                System.out.println("Invalid ID format! Format must be ST-XXXXXXXX (where X is an uppercase letter or digit).");
            } else if (studentService.getStudentById(id) != null) {
                System.out.println("Student ID already exists! Please enter a unique ID.");
            } else {
                break;
            }
        }

        String name = ConsoleInputUtil.readString("Enter Student Name: ");
        
        int age;
        while (true) {
            age = ConsoleInputUtil.readInt("Enter Student Age: ");
            if (age <= 0 || age > 120) {
                System.out.println("Invalid age! Age must be between 1 and 120.");
            } else {
                break;
            }
        }

        String major = ConsoleInputUtil.readString("Enter Student Major: ");
        String email = readValidEmail("Enter Student Email: ", null);
        String phone = readValidPhone("Enter Student Phone (10 digits, starts with 0): ", null);

        Student student = new Student(id, name, age, major, email, phone);
        studentService.createStudent(student);

        System.out.println("Student created successfully!");
    }

    public static void updateStudent() {
        System.out.println("\n--- Update Student ---");
        String id = ConsoleInputUtil.readString("Enter Student ID to update: ");
        Student existingStudent = studentService.getStudentById(id);
        
        if (existingStudent == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }

        System.out.println("Current information: " + existingStudent);
        String name = ConsoleInputUtil.readString("Enter new Student Name: ");
        
        int age;
        while (true) {
            age = ConsoleInputUtil.readInt("Enter new Student Age: ");
            if (age <= 0 || age > 120) {
                System.out.println("Invalid age! Age must be between 1 and 120.");
            } else {
                break;
            }
        }

        String major = ConsoleInputUtil.readString("Enter new Student Major: ");
        // Khi update, ta trích xuất id hiện tại ra để bỏ qua check trùng chính email/phone của Student này
        String email = readValidEmail("Enter new Student Email: ", id);
        String phone = readValidPhone("Enter new Student Phone (10 digits, starts with 0): ", id);

        Student student = new Student(id, name, age, major, email, phone);
        studentService.updateStudent(student);

        System.out.println("Student updated successfully!");
    }

    public static void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        String id = ConsoleInputUtil.readString("Enter Student ID to delete: ");
        Student existingStudent = studentService.getStudentById(id);
        
        if (existingStudent == null) {
            System.out.println("Student not found with ID: " + id);
            return;
        }

        studentService.deleteStudent(id);
        System.out.println("Student deleted successfully!");
    }

    public static void listStudents() {
        System.out.println("\n--- List of Students ---");
        List<Student> students = studentService.getAllStudents();

        if (students == null || students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.printf("%-12s %-25s %-5s %-15s %-25s %-15s%n", "ID", "Name", "Age", "Major", "Email", "Phone");
            System.out.println("----------------------------------------------------------------------------------------------------------------");
            for (Student student : students) {
                System.out.printf("%-12s %-25s %-5d %-15s %-25s %-15s%n", 
                        student.getId(), 
                        student.getName(), 
                        student.getAge(), 
                        student.getMajor(),
                        student.getEmail(),
                        student.getPhone());
            }
        }
    }

    private static String readValidEmail(String prompt, String excludeId) {
        while (true) {
            String email = ConsoleInputUtil.readString(prompt);
            if (!ValidationUtil.isValidEmail(email)) {
                System.out.println("Invalid email format! Example: abc@domain.com");
            } else if (studentService.isEmailExists(email, excludeId)) {
                System.out.println("Email already exists in database! Please enter a unique email.");
            } else {
                return email;
            }
        }
    }

    private static String readValidPhone(String prompt, String excludeId) {
        while (true) {
            String phone = ConsoleInputUtil.readString(prompt);
            if (!ValidationUtil.isValidPhone(phone)) {
                System.out.println("Invalid phone format! Must start with 0 and have exactly 10 digits.");
            } else if (studentService.isPhoneExists(phone, excludeId)) {
                System.out.println("Phone number already exists in database! Please enter a unique phone number.");
            } else {
                return phone;
            }
        }
    }
}
