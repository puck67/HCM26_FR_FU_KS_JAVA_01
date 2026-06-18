package com.example.demo.service;

import com.example.demo.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private static final String FILE_NAME = "courses.dat";

    public void saveCourses(List<Course> courses) {
        if (courses == null) {
            throw new IllegalArgumentException("The list of courses to save cannot be null.");
        }

        for (Course course : courses) {
            if (course == null) {
                throw new IllegalArgumentException("Course list contains null elements.");
            }
            validateCourse(course);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            logger.info("Successfully saved courses to {}", FILE_NAME);
        } catch (IOException e) {
            logger.error("Failed to save courses to file: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while saving courses to file.", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            logger.warn("File {} does not exist. Returning empty list.", FILE_NAME);
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj == null) {
                return new ArrayList<>();
            }
            if (!(obj instanceof List)) {
                throw new IllegalArgumentException("Invalid data type read from file. Expected List but found: " + obj.getClass().getName());
            }

            List<?> rawList = (List<?>) obj;
            List<Course> retrievedCourses = new ArrayList<>();
            for (Object item : rawList) {
                if (item == null) {
                    throw new IllegalArgumentException("File contains null elements inside the course list.");
                }
                if (!(item instanceof Course)) {
                    throw new IllegalArgumentException("Invalid element type inside list: " + item.getClass().getName());
                }
                Course course = (Course) item;
                validateCourse(course);
                retrievedCourses.add(course);
            }
            return retrievedCourses;
        } catch (FileNotFoundException e) {
            logger.warn("File {} not found: {}", FILE_NAME, e.getMessage());
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Failed to read courses from file: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while reading courses from file.", e);
        }
    }

    private void validateCourse(Course course) {
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be null or empty.");
        }
        if (course.getInstructorName() == null || course.getInstructorName().trim().isEmpty()) {
            throw new IllegalArgumentException("Instructor name cannot be null or empty.");
        }
        if (course.getDescription() == null || course.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (course.getDurationHours() <= 0) {
            throw new IllegalArgumentException("Duration hours must be a positive integer.");
        }
    }
}
