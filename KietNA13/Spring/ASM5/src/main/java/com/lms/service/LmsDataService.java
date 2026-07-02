package com.lms.service;

import com.lms.model.LmsUser;
import com.lms.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * LmsDataService — Spring-managed bean (@Service).
 *
 * Chịu trách nhiệm lưu/đọc LmsUser (kể cả Instructor) và Student ra file.
 * Vì Instructor extends LmsUser và List<LmsUser> có thể chứa cả hai loại,
 * Java serialization sẽ tự xử lý đúng kiểu thực tế khi deserialize (polymorphism).
 *
 * Dùng BufferedStream + NIO Files API để tối ưu I/O.
 */
@Service
public class LmsDataService {

    private static final Logger log = LoggerFactory.getLogger(LmsDataService.class);

    private static final String USERS_FILE    = "users.dat";
    private static final String STUDENTS_FILE = "students.dat";

    // ── LmsUser (bao gồm cả Instructor) → users.dat ──────────────────────────

    /**
     * Lưu list LmsUser (có thể chứa cả Instructor) vào users.dat.
     *
     * @param users danh sách user cần lưu (không được null/rỗng)
     */
    public void saveUserList(List<LmsUser> users) {
        if (users == null || users.isEmpty()) {
            log.warn("saveUserList called with null or empty list — nothing to save.");
            return;
        }
        writeObjectToFile(users, USERS_FILE);
        log.info("Saved {} user(s) to '{}'", users.size(), USERS_FILE);
    }

    /**
     * Đọc list LmsUser từ users.dat.
     *
     * @return danh sách LmsUser (unmodifiable), hoặc rỗng nếu file không tồn tại/lỗi đọc
     */
    @SuppressWarnings("unchecked")
    public List<LmsUser> loadUserList() {
        return (List<LmsUser>) readObjectFromFile(USERS_FILE);
    }

    // ── Student → students.dat ────────────────────────────────────────────────

    /**
     * Lưu list Student vào students.dat.
     *
     * @param students danh sách student cần lưu (không được null/rỗng)
     */
    public void saveStudentList(List<Student> students) {
        if (students == null || students.isEmpty()) {
            log.warn("saveStudentList called with null or empty list — nothing to save.");
            return;
        }
        writeObjectToFile(students, STUDENTS_FILE);
        log.info("Saved {} student(s) to '{}'", students.size(), STUDENTS_FILE);
    }

    /**
     * Đọc list Student từ students.dat.
     *
     * @return danh sách Student (unmodifiable), hoặc rỗng nếu file không tồn tại/lỗi đọc
     */
    @SuppressWarnings("unchecked")
    public List<Student> loadStudentList() {
        return (List<Student>) readObjectFromFile(STUDENTS_FILE);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Ghi object vào file bằng ObjectOutputStream + BufferedOutputStream (NIO). */
    private void writeObjectToFile(Object data, String filename) {
        Path filePath = Paths.get(filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(filePath)))) {
            oos.writeObject(data);
        } catch (IOException e) {
            log.error("Failed to write to '{}': {}", filename, e.getMessage(), e);
        }
    }

    /** Đọc object từ file bằng ObjectInputStream + BufferedInputStream (NIO).
     *  Trả về Collections.emptyList() nếu file không tồn tại hoặc lỗi. */
    private Object readObjectFromFile(String filename) {
        Path filePath = Paths.get(filename);
        if (!Files.exists(filePath)) {
            log.warn("File '{}' not found. Returning empty list.", filename);
            return Collections.emptyList();
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(filePath)))) {
            Object result = ois.readObject();
            log.info("Loaded data from '{}'", filename);
            return result instanceof List ? Collections.unmodifiableList((List<?>) result) : result;
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to read from '{}': {}", filename, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
