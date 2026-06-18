package com.example.ASM3.service;

import com.example.ASM3.model.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CourseService {

    @Value("${lms.data.file:courses.dat}")
    private String dataFile = "courses.dat";

    /**
     * Saves the list of courses to a binary file using ObjectOutputStream.
     */
    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(courses);
            log.info("Saved {} course(s) to '{}'", courses.size(), dataFile);
        } catch (IOException e) {
            log.error("Failed to save courses: {}", e.getMessage());
            throw new RuntimeException("Could not save courses to file: " + dataFile, e);
        }
    }

    /**
     * Reads and returns the list of courses from the binary file using ObjectInputStream.
     */
    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        File file = new File(dataFile);
        if (!file.exists()) {
            log.warn("Data file '{}' not found, returning empty list.", dataFile);
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Course> courses = (List<Course>) ois.readObject();
            log.info("Loaded {} course(s) from '{}'", courses.size(), dataFile);
            return courses;
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to load courses: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Deletes a course by title and persists the updated list.
     */
    public boolean deleteCourse(String title) {
        List<Course> courses = getCourses();
        boolean removed = courses.removeIf(c -> c.getTitle().equalsIgnoreCase(title));
        if (removed) {
            saveCourses(courses);
        }
        return removed;
    }

    /**
     * Adds a single course to the persisted list.
     */
    public void addCourse(Course course) {
        List<Course> courses = getCourses();
        courses.add(course);
        saveCourses(courses);
    }
}
