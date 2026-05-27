package controller;

import dao.TeacherDAO;
import model.Teacher;

import java.util.List;

public class TeacherController {

    private final TeacherDAO dao;

    public TeacherController() {
        this.dao = new TeacherDAO();
    }

    public String addTeacher(String id, String name, String email,
                             String phone, double salary) {
        if (id.length() > 10) {
            return "ERROR: ID must be at most 10 characters.";
        }
        if (dao.findById(id) != null) {
            return "ERROR: A teacher with ID '" + id + "' already exists.";
        }

        // Check duplicate email and phone
        if (dao.findByEmail(email) != null) {
            return "ERROR: Email '" + email + "' is already registered.";
        }
        if (dao.findByPhone(phone) != null) {
            return "ERROR: Phone '" + phone + "' is already registered.";
        }

        Teacher teacher = new Teacher(id, name, email, phone, salary);
        boolean ok = dao.add(teacher);
        return ok ? "SUCCESS: Teacher added successfully."
                : "ERROR: Failed to add teacher (database error).";
    }

    public List<Teacher> getAllTeachers() {
        return dao.getAll();
    }

    public String updateTeacher(String id, String name, String email,
                                String phone, double salary) {
        if (dao.findById(id) == null) {
            return "ERROR: Teacher with ID '" + id + "' not found.";
        }

        // Check duplicate email and phone for update
        Teacher existingByEmail = dao.findByEmail(email);
        if (existingByEmail != null && !existingByEmail.getId().equals(id)) {
            return "ERROR: Email '" + email + "' is already used by another teacher.";
        }

        Teacher existingByPhone = dao.findByPhone(phone);
        if (existingByPhone != null && !existingByPhone.getId().equals(id)) {
            return "ERROR: Phone '" + phone + "' is already used by another teacher.";
        }

        Teacher updated = new Teacher(id, name, email, phone, salary);
        boolean ok = dao.update(updated);
        return ok ? "SUCCESS: Teacher updated successfully."
                : "ERROR: Failed to update teacher (database error).";
    }

    public String deleteTeacher(String id) {
        if (dao.findById(id) == null) {
            return "ERROR: Teacher with ID '" + id + "' not found.";
        }
        boolean ok = dao.delete(id);
        return ok ? "SUCCESS: Teacher deleted successfully."
                : "ERROR: Failed to delete teacher (database error).";
    }

    public Teacher findTeacherById(String id) {
        return dao.findById(id);
    }
}