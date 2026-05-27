package fa.training.controller;

import fa.training.dao.StudentDAO;
import fa.training.entities.Student;
import fa.training.util.ValidationUtil;
import fa.training.view.ConsoleView;
import java.util.List;

public class StudentController {
    private final StudentDAO studentDAO;
    private final ConsoleView view;

    public StudentController(StudentDAO studentDAO, ConsoleView view) {
        this.studentDAO = studentDAO;
        this.view = view;
    }

    public void handleMenu() {
        boolean back = false;
        view.printStudentMenu();
        while (!back) {
            int choice = view.readInt("Enter your choice (1-9): ");
            System.out.println();
            switch (choice) {
                case 1 -> {
                    addStudent();
                    view.printStudentMenu();
                }
                case 2 -> {
                    updateStudent();
                    view.printStudentMenu();
                }
                case 3 -> {
                    deleteStudent();
                    view.printStudentMenu();
                }
                case 4 -> {
                    findStudentById();
                    view.printStudentMenu();
                }
                case 5 -> {
                    listStudents();
                    view.printStudentMenu();
                }
                case 6 -> {
                    findStudentsByName();
                    view.printStudentMenu();
                }
                case 7 -> {
                    findStudentsOlderThan();
                    view.printStudentMenu();
                }
                case 8 -> {
                    listUnenrolledStudents();
                    view.printStudentMenu();
                }
                case 9 -> back = true;
                default -> view.displayMessage("Invalid option. Please enter a choice between 1 and 9.");
            }
            System.out.println();
        }
    }

    private void addStudent() {
        view.displayMessage("--- Add Student ---");
        String name = view.readString("Enter Name: ");
        if (!ValidationUtil.validateStudentName(name)) {
            return;
        }
        int age = view.readInt("Enter Age: ");
        if (!ValidationUtil.validateStudentAge(age)) {
            return;
        }
        Student student = new Student(name, age);
        studentDAO.save(student);
        view.displayMessage("Student saved successfully: " + student);
    }

    private void updateStudent() {
        view.displayMessage("--- Update Student ---");
        int id = view.readInt("Enter Student ID to update: ");
        Student student = studentDAO.findById(id);
        if (student == null) {
            view.displayMessage("Student with ID " + id + " not found.");
            return;
        }
        view.displayMessage("Current Data: " + student);
        String name = view.readString("Enter New Name: ");
        if (!ValidationUtil.validateStudentName(name)) {
            return;
        }
        int age = view.readInt("Enter New Age: ");
        if (!ValidationUtil.validateStudentAge(age)) {
            return;
        }
        student.setName(name);
        student.setAge(age);
        studentDAO.update(student);
        view.displayMessage("Student updated successfully.");
    }

    private void deleteStudent() {
        view.displayMessage("--- Delete Student ---");
        int id = view.readInt("Enter Student ID to delete: ");
        Student student = studentDAO.findById(id);
        if (student == null) {
            view.displayMessage("Student with ID " + id + " not found.");
            return;
        }
        studentDAO.delete(id);
        view.displayMessage("Student deleted successfully.");
    }

    private void findStudentById() {
        view.displayMessage("--- Find Student by ID ---");
        int id = view.readInt("Enter Student ID: ");
        Student student = studentDAO.findById(id);
        view.displayStudent(student);
    }

    private void listStudents() {
        view.displayMessage("--- List Students ---");
        String ans = view.readString("Do you want paginated output? (y/n): ").toLowerCase();
        if (ans.equals("y") || ans.equals("yes")) {
            int page = view.readInt("Enter Page Number (starting from 1): ");
            int size = view.readInt("Enter Page Size: ");
            List<Student> students = studentDAO.findAllPaginated(page, size);
            view.displayStudents(students);
        } else {
            List<Student> students = studentDAO.findAll();
            view.displayStudents(students);
        }
    }

    private void findStudentsByName() {
        view.displayMessage("--- Find Students by Name (Named Query) ---");
        String name = view.readString("Enter name to search: ");
        List<Student> students = studentDAO.findByName(name);
        view.displayStudents(students);
    }

    private void findStudentsOlderThan() {
        view.displayMessage("--- Find Students Older Than Age (HQL Query) ---");
        int age = view.readInt("Enter age limit: ");
        List<Student> students = studentDAO.findStudentsOlderThan(age);
        view.displayStudents(students);
    }

    private void listUnenrolledStudents() {
        view.displayMessage("--- Unenrolled Students ---");
        List<Student> students = studentDAO.findStudentsNotEnrolled();
        view.displayStudents(students);
    }
}
