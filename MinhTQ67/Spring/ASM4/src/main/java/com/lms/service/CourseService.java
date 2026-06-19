package com.lms.service;

import com.lms.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CourseService - được Spring quản lý như một bean (dùng @Service annotation)
 * Chịu trách nhiệm lưu/đọc danh sách Course từ file courses.dat
 */
@Service
public class CourseService {

    private static final String FILE_NAME = "courses.dat";

    /**
     * Lưu danh sách courses vào file courses.dat
     * Sử dụng ObjectOutputStream để serialize List<Course>
     */
    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println(">>> Đã lưu " + courses.size() + " course(s) vào file: " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu courses: " + e.getMessage());
        }
    }

    /**
     * Đọc danh sách courses từ file courses.dat
     * Sử dụng ObjectInputStream để deserialize List<Course>
     */
    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            courses = (List<Course>) ois.readObject();
            System.out.println(">>> Đã đọc " + courses.size() + " course(s) từ file: " + FILE_NAME);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi đọc courses: " + e.getMessage());
        }
        return courses;
    }
}
