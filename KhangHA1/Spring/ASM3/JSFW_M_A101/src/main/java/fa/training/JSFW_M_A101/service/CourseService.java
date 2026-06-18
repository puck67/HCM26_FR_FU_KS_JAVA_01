package fa.training.JSFW_M_A101.service;

import fa.training.JSFW_M_A101.model.Course;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private static final String FILE_NAME = "courses.dat";

    /**
     * Saves the list of courses to a file named courses.dat
     * using ObjectOutputStream.
     *
     * @param courses the list of courses to save
     */
    public void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println("Courses saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads the courses.dat file and returns its content as a List<Course>.
     * Uses ObjectInputStream.
     *
     * @return the list of courses read from the file
     */
    @SuppressWarnings("unchecked")
    public List<Course> getCourses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Course>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILE_NAME);
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading courses: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
