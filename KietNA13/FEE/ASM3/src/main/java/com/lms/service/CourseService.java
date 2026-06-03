package com.lms.service;

import com.lms.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CourseService implements DataService<Course> {

    private static final Logger log = Logger.getLogger(CourseService.class.getName());
    private static final String FILE_PATH = "courses.dat";

    @Override
    public void save(List<Course> items) {
        if (items == null) throw new IllegalArgumentException("Course list must not be null");

        try (var fos = new FileOutputStream(FILE_PATH);
             var bos = new BufferedOutputStream(fos);
             var oos = new ObjectOutputStream(bos)) {

            oos.writeObject(items);
            log.info("Saved " + items.size() + " course(s) to " + FILE_PATH);

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to save courses", e);
            throw new RuntimeException("Save failed: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Course> getAll() {
        var file = new File(FILE_PATH);
        if (!file.exists()) {
            log.warning("courses.dat not found — returning empty list");
            return Collections.emptyList();
        }

        try (var fis = new FileInputStream(file);
             var bis = new BufferedInputStream(fis);
             var ois = new ObjectInputStream(bis)) {

            var result = (List<Course>) ois.readObject();
            log.info("Loaded " + result.size() + " course(s) from " + FILE_PATH);
            return result;

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Failed to load courses", e);
            throw new RuntimeException("Load failed: " + e.getMessage(), e);
        }
    }
}
