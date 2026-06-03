package org.example.service;

import org.example.dao.StudentDAO;
import org.example.model.Student;
import org.example.util.Validator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StudentService {

    private final StudentDAO dao;

    public StudentService() {
        this.dao = new StudentDAO();
    }


    StudentService(StudentDAO dao) {
        this.dao = dao;
    }


    public String addStudent(Student student) {
        return validateStudent(student)
                .map(err -> "Validation error: " + err)
                .orElseGet(() ->
                    dao.findById(student.getId())
                       .map(existing -> "Error: Student with ID '" + student.getId() + "' already exists.")
                       .orElseGet(() -> dao.add(student)
                               ? "Student added successfully."
                               : "Error: Failed to add student to database.")
                );
    }


    public List<Student> getAllStudents() {
        return dao.getAll()
                  .stream()
                  .sorted((a, b) -> a.getId().compareToIgnoreCase(b.getId()))
                  .collect(Collectors.toList());
    }


    public String updateStudent(Student student) {
        return validateStudent(student)
                .map(err -> "Validation error: " + err)
                .orElseGet(() ->
                    dao.findById(student.getId())
                       .map(existing -> dao.update(student)
                               ? "Student updated successfully."
                               : "Error: Failed to update student.")
                       .orElse("Error: Student with ID '" + student.getId() + "' not found.")
                );
    }

    public String deleteStudent(String id) {
        if (!Validator.isValidId(id)) {
            return "Validation error: " + Validator.idError();
        }
        return dao.findById(id)
                  .map(existing -> dao.delete(id)
                          ? "Student deleted successfully."
                          : "Error: Failed to delete student.")
                  .orElse("Error: Student with ID '" + id + "' not found.");
    }


    public Optional<Student> findById(String id) {
        return dao.findById(id);
    }


    public List<Student> findByName(String name) {
        return dao.findByName(name)
                  .stream()
                  .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                  .collect(Collectors.toList());
    }


    public Optional<String> validateStudent(Student s) {
        return Stream.<Optional<String>>of(
                Validator.checkField(Validator.isValidId(s.getId()),          Validator.idError()),
                Validator.checkField(Validator.isNotBlank(s.getName()),       Validator.blankError("Name")),
                Validator.checkField(Validator.isValidEmail(s.getEmail()),    Validator.emailError()),
                Validator.checkField(Validator.isValidPhone(s.getPhone()),    Validator.phoneError()),
                Validator.checkField(Validator.isValidGpa(s.getGpa()),        Validator.gpaError())
        )
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
    }
}
