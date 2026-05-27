package fa.training.controller;

import fa.training.dao.StudentDAO;
import fa.training.dao.impl.StudentDAOImpl;
import fa.training.entity.Student;
import fa.training.util.ConsoleUtil;
import fa.training.view.StudentView;

import java.util.List;
import java.util.Scanner;

/**
 * Controller for Student CRUD — mirrors the LibraryManagement pattern.
 */
public class StudentController {

    private final StudentDAO  studentDAO = new StudentDAOImpl();
    private final StudentView view       = new StudentView();
    private final Scanner     scanner    = new Scanner(System.in);

    public StudentView getView() { return view; }

    // ------------------------------------------------------------------ //
    //  Add
    // ------------------------------------------------------------------ //

    public void addStudent() {
        view.printHeader("Add New Student");

        String name = ConsoleUtil.readString("Enter name: ");

        int age;
        while (true) {
            age = ConsoleUtil.readInt("Enter age: ");
            if (age > 0 && age < 120) break;
            view.printWarning("Age must be between 1 and 119.");
        }

        Student student = new Student(name, age);

        if (studentDAO.add(student)) {
            view.printSuccess("Student added successfully (id=" + student.getId() + ").");
        } else {
            view.printError("Failed to add student.");
        }
    }

    // ------------------------------------------------------------------ //
    //  Display all
    // ------------------------------------------------------------------ //

    public void displayAllStudents() {
        view.displayStudents(studentDAO.getAll());
    }

    // ------------------------------------------------------------------ //
    //  Find by ID
    // ------------------------------------------------------------------ //

    public void findStudentById() {
        view.printHeader("Find Student by ID");
        int id = ConsoleUtil.readInt("Enter student ID: ");
        view.displayStudent(studentDAO.findById(id));
    }

    // ------------------------------------------------------------------ //
    //  Update
    // ------------------------------------------------------------------ //

    public void updateStudent() {
        view.printHeader("Update Student");

        int id = ConsoleUtil.readInt("Enter student ID: ");
        Student existing = studentDAO.findById(id);

        if (existing == null) {
            view.printWarning("Student not found.");
            return;
        }

        System.out.print("Enter new name [" + existing.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (name.isBlank()) name = existing.getName();

        int age;
        while (true) {
            System.out.print("Enter new age [" + existing.getAge() + "]: ");
            String ageInput = scanner.nextLine().trim();
            if (ageInput.isBlank()) {
                age = existing.getAge();
                break;
            }
            try {
                age = Integer.parseInt(ageInput);
                if (age > 0 && age < 120) break;
                view.printWarning("Age must be between 1 and 119.");
            } catch (NumberFormatException e) {
                view.printWarning("Please enter a valid integer.");
            }
        }

        existing.setName(name);
        existing.setAge(age);

        if (studentDAO.update(existing)) {
            view.printSuccess("Student updated successfully.");
        } else {
            view.printError("Failed to update student.");
        }
    }

    // ------------------------------------------------------------------ //
    //  Delete
    // ------------------------------------------------------------------ //

    public void deleteStudent() {
        view.printHeader("Delete Student");

        int id = ConsoleUtil.readInt("Enter student ID: ");
        Student existing = studentDAO.findById(id);

        if (existing == null) {
            view.printWarning("Student not found.");
            return;
        }

        if (!ConsoleUtil.confirm("Delete student \"" + existing.getName() + "\"?")) {
            view.printWarning("Cancelled.");
            return;
        }

        if (studentDAO.delete(id)) {
            view.printSuccess("Student deleted successfully.");
        } else {
            view.printError("Failed to delete student.");
        }
    }
}
