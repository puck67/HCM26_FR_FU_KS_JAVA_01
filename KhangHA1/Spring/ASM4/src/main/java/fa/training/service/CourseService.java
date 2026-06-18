package fa.training.service;

import fa.training.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private static final String FILE_NAME = "courses.dat";

    /**
     * Saves a list of courses to the file courses.dat using ObjectOutputStream.
     */
    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println("Courses saved to " + FILE_NAME + " successfully.");
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    /**
     * Reads the contents of courses.dat and returns the list of courses.
     */
    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            courses = (List<Course>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILE_NAME);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading courses: " + e.getMessage());
        }
        return courses;
    }
}
