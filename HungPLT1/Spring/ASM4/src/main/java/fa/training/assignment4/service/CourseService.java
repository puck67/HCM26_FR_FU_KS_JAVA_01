package fa.training.assignment4.service;

import fa.training.assignment4.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private static final String FILE_NAME = "courses.dat";

    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println("Saved " + courses.size() + " courses to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading courses: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
