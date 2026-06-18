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
            logger.warn("Validation failed: The provided courses list is null.");
            return;
        }

        List<Course> validCourses = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course == null) {
                logger.warn("Validation warning: Course at index " + i + " is null. Skipping.");
                continue;
            }

            List<String> errors = validateCourse(course);
            if (!errors.isEmpty()) {
                logger.warn("Validation failed for course at index " + i + " (Title: '" + course.getTitle() + "'):");
                for (String err : errors) {
                    logger.warn("  - " + err);
                }
                logger.warn("Skipping serialization for this course.");
            } else {
                validCourses.add(course);
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(validCourses);
            logger.info("Successfully serialized and saved " + validCourses.size() + " valid courses to " + FILE_NAME);
        } catch (IOException e) {
            logger.error("IO Exception while saving courses to file: " + e.getMessage(), e);
        }
    }

    public List<Course> getCourses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            logger.warn("Data file " + FILE_NAME + " does not exist. Returning empty list.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<?> list = (List<?>) obj;
                List<Course> retrievedCourses = new ArrayList<>();
                for (Object item : list) {
                    if (item instanceof Course) {
                        Course course = (Course) item;
                        List<String> errors = validateCourse(course);
                        if (errors.isEmpty()) {
                            retrievedCourses.add(course);
                        } else {
                            logger.error("Skipping corrupted or invalid course from deserialized list: " + course + " with errors: " + errors);
                        }
                    } else {
                        logger.error("Invalid item type in deserialized list: expected Course, got " + (item != null ? item.getClass().getName() : "null"));
                    }
                }
                return retrievedCourses;
            } else {
                logger.error("Deserialized object is not a list. Got: " + (obj != null ? obj.getClass().getName() : "null"));
            }
        } catch (FileNotFoundException e) {
            logger.warn("Data file " + FILE_NAME + " was not found: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Exception while reading courses from file: " + e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    private List<String> validateCourse(Course course) {
        List<String> errors = new ArrayList<>();
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            errors.add("Course title cannot be null or empty.");
        }
        if (course.getInstructorName() == null || course.getInstructorName().trim().isEmpty()) {
            errors.add("Instructor name cannot be null or empty.");
        } else if (!course.getInstructorName().trim().matches("^[a-zA-Z.\\s\\p{L}]+$")) {
            errors.add("Instructor name contains invalid characters: '" + course.getInstructorName() + "'. Only letters, spaces, and dots are allowed.");
        }
        if (course.getDescription() == null || course.getDescription().trim().isEmpty()) {
            errors.add("Course description cannot be null or empty.");
        }
        if (course.getDurationHours() <= 0) {
            errors.add("Course duration must be a positive integer, got: " + course.getDurationHours());
        }
        return errors;
    }
}
