package com.example.ASM3.service;

import com.example.ASM3.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    @Value("${lms.data.file:courses.dat}")
    private String dbFile = "courses.dat";

    public void saveCourses(List<Course> courseList) {
        try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(dbFile))) {
            outStream.writeObject(courseList);
            log.info("Saved {} course(s) to '{}'", courseList.size(), dbFile);
        } catch (IOException ex) {
            log.error("Failed to save courses: {}", ex.getMessage());
            throw new RuntimeException("Could not save courses to file: " + dbFile, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        File file = new File(dbFile);
        if (!file.exists()) {
            log.warn("Data file '{}' not found, returning empty list.", dbFile);
            return new ArrayList<>();
        }
        try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(file))) {
            List<Course> courseList = (List<Course>) inStream.readObject();
            log.info("Loaded {} course(s) from '{}'", courseList.size(), dbFile);
            return courseList;
        } catch (IOException | ClassNotFoundException ex) {
            log.error("Failed to load courses: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean deleteCourse(String title) {
        List<Course> courseList = getCourses();
        boolean isDone = false;
        for (int i = courseList.size() - 1; i >= 0; i--) {
            if (courseList.get(i).getTitle().equalsIgnoreCase(title)) {
                courseList.remove(i);
                isDone = true;
            }
        }
        if (isDone) {
            saveCourses(courseList);
        }
        return isDone;
    }

    public void addCourse(Course course) {
        List<Course> courseList = getCourses();
        courseList.add(course);
        saveCourses(courseList);
    }
}
