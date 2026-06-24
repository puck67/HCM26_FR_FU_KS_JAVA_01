package com.lms.service;

import com.lms.model.LmsUser;
import com.lms.model.Student;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Task 4: LmsDataService - được Spring quản lý qua @Service.
 *
 * Chịu trách nhiệm lưu/đọc LmsUser (kể cả Instructor) và Student ra file.
 * Vì Instructor extends LmsUser và List<LmsUser> có thể chứa cả hai loại,
 * Java serialization sẽ tự xử lý đúng kiểu thực tế khi deserialize (polymorphism).
 */
@Service
public class LmsDataService {

    private static final String USERS_FILE    = "users.dat";
    private static final String STUDENTS_FILE = "students.dat";

    // ----------------------------------------------------------------
    // LmsUser (bao gồm cả Instructor) → users.dat
    // ----------------------------------------------------------------

    /**
     * Lưu list LmsUser (có thể chứa cả Instructor) vào users.dat
     */
    public void saveUsers(List<LmsUser> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            System.out.println(">>> Đã lưu " + users.size() + " user(s) vào file: " + USERS_FILE);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu users: " + e.getMessage());
        }
    }

    /**
     * Đọc list LmsUser từ users.dat
     */
    @SuppressWarnings("unchecked")
    public List<LmsUser> getUsers() {
        List<LmsUser> users = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (List<LmsUser>) ois.readObject();
            System.out.println(">>> Đã đọc " + users.size() + " user(s) từ file: " + USERS_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc users: " + e.getMessage());
        }
        return users;
    }

    // ----------------------------------------------------------------
    // Student → students.dat
    // ----------------------------------------------------------------

    /**
     * Lưu list Student vào students.dat
     */
    public void saveStudents(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STUDENTS_FILE))) {
            oos.writeObject(students);
            System.out.println(">>> Đã lưu " + students.size() + " student(s) vào file: " + STUDENTS_FILE);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu students: " + e.getMessage());
        }
    }

    /**
     * Đọc list Student từ students.dat
     */
    @SuppressWarnings("unchecked")
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STUDENTS_FILE))) {
            students = (List<Student>) ois.readObject();
            System.out.println(">>> Đã đọc " + students.size() + " student(s) từ file: " + STUDENTS_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc students: " + e.getMessage());
        }
        return students;
    }
}
