package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.StudentService;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            new Main().start();
        } finally {
            // Dam bao dong SessionFactory khi thoat chuong trinh
            HibernateUtil.shutdown();
        }
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Student Management");
            System.out.println("2. Course Management");
            System.out.println("3. Enrollment Management");
            System.out.println("4. Queries and Reports");
            System.out.println("5. Exit");
            System.out.print("Nhap lua chon cua ban (1-5): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showStudentMenu();
                    break;
                case "2":
                    showCourseMenu();
                    break;
                case "3":
                    showEnrollmentMenu();
                    break;
                case "4":
                    showReportsMenu();
                    break;
                case "5":
                    System.out.println("\nCam on ban da su dung ung dung!");
                    running = false;
                    break;
                default:
                    System.out.println("Lua chon khong hop le! Vui long chon lai.");
            }
        }
    }

    // --- Submenu 1: Student Management ---
    private void showStudentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== STUDENT MANAGEMENT ===");
            System.out.println("1. Create a new student");
            System.out.println("2. Update student information");
            System.out.println("3. Delete a student");
            System.out.println("4. View student by ID");
            System.out.println("5. List all students");
            System.out.println("6. Back to main menu");
            System.out.print("Chon chuc nang (1-6): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createStudent();
                    break;
                case "2":
                    updateStudent();
                    break;
                case "3":
                    deleteStudent();
                    break;
                case "4":
                    viewStudentById();
                    break;
                case "5":
                    listAllStudents();
                    break;
                case "6":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private void createStudent() {
        System.out.print("Nhap ten sinh vien: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Loi: Ten sinh vien khong duoc de trong!");
            return;
        }
        int age = readInt("Nhap tuoi sinh vien: ");
        if (age <= 0) {
            System.out.println("Loi: Tuoi sinh vien phai lon hon 0!");
            return;
        }
        try {
            studentService.createStudent(name, age);
            System.out.println("Da tao sinh vien thanh cong!");
        } catch (Exception e) {
            System.out.println("Loi khi tao sinh vien: " + e.getMessage());
        }
    }

    private void updateStudent() {
        int id = readInt("Nhap ID sinh vien can cap nhat: ");
        if (id <= 0) {
            System.out.println("Loi: ID khong hop le!");
            return;
        }
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("Khong tim thay sinh vien co ID = " + id);
            return;
        }
        System.out.print("Nhap ten moi (hien tai: " + student.getName() + " - Nhan Enter de bo qua): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = student.getName();
        }
        int age = readInt("Nhap tuoi moi (hien tai: " + student.getAge() + "): ");
        if (age <= 0) {
            System.out.println("Loi: Tuoi sinh vien phai lon hon 0!");
            return;
        }
        try {
            studentService.updateStudent(id, name, age);
            System.out.println("Da cap nhat thong tin sinh vien thanh cong!");
        } catch (Exception e) {
            System.out.println("Loi khi cap nhat: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        int id = readInt("Nhap ID sinh vien can xoa: ");
        if (id <= 0) {
            System.out.println("Loi: ID khong hop le!");
            return;
        }
        try {
            studentService.deleteStudent(id);
            System.out.println("Da xoa sinh vien thanh cong!");
        } catch (Exception e) {
            System.out.println("Loi khi xoa sinh vien: " + e.getMessage());
        }
    }

    private void viewStudentById() {
        int id = readInt("Nhap ID sinh vien can xem: ");
        if (id <= 0) {
            System.out.println("Loi: ID khong hop le!");
            return;
        }
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("Khong tim thay sinh vien co ID = " + id);
        } else {
            System.out.println(student);
        }
    }

    private void listAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students == null || students.isEmpty()) {
            System.out.println("Danh sach sinh vien trong!");
        } else {
            System.out.println("\n--- Danh sach sinh vien ---");
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    // --- Submenu 2: Course Management ---
    private void showCourseMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== COURSE MANAGEMENT ===");
            System.out.println("1. Create a new course");
            System.out.println("2. Update course information");
            System.out.println("3. Delete a course");
            System.out.println("4. View course by ID");
            System.out.println("5. List all courses");
            System.out.println("6. Back to main menu");
            System.out.print("Chon chuc nang (1-6): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createCourse();
                    break;
                case "2":
                    updateCourse();
                    break;
                case "3":
                    deleteCourse();
                    break;
                case "4":
                    viewCourseById();
                    break;
                case "5":
                    listAllCourses();
                    break;
                case "6":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private void createCourse() {
        System.out.print("Nhap tieu de mon hoc: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Loi: Tieu de mon hoc khong duoc de trong!");
            return;
        }
        int credit = readInt("Nhap so tin chi: ");
        if (credit <= 0) {
            System.out.println("Loi: So tin chi phai lon hon 0!");
            return;
        }
        try {
            courseService.createCourse(title, credit);
            System.out.println("Da tao mon hoc thanh cong!");
        } catch (Exception e) {
            System.out.println("Loi khi tao mon hoc: " + e.getMessage());
        }
    }

    private void updateCourse() {
        int id = readInt("Nhap ID mon hoc can cap nhat: ");
        if (id <= 0) {
            System.out.println("Loi: ID khong hop le!");
            return;
        }
        Course course = courseService.getCourseById(id);
        if (course == null) {
            System.out.println("Khong tim thay mon hoc co ID = " + id);
            return;
        }
        System.out.print("Nhap tieu de moi (hien tai: " + course.getTitle() + " - Nhan Enter de bo qua): ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            title = course.getTitle();
        }
        int credit = readInt("Nhap so tin chi moi (hien tai: " + course.getCredit() + "): ");
        if (credit <= 0) {
            System.out.println("Loi: So tin chi phai lon hon 0!");
            return;
        }
        try {
            courseService.updateCourse(id, title, credit);
            System.out.println("Da cap nhat mon hoc thanh cong!");
        } catch (Exception e) {
            System.out.println("Loi khi cap nhat mon hoc: " + e.getMessage());
        }
    }

    private void deleteCourse() {
        int id = readInt("Nhap ID mon hoc can xoa: ");
        if (id <= 0) {
            System.out.println("Loi: ID khong hop le!");
            return;
        }
        try {
            courseService.deleteCourse(id);
            System.out.println("Da xoa mon hoc thanh cong!");
        } catch (Exception e) {
            System.out.println("Loi khi xoa mon hoc: " + e.getMessage());
        }
    }

    private void viewCourseById() {
        int id = readInt("Nhap ID mon hoc can xem: ");
        if (id <= 0) {
            System.out.println("Loi: ID khong hop le!");
            return;
        }
        Course course = courseService.getCourseById(id);
        if (course == null) {
            System.out.println("Khong tim thay mon hoc co ID = " + id);
        } else {
            System.out.println(course);
        }
    }

    private void listAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses == null || courses.isEmpty()) {
            System.out.println("Danh sach mon hoc trong!");
        } else {
            System.out.println("\n--- Danh sach mon hoc ---");
            for (Course c : courses) {
                System.out.println(c);
            }
        }
    }

    // --- Submenu 3: Enrollment Management ---
    private void showEnrollmentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== ENROLLMENT MANAGEMENT ===");
            System.out.println("1. Enroll a student in a course");
            System.out.println("2. Remove a student from a course");
            System.out.println("3. View courses of a student");
            System.out.println("4. View students of a course");
            System.out.println("5. Back to main menu");
            System.out.print("Chon chuc nang (1-5): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    enrollStudent();
                    break;
                case "2":
                    removeStudentFromCourse();
                    break;
                case "3":
                    viewCoursesOfStudent();
                    break;
                case "4":
                    viewStudentsOfCourse();
                    break;
                case "5":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private void enrollStudent() {
        int studentId = readInt("Nhap ID sinh vien: ");
        int courseId = readInt("Nhap ID mon hoc: ");
        if (studentId <= 0 || courseId <= 0) {
            System.out.println("Loi: ID phai lon hon 0!");
            return;
        }
        try {
            studentService.enrollStudentInCourse(studentId, courseId);
            System.out.println("Dang ky khoa hoc thanh cong!");
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    private void removeStudentFromCourse() {
        int studentId = readInt("Nhap ID sinh vien: ");
        int courseId = readInt("Nhap ID mon hoc: ");
        if (studentId <= 0 || courseId <= 0) {
            System.out.println("Loi: ID phai lon hon 0!");
            return;
        }
        try {
            studentService.removeStudentFromCourse(studentId, courseId);
            System.out.println("Da huy dang ky mon hoc thanh cong!");
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    private void viewCoursesOfStudent() {
        int studentId = readInt("Nhap ID sinh vien: ");
        if (studentId <= 0) {
            System.out.println("Loi: ID khong hop le!");
            return;
        }
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Khong tim thay sinh vien co ID = " + studentId);
            return;
        }
        Set<Course> courses = studentService.getCoursesOfStudent(studentId);
        if (courses == null || courses.isEmpty()) {
            System.out.println("Sinh vien " + student.getName() + " chua dang ky mon hoc nao.");
        } else {
            System.out.println("\nSinh vien " + student.getName() + " da dang ky cac mon hoc:");
            for (Course c : courses) {
                System.out.println(" - " + c);
            }
        }
    }

    private void viewStudentsOfCourse() {
        int courseId = readInt("Nhap ID mon hoc: ");
        if (courseId <= 0) {
            System.out.println("Loi: ID khong hop le!");
            return;
        }
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Khong tim thay mon hoc co ID = " + courseId);
            return;
        }
        Set<Student> students = courseService.getStudentsOfCourse(courseId);
        if (students == null || students.isEmpty()) {
            System.out.println("Chua co sinh vien nao dang ky mon hoc " + course.getTitle());
        } else {
            System.out.println("\nDanh sach sinh vien tham gia mon " + course.getTitle() + ":");
            for (Student s : students) {
                System.out.println(" - " + s);
            }
        }
    }

    // --- Submenu 4: Queries and Reports ---
    private void showReportsMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== QUERIES AND REPORTS ===");
            System.out.println("1. Find students older than a given age (HQL)");
            System.out.println("2. Find students by name (Named Query)");
            System.out.println("3. List students and their courses (HQL Join)");
            System.out.println("4. Find courses with credit greater than a value (Criteria API)");
            System.out.println("5. Count number of students in each course (Aggregation Query)");
            System.out.println("6. Find students enrolled in a specific course");
            System.out.println("7. Back to main menu");
            System.out.print("Chon chuc nang (1-7): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    findStudentsOlderThan();
                    break;
                case "2":
                    findStudentsByName();
                    break;
                case "3":
                    listStudentsAndCourses();
                    break;
                case "4":
                    findCoursesWithCreditGreaterThan();
                    break;
                case "5":
                    countStudentsInEachCourse();
                    break;
                case "6":
                    findStudentsEnrolledInSpecificCourse();
                    break;
                case "7":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private void findStudentsOlderThan() {
        int age = readInt("Nhap tuoi: ");
        if (age <= 0) {
            System.out.println("Tuoi phai lon hon 0!");
            return;
        }
        List<Student> students = studentService.findStudentsOlderThan(age);
        if (students == null || students.isEmpty()) {
            System.out.println("Khong tim thay sinh vien nao lon hon " + age + " tuoi.");
        } else {
            System.out.println("\n--- Sinh vien lon hon " + age + " tuoi (HQL) ---");
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    private void findStudentsByName() {
        System.out.print("Nhap ten sinh vien can tim: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Ten tim kiem khong duoc de trong!");
            return;
        }
        List<Student> students = studentService.findStudentsByName(name);
        if (students == null || students.isEmpty()) {
            System.out.println("Khong tim thay sinh vien nao co ten '" + name + "'.");
        } else {
            System.out.println("\n--- Sinh vien co ten '" + name + "' (Named Query) ---");
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    private void listStudentsAndCourses() {
        List<Object[]> results = studentService.getStudentsWithCourses();
        if (results == null || results.isEmpty()) {
            System.out.println("Chua co luot dang ky nao!");
        } else {
            System.out.println("\n--- Danh sach sinh vien va mon da dang ky ---");
            for (Object[] row : results) {
                System.out.println("Sinh vien: " + row[0] + " | Mon hoc: " + row[1]);
            }
        }
    }

    private void findCoursesWithCreditGreaterThan() {
        int credit = readInt("Nhap so tin chi: ");
        if (credit <= 0) {
            System.out.println("So tin chi phai lon hon 0!");
            return;
        }
        List<Course> courses = courseService.getCoursesWithCreditGreaterThan(credit);
        if (courses == null || courses.isEmpty()) {
            System.out.println("Khong tim thay mon hoc nao co so tin chi > " + credit);
        } else {
            System.out.println("\n--- Mon hoc co tin chi > " + credit + " (Criteria API) ---");
            for (Course c : courses) {
                System.out.println(c);
            }
        }
    }

    private void countStudentsInEachCourse() {
        List<Object[]> results = courseService.getStudentCountPerCourse();
        if (results == null || results.isEmpty()) {
            System.out.println("Chua co du lieu mon hoc!");
        } else {
            System.out.println("\n--- So luong sinh vien dang ky moi mon hoc ---");
            for (Object[] row : results) {
                System.out.println("Mon hoc: " + row[0] + " | Sinh vien: " + row[1]);
            }
        }
    }

    private void findStudentsEnrolledInSpecificCourse() {
        int courseId = readInt("Nhap ID mon hoc: ");
        if (courseId <= 0) {
            System.out.println("ID khong hop le!");
            return;
        }
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Khong tim thay mon hoc co ID = " + courseId);
            return;
        }
        List<Student> students = studentService.getStudentsEnrolledInCourse(courseId);
        if (students == null || students.isEmpty()) {
            System.out.println("Khong co sinh vien nao dang ky mon hoc nay.");
        } else {
            System.out.println("\n--- Sinh vien dang ky mon [" + course.getTitle() + "] ---");
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    // --- Helpers ---
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap mot so nguyen hop le!");
            }
        }
    }
}
