package fa.training.controller;

import fa.training.entity.Student;
import fa.training.service.StudentService;
import fa.training.service.impl.StudentServiceImpl;
import fa.training.util.ConsoleUtil;
import fa.training.view.StudentView;

import java.util.List;

public class StudentController {
    private final StudentService studentService = new StudentServiceImpl();
    private final StudentView view = new StudentView();

    public StudentView getView() {
        return view;
    }

    public void addStudent() {
        String name = ConsoleUtil.readString("Enter student name: ");
        int age = ConsoleUtil.readInt("Enter student age: ");
        studentService.createStudent(name, age);
    }

    public void displayAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(System.out::println);
        }
    }

    public void findStudentById() {
        int viewId = ConsoleUtil.readInt("Enter student ID to view: ");
        Student student = studentService.getStudentById(viewId);
        if (student != null) {
            System.out.println(student);
        } else {
            System.out.println("Student not found.");
        }
    }

    public void updateStudent() {
        int updateId = ConsoleUtil.readInt("Enter student ID to update: ");
        String newName = ConsoleUtil.readString("Enter new name: ");
        int newAge = ConsoleUtil.readInt("Enter new age: ");
        studentService.updateStudent(updateId, newName, newAge);
    }

    public void deleteStudent() {
        int deleteId = ConsoleUtil.readInt("Enter student ID to delete: ");
        studentService.deleteStudent(deleteId);
    }
}
