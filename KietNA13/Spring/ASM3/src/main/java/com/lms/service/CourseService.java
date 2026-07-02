package com.lms.service;

import com.lms.model.Course;
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
 * Service xử lý lưu/đọc danh sách Course từ file nhị phân (.dat).
 *
 * Lưu ý: ObjectInputStream deserialize dữ liệu từ file — chỉ dùng với file tin cậy.
 * Nếu mở rộng sang production, nên chuyển sang JSON/DB thay vì Java serialization.
 */
@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);
    private static final String DATA_FILE = "courses.dat";

    /**
     * Lưu danh sách Course vào file courses.dat.
     * Ghi đè toàn bộ nội dung file mỗi lần gọi.
     *
     * @param courses danh sách cần lưu (không được null)
     */
    public void saveCourseList(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            log.warn("saveCourseList called with null or empty list — nothing to save.");
            return;
        }

        Path filePath = Paths.get(DATA_FILE);
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(filePath)))) {
            oos.writeObject(courses);
            log.info("Saved {} course(s) to '{}'", courses.size(), DATA_FILE);
        } catch (IOException e) {
            log.error("Failed to save courses to '{}': {}", DATA_FILE, e.getMessage(), e);
        }
    }

    /**
     * Đọc danh sách Course từ file courses.dat.
     * Trả về danh sách rỗng nếu file không tồn tại hoặc lỗi đọc — không ném exception ra ngoài.
     *
     * @return danh sách Course (unmodifiable), hoặc danh sách rỗng nếu lỗi
     */
    @SuppressWarnings("unchecked")
    public List<Course> loadCourseList() {
        Path filePath = Paths.get(DATA_FILE);

        if (!Files.exists(filePath)) {
            log.warn("Data file '{}' not found. Returning empty list.", DATA_FILE);
            return Collections.emptyList();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(filePath)))) {
            List<Course> courses = (List<Course>) ois.readObject();
            log.info("Loaded {} course(s) from '{}'", courses.size(), DATA_FILE);
            return Collections.unmodifiableList(courses);
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to read courses from '{}': {}", DATA_FILE, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
