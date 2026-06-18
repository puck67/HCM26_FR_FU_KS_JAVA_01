package fa.training.jsfw_m_a101.service;

import fa.training.jsfw_m_a101.model.Course;
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
            System.out.println("Successfully saved course list to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error while writing courses to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println(FILE_NAME + " does not exist. Returning empty list.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while reading courses from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
